package com.anread.common.enums;

public enum RelateTableEnum {
    BOOK_COMMENT("book_comment"),
    SUB_COMMENT("sub_comment");

    private final String tableName;

    RelateTableEnum(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
