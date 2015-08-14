package com.johnnymolina.androidudacitymovieproject.api.model.modelRealm;

import io.realm.RealmObject;

/**
 * Created by Johnny on 8/13/2015.
 */
public class RealmResultMedia extends RealmObject{

    private String id;

    private String name;

    private String site;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
