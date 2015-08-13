package com.johnnymolina.androidudacitymovieproject.api.modelUI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 8/5/2015.
 */
public class MovieReviewRequestResponse {

    @Expose private int id;
    @Expose private int page;
    @SerializedName("results")
    @Expose private List<ResultReview> resultsReview = new ArrayList<>();
    @SerializedName("total_pages")
    @Expose private int totalPages;
    @SerializedName("total_results")
    @Expose private int totalResults;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The page
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<ResultReview> getResultsReview() {
        return resultsReview;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResultsReview(List<ResultReview> results) {
        this.resultsReview = results;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The totalResults
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
