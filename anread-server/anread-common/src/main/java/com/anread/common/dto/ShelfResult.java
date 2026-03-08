package com.anread.common.dto;

public class ShelfResult<T> extends Result<T> {
    private boolean hasMore;
    public boolean hasMore() {
        return hasMore;
    }
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
