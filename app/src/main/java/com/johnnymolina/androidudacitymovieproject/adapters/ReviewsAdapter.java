package com.johnnymolina.androidudacitymovieproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnnymolina.androidudacitymovieproject.api.model.Result;
import com.johnnymolina.androidudacitymovieproject.api.model.ResultReview;
import com.johnnymolina.androidudacitymovieproject.extended.AutofitRecyclerView;
import com.johnnymolina.androidudacityspotifyproject.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Johnny on 8/9/2015.
 */
public class ReviewsAdapter extends AutofitRecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    List<ResultReview> reviews;
    Context context;

    public ReviewsAdapter() {
    }

    public List<ResultReview> getReviews() {
        return reviews ;
    }

    public void setReviews(List<ResultReview> reviews) {
        this.reviews = reviews;
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
        ResultReview review = reviews.get(position);

        holder.authorTextView.setText(review.getAuthor());
        holder.contentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

}