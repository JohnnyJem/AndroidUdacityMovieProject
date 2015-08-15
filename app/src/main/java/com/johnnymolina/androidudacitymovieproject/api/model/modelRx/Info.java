package com.johnnymolina.androidudacitymovieproject.api.model.modelRx;

/**
 * Created by Johnny on 8/12/2015.
 */
public class Info {
    private final int id;
    private final String title;
    private final String posterPath;
    private final String releaseDate;
    private final double voteAverage;
    private final String overview;

    public Info(int id, String title, String posterPath, String releaseDate, double voteAverage, String overview) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

}
