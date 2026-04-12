package com.anread.filesystem.utils;

import com.anread.common.entity.Chapter;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import org.jsoup.Jsoup;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class EpublibUtil {

    @Autowired
    private MinioUtil minioUtil;

    public byte[] extractCover(MultipartFile file) {
        InputStream in = null;
        byte[] data = null;
        try {
            EpubReader reader = new EpubReader();
            in = file.getInputStream();
            Book book = reader.readEpub(in);
            if (book.getCoverImage() != null) {
                data = book.getCoverImage().getData();
            } else if (book.getResources().getById("cover.jpg") != null) {
                data =  book.getResources().getById("cover.jpg").getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public Mono<byte[]> extractCoverReactive(MultipartFile file) {
        return Mono.fromCallable(() -> extractCover(file));
    }

    public void parse2Minio(MultipartFile file) {
        boolean hasToc = false; // 是否存在目录
        InputStream in = null;
        try {
            EpubReader reader = new EpubReader();
            in = file.getInputStream();
            Book book = reader.readEpub(in);
            Resources resources = book.getResources(); // 全部资源
            // 书脊 - 阅读顺序 - 分章节存储
            List<SpineReference> spineReferences = book.getSpine().getSpineReferences();
            Resource tocResource = book.getSpine().getTocResource();
            // 存在目录资源toc.ncx
            if (tocResource != null) {
                hasToc = true;
            }
            for (int i = 0; i < spineReferences.size(); i++) {
                Resource resource = spineReferences.get(i).getResource();
                resources.remove(resource.getHref()); // 去除全部资源当中的内容部分
                // 将内容部分按序号放入minio当中
                minioUtil.uploadBookContent(file.getName(), i, resource.getMediaType(), resource.getData());
            }
            // 把剩余的资源存储到minio当中
            Collection<String> allHrefs = resources.getAllHrefs();
            for (String href : allHrefs) {
                Resource resource = resources.getByHref(href);
                if (resource.getId().equals("nav")) {
                    hasToc = true;
                }
                minioUtil.uploadBookResources(file.getName(), href, resource.getMediaType(), resource.getData());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Chapter> parse2Chapters(InputStream inputStream) {
        List<Chapter> chapters = new ArrayList<>();
        try {
            EpubReader reader = new EpubReader();
            Book book = reader.readEpub(inputStream);
            List<TOCReference> tocReferences = book.getTableOfContents().getTocReferences();

            if (tocReferences != null && !tocReferences.isEmpty()) {
                extractChaptersRecursive(tocReferences, chapters, 0, 0);
            } else {
                // Fallback: 如果没有TOC，尝试从spine中提取
                chapters = extractChaptersFromSpine(book);
            }
        } catch (Exception e) {
            log.error("章节获取失败：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return chapters;
    }

    public static void main(String[] args) {
        List<Chapter> chapters = new ArrayList<>();
        try {
            EpubReader reader = new EpubReader();
            URL url = new URL("http://127.0.0.1:9000/books/4801e4e026e2f59101eb03a00fbdd5c7/等待戈多 (塞缪尔·贝克特) (z-library.sk, 1lib.sk, z-lib.sk).epub");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Host", "127.0.0.1:9000"); // 必须和 URL 中的 host 一致
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Java EPUB Parser)");
            Book book = reader.readEpub(connection.getInputStream());
            Spine spine = book.getSpine();
            /*List<TOCReference> tocReferences = book.getTableOfContents().getTocReferences();

            if (tocReferences != null && !tocReferences.isEmpty()) {
                extractChaptersRecursive(tocReferences, chapters, 0, 0);
            } else {
                // Fallback: 如果没有TOC，尝试从spine中提取
                chapters = extractChaptersFromSpine(book);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Chapter> extractChaptersFromSpine(Book book) {
        List<Chapter> chapters = new ArrayList<>();
        int index = 0;

        for (int i = 0; i < book.getSpine().getSpineReferences().size(); i++) {
            Resource resource = book.getSpine().getResource(index);
            if (resource != null && resource.getHref() != null) {
                String content = extractTextFromResource(resource);
                if (!content.trim().isEmpty()) {
                    // 尝试从内容中提取章节标题
                    String title = extractChapterTitle(content, "Chapter " + (index + 1));
                    Chapter chapter = new Chapter(title, content, index, 0);
                    chapters.add(chapter);
                    index++;
                }
            }
        }

        return chapters;
    }

    private static String extractChapterTitle(String content, String defaultTitle) {
        // 尝试从内容开头提取章节标题
        String[] lines = content.split("\n");
        if (lines.length > 0) {
            String firstLine = lines[0].trim();
            if (firstLine.length() <= 100 && firstLine.matches(".*[第章节].*")) {
                return firstLine;
            }
        }
        return defaultTitle;
    }


    public static void extractChaptersRecursive(List<TOCReference> tocReferences,
                                                List<Chapter> chapters,
                                                int level,
                                                int startIndex) {
        for (int i = 0; i < tocReferences.size(); i++) {
            TOCReference tocRef = tocReferences.get(i);
            String title = tocRef.getTitle();
            Resource resource = tocRef.getResource();

            if (resource != null) {
                String content = extractTextFromResource(resource);
                if (!content.trim().isEmpty()) {
                    Chapter chapter = new Chapter(title, content, startIndex + chapters.size(), level);
                    chapters.add(chapter);
                }
            }

            // 处理子章节
            if (!tocRef.getChildren().isEmpty()) {
                extractChaptersRecursive(tocRef.getChildren(), chapters, level + 1, chapters.size());
            }
        }
    }

    private static String extractTextFromResource(Resource resource) {
        try {
            byte[] data = resource.getData();
            if (data == null) return "";

            String htmlContent = new String(data, StandardCharsets.UTF_8);
            return cleanHtmlContent(htmlContent);
        } catch (Exception e) {
            System.err.println("Error extracting text from resource: " + e.getMessage());
            return "";
        }
    }

    private static String cleanHtmlContent(String htmlContent) {
        if (htmlContent == null || htmlContent.trim().isEmpty()) {
            return "";
        }


        Document doc = Jsoup.parse(htmlContent);

        // 移除不需要的元素
        Elements unwantedElements = doc.select("script, style, footer, aside, nav, figure, .footnote");
        unwantedElements.remove();

        // 提取纯文本，保留段落结构
        String text = doc.body().text();

        // 清理多余的空白字符
        text = text.replaceAll("\\s+", " ");
        text = text.replaceAll("([。！？；])\\s*", "$1\n");

        return text.trim();
    }
}
