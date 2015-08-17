package com.johnnymolina.androidudacitymovieproject.api.model.modelRealm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Johnny on 8/13/2015.
 */
public class RealmMovieMedia extends RealmObject{

    private String id;
    private String name;
    private String site;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }





}
