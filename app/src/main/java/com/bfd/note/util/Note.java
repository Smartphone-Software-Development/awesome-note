package com.bfd.note.util;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class Note extends DataSupport {

    private static final int DEFAULT_SHORT_MESSAGE_LENGTH = 50;

    private String content;

    private Long id;
    private String title;

    private double latitude, longitude;

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

    public String getShortContent() {
        if (content.length() < DEFAULT_SHORT_MESSAGE_LENGTH) {
            return content;
        } else {
            return content.substring(0, DEFAULT_SHORT_MESSAGE_LENGTH) + "...";
        }
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Note && ((Note) o).id.equals(id);
    }
}
