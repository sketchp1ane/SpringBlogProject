package com.javaweb.finalwork;

import jakarta.persistence.*;


@Entity // This tells Hibernate to make a table out of this class
public class QueryResult {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String sort;

    private String title;

    private String content;

    @Lob
    @Column(name = "image_data", columnDefinition = "BLOB")
    private byte[] imageData;

    public QueryResult() {}

    // 带参构造函数
    public QueryResult(String sort, String title, String content, byte[] imageData) {
        this.sort = sort;
        this.title = title;
        this.content = content;
        this.imageData = imageData;
    }

    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}




