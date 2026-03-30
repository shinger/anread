import uuid
import shutil
import logging
from pathlib import Path
from typing import Optional

import httpx
from fastapi import FastAPI, File, UploadFile, HTTPException, BackgroundTasks, Request
from fastapi.responses import JSONResponse

from .converter import PDFToEpubConverter

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="PDF to EPUB Converter",
    description="Upload a PDF file, convert via OCR, then forward the EPUB to the book API",
    version="2.0.0",
)

UPLOAD_DIR = Path("/tmp/pdf2epub/uploads")
OUTPUT_DIR = Path("/tmp/pdf2epub/outputs")
UPLOAD_DIR.mkdir(parents=True, exist_ok=True)
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

converter = PDFToEpubConverter()

# 下游 Java 接口地址，可通过环境变量覆盖
import os
BOOK_API_URL = os.getenv("BOOK_API_URL", "http://localhost:8000/file-api/file/book")
AUTHORIZATION_TOKEN = os.getenv("AUTHORIZATION_TOKEN")


def cleanup_files(*paths: Path):
    for path in paths:
        try:
            if path.exists():
                path.unlink()
                logger.info(f"Cleaned up: {path}")
        except Exception as e:
            logger.warning(f"Failed to clean up {path}: {e}")


@app.get("/", summary="Health check")
async def root():
    return {"status": "ok", "service": "PDF to EPUB Converter"}


@app.get("/health", summary="Health check endpoint")
async def health():
    return {"status": "healthy"}


@app.post("/convert", summary="Convert PDF to EPUB and upload to book API")
async def convert_pdf_to_epub(
    background_tasks: BackgroundTasks,
    request: Request,
    file: UploadFile = File(..., description="PDF file to convert"),
    language: Optional[str] = "chi_sim+eng",
    title: Optional[str] = None,
    author: Optional[str] = None,
):
    if not request.headers.get("userId"):
        raise HTTPException(status_code=400, detail="User ID is required.")
    
    if not file.filename or not file.filename.lower().endswith(".pdf"):
        raise HTTPException(status_code=400, detail="Only PDF files are accepted.")

    job_id = str(uuid.uuid4())

    # 安全处理原始文件名（防止路径遍历或非法字符）
    safe_filename = "".join(c if c.isalnum() or c in "._-" else "_" for c in file.filename)
    pdf_stem = Path(safe_filename).stem

    pdf_path = UPLOAD_DIR / f"{job_id}_{pdf_stem}.pdf"
    epub_path = OUTPUT_DIR / f"{job_id}_{pdf_stem}.epub"  # 本地也用原始名（加 job_id 防冲突）

    try:
        # 1. 保存上传的 PDF
        with open(pdf_path, "wb") as f:
            shutil.copyfileobj(file.file, f)
        logger.info(f"[{job_id}] Saved PDF: {pdf_path} ({pdf_path.stat().st_size} bytes)")

        book_title = title or pdf_stem
        book_author = author or "Unknown"

        # 2. OCR 转换
        logger.info(f"[{job_id}] Starting conversion (lang={language}) ...")
        converter.convert(
            pdf_path=pdf_path,
            epub_path=epub_path,
            language=language,
            title=book_title,
            author=book_author,
        )
        logger.info(f"[{job_id}] Conversion complete: {epub_path}")
        cleanup_files(pdf_path)

        # 3. 将 EPUB POST 到下游接口 —— 使用原始文件名（不带 job_id）
        epub_upload_name = pdf_stem + ".epub"
        logger.info(f"[{job_id}] Uploading EPUB as '{epub_upload_name}' to {BOOK_API_URL} ...")

        # 读取 EPUB 文件内容到内存（避免异步文件句柄关闭问题）
        with open(epub_path, "rb") as f:
            epub_content = f.read()
        headers = {"X-User-ID": request.headers.get("userId")} # 从请求头获取 userId
        if AUTHORIZATION_TOKEN:
            headers["Authorization"] = AUTHORIZATION_TOKEN

        async with httpx.AsyncClient(timeout=60.0) as client:
            response = await client.post(
                BOOK_API_URL,
                files={"file": (epub_upload_name, epub_content, "application/epub+zip")},
                headers=headers,
            )

        logger.info(f"[{job_id}] Book API responded: HTTP {response.status_code}")

        try:
            resp_json = response.json()
        except Exception:
            resp_json = {"raw": response.text}

        if response.status_code >= 400:
            raise HTTPException(
                status_code=response.status_code,
                detail=f"Book API error: {resp_json}",
            )

        # 成功后清理 EPUB 文件
        # cleanup_files(epub_path)

        return JSONResponse(content=resp_json, status_code=response.status_code)

    except HTTPException:
        cleanup_files(pdf_path, epub_path)
        raise
    except Exception as e:
        cleanup_files(pdf_path, epub_path)
        logger.error(f"[{job_id}] Failed: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Conversion failed: {str(e)}")