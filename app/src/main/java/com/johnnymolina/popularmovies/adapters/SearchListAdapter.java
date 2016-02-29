package com.johnnymolina.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.johnnymolina.popularmovies.R;
import com.johnnymolina.popularmovies.api.model.modelRetrofit.MovieInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by fahim on 4/20/15.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    Glide glide;
    private List<MovieInfo> movies;
    Context context;

    public SearchListAdapter( ) {

    }

    public List<MovieInfo> getMovies() {
        return movies ;
    }

    public void setMovies(List<MovieInfo> movies) {
        this.movies = movies;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image) ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_images, parent, false);
        context= rowView.getContext();
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieInfo movie = movies.get(position);
        String imageUrl = "http://image.tmdb.org/t/p/w185/" + movie.getPosterPath();
        String ObjectCheck = String.valueOf(movies.size());

        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }



}
