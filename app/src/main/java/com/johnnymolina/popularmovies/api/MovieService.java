package com.johnnymolina.popularmovies.api;

import com.johnnymolina.popularmovies.api.model.modelRetrofit.ReturnedMedia;
import com.johnnymolina.popularmovies.api.model.modelRetrofit.ReturnedReviews;
import com.johnnymolina.popularmovies.api.model.modelRetrofit.ReturnedMovies;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Johnny Molina on 7/19/2015.
 * This interface is a Retrofit endpoint
 */
public interface MovieService {

        @GET("/3/discover/movie") Observable<ReturnedMovies> movieSearch(
            @Query("sort_by") String query,
            @Query("api_key") String apiKey);

        @GET("/3/movie/{id}/videos") Observable<ReturnedMedia> movieMediaRequest(
            @Path("id") String query,
            @Query("api_key") String apiKey);

        @GET("/3/movie/{id}/reviews") Observable<ReturnedReviews> movieReviewRequest(
                @Path("id") String query,
                @Query("api_key") String apiKey);

}
