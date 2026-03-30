import logging
import re
import uuid
import zipfile
from pathlib import Path

import pytesseract
from pdf2image import convert_from_path
from PIL import Image

logger = logging.getLogger(__name__)


class PDFToEpubConverter:
    DPI = 300

    def convert(
        self,
        pdf_path: Path,
        epub_path: Path,
        language: str = "chi_sim+eng",
        title: str = "Untitled",
        author: str = "Unknown",
    ) -> None:
        pages_text = self._ocr_pdf(pdf_path, language)
        self._write_epub(pages_text, epub_path, title=title, author=author)

    # ------------------------------------------------------------------ #
    # OCR
    # ------------------------------------------------------------------ #

    def _ocr_pdf(self, pdf_path: Path, language: str) -> list[str]:
        logger.info(f"Rendering PDF pages at {self.DPI} DPI ...")
        images: list[Image.Image] = convert_from_path(
            str(pdf_path), dpi=self.DPI, fmt="jpeg"
        )
        logger.info(f"Total pages: {len(images)}")
        pages_text: list[str] = []
        for idx, image in enumerate(images, start=1):
            logger.info(f"  OCR page {idx}/{len(images)} ...")
            pages_text.append(pytesseract.image_to_string(image, lang=language))
        return pages_text

    # ------------------------------------------------------------------ #
    # EPUB writer (pure zipfile — no ebooklib dependency)
    # ------------------------------------------------------------------ #

    def _write_epub(
        self,
        pages_text: list[str],
        epub_path: Path,
        title: str,
        author: str,
    ) -> None:
        book_id = str(uuid.uuid4())

        # Build chapter filenames and HTML content up front
        chapters: list[tuple[str, str, str]] = []  # (filename, nav_label, html)
        for i, text in enumerate(pages_text, start=1):
            is_zh = self._looks_chinese(text)
            label = f"第 {i} 页" if is_zh else f"Page {i}"
            fname = f"text/page_{i:04d}.xhtml"
            chapters.append((fname, label, self._page_html(text, label)))

        with zipfile.ZipFile(str(epub_path), "w", zipfile.ZIP_DEFLATED) as zf:
            # 1. mimetype — MUST be first and uncompressed
            zf.writestr(
                zipfile.ZipInfo("mimetype"),  # no compression
                "application/epub+zip",
            )

            # 2. META-INF/container.xml
            zf.writestr("META-INF/container.xml", self._container_xml())

            # 3. OEBPS/content.opf
            zf.writestr("OEBPS/content.opf", self._opf(book_id, title, author, chapters))

            # 4. OEBPS/toc.ncx
            zf.writestr("OEBPS/toc.ncx", self._ncx(book_id, title, chapters))

            # 5. OEBPS/nav.xhtml
            zf.writestr("OEBPS/nav.xhtml", self._nav(title, chapters))

            # 6. OEBPS/style/main.css
            zf.writestr("OEBPS/style/main.css", self._css())

            # 7. Chapter xhtml files
            for fname, _label, html in chapters:
                zf.writestr(f"OEBPS/{fname}", html)

        logger.info(f"EPUB written → {epub_path}  ({epub_path.stat().st_size} bytes)")

    # ------------------------------------------------------------------ #
    # EPUB XML fragments
    # ------------------------------------------------------------------ #

    @staticmethod
    def _container_xml() -> str:
        return (
            '<?xml version="1.0" encoding="UTF-8"?>\n'
            '<container version="1.0" xmlns="urn:oasis:names:tc:opendocument:xmlns:container">\n'
            '  <rootfiles>\n'
            '    <rootfile full-path="OEBPS/content.opf" media-type="application/oebps-package+xml"/>\n'
            '  </rootfiles>\n'
            '</container>'
        )

    @staticmethod
    def _opf(book_id: str, title: str, author: str, chapters: list) -> str:
        manifest_items = (
            '    <item id="nav" href="nav.xhtml" media-type="application/xhtml+xml" properties="nav"/>\n'
            '    <item id="ncx" href="toc.ncx" media-type="application/x-dtbncx+xml"/>\n'
            '    <item id="css" href="style/main.css" media-type="text/css"/>\n'
        )
        for i, (fname, _label, _html) in enumerate(chapters, start=1):
            manifest_items += f'    <item id="page{i:04d}" href="{fname}" media-type="application/xhtml+xml"/>\n'

        spine_items = "\n".join(
            f'    <itemref idref="page{i:04d}"/>' for i in range(1, len(chapters) + 1)
        )

        return (
            '<?xml version="1.0" encoding="UTF-8"?>\n'
            '<package xmlns="http://www.idpf.org/2007/opf" version="3.0" unique-identifier="uid">\n'
            '  <metadata xmlns:dc="http://purl.org/dc/elements/1.1/">\n'
            f'    <dc:identifier id="uid">{book_id}</dc:identifier>\n'
            f'    <dc:title>{_esc(title)}</dc:title>\n'
            f'    <dc:creator>{_esc(author)}</dc:creator>\n'
            '    <dc:language>zh</dc:language>\n'
            '    <meta property="dcterms:modified">2024-01-01T00:00:00Z</meta>\n'
            '  </metadata>\n'
            '  <manifest>\n'
            f'{manifest_items}'
            '  </manifest>\n'
            '  <spine toc="ncx">\n'
            f'{spine_items}\n'
            '  </spine>\n'
            '</package>'
        )

    @staticmethod
    def _ncx(book_id: str, title: str, chapters: list) -> str:
        nav_points = ""
        for i, (fname, label, _html) in enumerate(chapters, start=1):
            nav_points += (
                f'  <navPoint id="np{i:04d}" playOrder="{i}">\n'
                f'    <navLabel><text>{_esc(label)}</text></navLabel>\n'
                f'    <content src="{fname}"/>\n'
                '  </navPoint>\n'
            )
        return (
            '<?xml version="1.0" encoding="UTF-8"?>\n'
            '<!DOCTYPE ncx PUBLIC "-//NISO//DTD ncx 2005-1//EN" "http://www.daisy.org/z3986/2005/ncx-2005-1.dtd">\n'
            '<ncx xmlns="http://www.daisy.org/z3986/2005/ncx/" version="2005-1">\n'
            '  <head>\n'
            f'    <meta name="dtb:uid" content="{book_id}"/>\n'
            '  </head>\n'
            f'  <docTitle><text>{_esc(title)}</text></docTitle>\n'
            '  <navMap>\n'
            f'{nav_points}'
            '  </navMap>\n'
            '</ncx>'
        )

    @staticmethod
    def _nav(title: str, chapters: list) -> str:
        items = "\n".join(
            f'      <li><a href="{fname}">{_esc(label)}</a></li>'
            for fname, label, _ in chapters
        )
        return (
            '<?xml version="1.0" encoding="UTF-8"?>\n'
            '<!DOCTYPE html>\n'
            '<html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" xml:lang="zh">\n'
            '<head><meta charset="utf-8"/>'
            f'<title>{_esc(title)}</title></head>\n'
            '<body>\n'
            '  <nav epub:type="toc" id="toc">\n'
            f'    <h1>{_esc(title)}</h1>\n'
            '    <ol>\n'
            f'{items}\n'
            '    </ol>\n'
            '  </nav>\n'
            '</body>\n'
            '</html>'
        )

    @staticmethod
    def _css() -> str:
        return (
            "body { font-family: serif; line-height: 1.8; margin: 1em 2em; color: #222; }\n"
            "h1 { font-size: 1.3em; border-bottom: 1px solid #ccc; margin-bottom: .5em; }\n"
            "p { margin: .4em 0; text-indent: 2em; white-space: pre-wrap; }\n"
        )

    @staticmethod
    def _page_html(text: str, label: str) -> str:
        safe = (
            text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace('"', "&quot;")
        )
        parts = re.split(r"\n{2,}", safe)
        if len(parts) <= 1:
            parts = [l for l in safe.splitlines() if l.strip()]
        paras = "\n".join(f"  <p>{p.strip()}</p>" for p in parts if p.strip()) \
                or "  <p>(此页未能识别文字)</p>"
        return (
            '<?xml version="1.0" encoding="UTF-8"?>\n'
            '<!DOCTYPE html>\n'
            '<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh">\n'
            '<head>\n'
            '  <meta charset="utf-8"/>\n'
            f'  <title>{_esc(label)}</title>\n'
            '  <link rel="stylesheet" type="text/css" href="../../style/main.css"/>\n'
            '</head>\n'
            '<body>\n'
            f'  <h1>{_esc(label)}</h1>\n'
            f'{paras}\n'
            '</body>\n'
            '</html>'
        )

    @staticmethod
    def _looks_chinese(text: str) -> bool:
        cjk = sum(1 for c in text if "\u4e00" <= c <= "\u9fff")
        return len(text) > 0 and cjk / len(text) > 0.05


def _esc(s: str) -> str:
    """Minimal XML escape for attribute/text content."""
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace('"', "&quot;")
