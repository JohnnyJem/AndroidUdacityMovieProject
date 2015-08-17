package com.johnnymolina.androidudacitymovieproject.api.model.modelRx;

/**
 * Created by Johnny on 8/13/2015.
 */
public class Review {
    private String id;
    private String author;
    private String content;
    private String url;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

}
