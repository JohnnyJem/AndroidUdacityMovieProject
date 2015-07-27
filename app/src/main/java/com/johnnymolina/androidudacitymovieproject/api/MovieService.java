package com.johnnymolina.androidudacitymovieproject.api;

import com.johnnymolina.androidudacitymovieproject.api.model.MovieSearchResponse;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Johnny Molina on 7/19/2015.
 * This interface is a Retrofit endpoint
 */
public interface MovieService {

    @GET("/3/discover/movie")
    Observable<MovieSearchResponse> movieSearch(
            @Query("sort_by") String query,
            @Query("api_key") String apiKey);
}
