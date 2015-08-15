package com.johnnymolina.androidudacitymovieproject.api.model.modelPogo;

import com.google.gson.annotations.Expose;

/**
 * Created by Johnny on 8/13/2015.
 */
public class Media {
    private String id;
    private String name;
    private String site;

    public Media(String id, String name, String site) {
        this.id = id;
        this.name = name;
        this.site = site;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

}
