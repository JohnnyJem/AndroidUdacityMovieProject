package com.johnnymolina.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johnnymolina.popularmovies.api.model.modelRetrofit.MovieReview;
import com.johnnymolina.popularmovies.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Johnny on 8/9/2015.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    List<MovieReview> reviews;
    Context context;

    public ReviewsAdapter() {
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }

    public List<MovieReview> getReviews() {
        return reviews ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card_view_reviews_text_author) TextView authorTextView;
        @Bind(R.id.card_view_reviews_text_content) TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_reviews, parent, false);
        context = rowView.getContext();
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieReview review = reviews.get(position);
        holder.authorTextView.setText(review.getAuthor());
        holder.contentTextView.setText(review.getContent());
        holder.authorTextView.setTextColor(context.getResources().getColor(R.color.primary_dark_material_light));
        holder.contentTextView.setTextColor(context.getResources().getColor(R.color.primary_dark_material_light));
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

}