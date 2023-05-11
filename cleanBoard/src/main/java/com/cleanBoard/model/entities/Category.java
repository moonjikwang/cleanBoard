package com.cleanBoard.model.entities;

public enum Category {
    FREE("board"),
    NOTICE("notice"),
    GALLERY("gallery");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
