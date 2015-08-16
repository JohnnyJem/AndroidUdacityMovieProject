package com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 8/5/2015.
 */
public class ReturnedMedia {

        @Expose private int id;
        @SerializedName("results")
        @Expose private List<MovieMedia> resultsMedia = new ArrayList<>();

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
        public List<MovieMedia> getResultsMedia() {
                return resultsMedia;
        }

        /**
         *
         * @param resultsMedia
         * The resultsMedia
         */
        public void setResultsMedia(List<MovieMedia> resultsMedia) {
                this.resultsMedia = resultsMedia;
        }
}
