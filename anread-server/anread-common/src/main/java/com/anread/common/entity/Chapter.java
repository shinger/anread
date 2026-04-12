package com.anread.common.entity;

import lombok.Data;

import java.util.Map;

@Data
public class Chapter {
    private String title;
    private String content;
    private int chapterIndex;
    private int level;
    private Map<String, Object> metadata;

    public Chapter(String title, String content, int chapterIndex, int level) {
        this.title = title;
        this.content = content;
        this.chapterIndex = chapterIndex;
        this.level = level;
    }
}
