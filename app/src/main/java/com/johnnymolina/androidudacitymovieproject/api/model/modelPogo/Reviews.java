package com.johnnymolina.androidudacitymovieproject.api.model.modelPogo;

/**
 * Created by Johnny on 8/13/2015.
 */
public class Reviews {
    private String id;
    private String author;
    private String content;
    private String url;

    public Reviews(String id, String author, String content, String url) {
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
