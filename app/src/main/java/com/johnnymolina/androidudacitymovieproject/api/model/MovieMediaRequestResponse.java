package com.johnnymolina.androidudacitymovieproject.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 8/5/2015.
 */
public class MovieMediaRequestResponse {

        @Expose
        private int id;

        @SerializedName("results")
        @Expose
        private List<ResultMedia> resultsMedia = new ArrayList<>();

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
         * The resultsMedia
         */
        public List<ResultMedia> getResultsMedia() {
                return resultsMedia;
        }

        /**
         *
         * @param resultsMedia
         * The resultsMedia
         */
        public void setResultsMedia(List<ResultMedia> resultsMedia) {
                this.resultsMedia = resultsMedia;
        }
}
