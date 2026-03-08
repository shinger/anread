package com.anread.common.enums;

public enum ReadingStatus {

    UNREAD(0, "未标记"),
    READING(1, "在读"),
    READOVER(2, "读完");

    private final int status;

    private final String detail;

    ReadingStatus(int status, String detail) {
        this.status = status;
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

}
