package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dd.realmbrowser.RealmFilesActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.RestoreableViewState;
import com.johnnymolina.androidudacitymovieproject.MovieApplication;
import com.johnnymolina.androidudacitymovieproject.adapters.ReviewsAdapter;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieReview;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieReview;
import com.johnnymolina.androidudacitymovieproject.eventBus.RxBus;
import com.johnnymolina.androidudacitymovieproject.mvp.mainSearch.ActivityMain;
import com.johnnymolina.androidudacityspotifyproject.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFrag extends MvpViewStateFragment<DetailsFragView,DetailsFragPresenter> implements DetailsFragView {
    public static final int VIEWFLIPPER_RESULTS = 0;
    public static final int VIEWFLIPPER_LOADING = 1;

    @Inject MovieService movieService;
    @Inject MovieApplication movieApplication;
    @Inject RxBus rxBus;
    @Inject SharedPreferences sharedPreferences;
    @Inject Realm realm;


    @Bind(R.id.detail_linear_layout) LinearLayout linearLayout;
    @Bind(R.id.detail_media_linear_layout) LinearLayout detailMediaLinearLayout;
    @Bind(R.id.detail_review_linear_layout) LinearLayout detailReviewLinearLayout;
    @Bind(R.id.detail_title) TextView title;
    @Bind(R.id.detail_plot) TextView plot;
    @Bind(R.id.detail_user_rating) TextView userRating;
    @Bind(R.id.detail_release_date) TextView releaseDate;
    @Bind(R.id.detail_image) ImageView image;
    @Bind(R.id.favorite_star) ImageButton favoriteStarButton;
    @Bind(R.id.view_flipper) ViewFlipper viewFlipper;

    int loadState = 0;
    boolean saved = false;
    // using a retained fragment to hold the ViewSta    te
    // and the adapter for the RecyclerView
    ReviewsAdapter reviewsAdapter;
    int savedMovieID;
    String savedFirstMediaLink;

    //Injecting our dagger dependencies
    @Override
    protected void injectDependencies() {
        ((MovieApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean isRetainingInstance() {
        return super.isRetainingInstance();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //set this fragments details
        if (savedMovieID > 0){
            presenter.initFrag(realm, sharedPreferences, savedMovieID);
        }else {
            savedMovieID = ((ActivityMain) getActivity()).getMovieID();
            presenter.initFrag(realm, sharedPreferences, ((ActivityMain) getActivity()).getMovieID());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public DetailsFragPresenter createPresenter() {
        return new DetailsFragPresenter(movieService);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_details;
    }

    @Override
    public RestoreableViewState createViewState() {
        return new DetailsFragViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        showSearchList();
    }

    @Override
    public void setData(RealmMovieInfo movieInfo) {
            ((ActivityMain) getActivity()).getSupportActionBar().setTitle("Movie Details");
            linearLayout.setVisibility(View.VISIBLE);
                title.setText(movieInfo.getTitle());
                plot.setText(movieInfo.getOverview());
                userRating.setText("" + String.valueOf(movieInfo.getVoteAverage()) + "/10");
                releaseDate.setText(movieInfo.getReleaseDate().substring(0, 4));

                String imageUrl = "http://image.tmdb.org/t/p/w185/" + movieInfo.getPosterPath();
                Glide.with(this)
                        .load(imageUrl)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.placeholderdrawable)
                        .fitCenter()
                        .into(image);
    }

    @Override
    public void setRealmData(RealmReturnedMovie realmReturnedMovie){

        ((ActivityMain) getActivity()).getSupportActionBar().setTitle("Movie Details");
        favoriteStarButton.setEnabled(false);
        favoriteStarButton.setImageDrawable(getResources().getDrawable(R.drawable.button_pressed));

        //set realmMovieInfo fields to views
        linearLayout.setVisibility(View.VISIBLE);
        title.setText(realmReturnedMovie.getRealmMovieInfo().getTitle());
        plot.setText(realmReturnedMovie.getRealmMovieInfo().getOverview());
        userRating.setText("" + String.valueOf(realmReturnedMovie.getRealmMovieInfo().getVoteAverage()) + "/10");
        releaseDate.setText(realmReturnedMovie.getRealmMovieInfo().getReleaseDate().substring(0, 4));
        String imagePath = "http://image.tmdb.org/t/p/w185/" + realmReturnedMovie.getRealmMovieInfo().getPosterPath();//load file pile
        Glide.with(this)
                .load(imagePath)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.placeholderdrawable)
                .fitCenter()
                .into(image);

        //set realmMedia fields to views
            if (detailMediaLinearLayout.getChildCount()==0) {
                if (realmReturnedMovie.getRealmMediaList().get(0)!=null){
                    savedFirstMediaLink = "http://www.youtube.com/watch?v=" + realmReturnedMovie.getRealmMediaList().get(0).getKey();
                }
            for (final RealmMovieMedia movieMedia : realmReturnedMovie.getRealmMediaList()) {
                TextView linkTextView = new TextView(getActivity());
                linkTextView.setText(movieMedia.getName());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 5, 0, 1);
                linkTextView.setLayoutParams(lp);
                detailMediaLinearLayout.addView(linkTextView);
                linkTextView.setTextColor(getResources().getColor(R.color.primary_dark_material_light));
                linkTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieMedia.getKey())));
                    }
                });
            }
        }

        //set realmReview fields to views
        if (detailReviewLinearLayout.getChildCount()==0) {
            for (RealmMovieReview movieReview : realmReturnedMovie.getRealmReviewList()) {
                TextView linkTextView = new TextView(getActivity());
                linkTextView.setText(movieReview.getAuthor() + "\n" + movieReview.getContent());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 5, 0, 1);
                linkTextView.setLayoutParams(lp);
                detailReviewLinearLayout.addView(linkTextView);
                linkTextView.setTextColor(getResources().getColor(R.color.primary_dark_material_light));
            }
        }
    }

    @Override
    public void setDataMedia(final List<MovieMedia> movieMedia) {

        if (detailMediaLinearLayout.getChildCount()==0) {
            for (final MovieMedia movieMediaTemp : movieMedia) {
                TextView linkTextView = new TextView(getActivity());
                linkTextView.setText(movieMediaTemp.getName());
                linkTextView.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                detailMediaLinearLayout.addView(linkTextView);
                linkTextView.setTextColor(getResources().getColor(R.color.primary_dark_material_light));
                linkTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieMediaTemp.getKey())));
                    }
                });
            }
        }

        loadState++;
        if (loadState>1 && !saved) {
            favoriteStarButton.setEnabled(true);
        }

    }

    @Override
    public void setDataReview(List<MovieReview> movieReviews) {
        if (detailReviewLinearLayout.getChildCount()==0) {
            for (MovieReview movieReview : movieReviews) {
                TextView linkTextView = new TextView(getActivity());
                linkTextView.setText(movieReview.getAuthor() + "\n" + movieReview.getContent());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,5,0,1);
                linkTextView.setLayoutParams(lp);
                detailReviewLinearLayout.addView(linkTextView);
                linkTextView.setTextColor(getResources().getColor(R.color.primary_dark_material_light));
            }
        }

        loadState++;
        if (loadState>1 && !saved ) {
            favoriteStarButton.setEnabled(true);
        }

    }


    @Override
    public DetailsFragViewState getViewState() {
        return (DetailsFragViewState) super.getViewState();
    }

    @Override
    public void showSearchList() {
        getViewState().setStateShowSearchList();
        viewFlipper.setDisplayedChild(VIEWFLIPPER_RESULTS);

    }

    @Override
    public RealmMovieInfo getRealmMovieInfo() {
        return ((ActivityMain) getActivity()).getCurrentResult();
    }


    @Override
    public void showLoading() {
        getViewState().setStateShowLoading();
        viewFlipper.setDisplayedChild(VIEWFLIPPER_LOADING);
    }

    @Override
    public void showError(Throwable e) {
        viewFlipper.setDisplayedChild(VIEWFLIPPER_RESULTS);
        Toast.makeText(getActivity(), "error: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.favorite_star)
    public void onClickStarButton(ImageButton button){
        //Todo: need to add wait function before alllowing presenter.addMovieToRealm() to be called.
        presenter.addMovieToRealm(realm, sharedPreferences);
        button.setEnabled(false);
        button.setImageDrawable(getResources().getDrawable(R.drawable.button_pressed));
        saved = true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String shareFirstVideo = "share_first_video";
        switch (item.getItemId()) {
            case R.id.share_first_video:
                this.overflowMenuTasks(shareFirstVideo);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void overflowMenuTasks(String itemName){
        switch (itemName) {
            case "share_first_video":
               if (detailMediaLinearLayout.getChildCount()!=0 && savedFirstMediaLink!=null){
                   Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                   sharingIntent.setType("text/plain");
                   sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                   sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, savedFirstMediaLink);
                   startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
               }
                return;
        }
    }

}
