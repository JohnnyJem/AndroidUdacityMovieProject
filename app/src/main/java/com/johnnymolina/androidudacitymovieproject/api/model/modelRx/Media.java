package com.johnnymolina.androidudacitymovieproject.api.model.modelRx;

/**
 * Created by Johnny on 8/13/2015.
 */
public class Media {
    private final String id;
    private final String name;
    private final String site;

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
