package com.bfd.note.util;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class Note extends DataSupport {

    private String content;

    private Long id;
    private String title;

    public Note(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
