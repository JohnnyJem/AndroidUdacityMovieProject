package com.johnnymolina.androidudacitymovieproject.api.model;


import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.Issue;

import java.util.List;

import rx.Observable;

/* github author: kboyarshinov/realm-rxjava-example */
public interface DataService {
    public Observable<List<Issue>> issues();
    public Observable<Issue> newIssue(String title, String body, User user, List<Label> labels);
}