package com.johnnymolina.androidudacitymovieproject.api.model.modelRx;

import java.util.List;

/**
 * Created by Johnny on 8/14/2015.
 */
public class Returned {
    private final int id;
    private final Info info;
    private final List<Media> mediaList;
    private final List<Review> reviewList;


    public Returned(int id, Info info, List<Media> mediaList, List<Review> reviewList) {
        this.id = id;
        this.info = info;
        this.mediaList = mediaList;
        this.reviewList = reviewList;
    }

    public int getId() {
        return id;
    }

    public Info getInfo() {
        return info;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }




}
