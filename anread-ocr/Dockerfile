# ── Stage 1: builder ──────────────────────────────────────────────────────────
FROM python:3.12-slim-bookworm AS builder

WORKDIR /build

# 换用阿里云 APT 镜像，解决国内网络访问 deb.debian.org 502 的问题
RUN sed -i 's|http://deb.debian.org|https://mirrors.aliyun.com|g' /etc/apt/sources.list.d/debian.sources \
    && apt-get update && apt-get install -y --no-install-recommends \
        gcc \
        libffi-dev \
    && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
# 换用阿里云 PyPI 镜像
RUN pip install --upgrade pip -i https://mirrors.aliyun.com/pypi/simple/ \
    && pip install --prefix=/install --no-cache-dir \
        -i https://mirrors.aliyun.com/pypi/simple/ \
        -r requirements.txt


# ── Stage 2: runtime ──────────────────────────────────────────────────────────
FROM python:3.12-slim-bookworm

LABEL maintainer="pdf2epub-service"
LABEL description="PDF -> EPUB conversion service using OCR (Tesseract)"

# 换用阿里云 APT 镜像
RUN sed -i 's|http://deb.debian.org|https://mirrors.aliyun.com|g' /etc/apt/sources.list.d/debian.sources \
    && apt-get update && apt-get install -y --no-install-recommends \
        poppler-utils \
        tesseract-ocr \
        tesseract-ocr-chi-sim \
        tesseract-ocr-chi-tra \
        tesseract-ocr-jpn \
        tesseract-ocr-eng \
        libgl1 \
        libglib2.0-0 \
    && rm -rf /var/lib/apt/lists/*

# Copy Python packages from builder
COPY --from=builder /install /usr/local

# ── Application ───────────────────────────────────────────────────────────────
WORKDIR /app
COPY app/ ./app/

# Create temp dirs
RUN mkdir -p /tmp/pdf2epub/uploads /tmp/pdf2epub/outputs

# Non-root user for security
RUN useradd -m -u 1001 appuser \
    && chown -R appuser:appuser /app /tmp/pdf2epub
USER appuser

EXPOSE 8000

HEALTHCHECK --interval=30s --timeout=10s --start-period=15s --retries=3 \
    CMD python -c "import urllib.request; urllib.request.urlopen('http://localhost:7000/health')"

CMD ["uvicorn", "app.main:app", \
     "--host", "0.0.0.0", \
     "--port", "7000", \
     "--workers", "2", \
     "--timeout-keep-alive", "120"]