package com.javaweb.finalwork;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class QueryResultDTO {
    private String title;
    private String content;
    private String imageData;

    public QueryResultDTO(String title, String content, String imageData) {
        this.title = title;
        this.content = content;
        this.imageData = imageData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }


}
