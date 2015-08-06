package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.RestoreableViewState;
import com.johnnymolina.androidudacitymovieproject.AppComponent;
import com.johnnymolina.androidudacitymovieproject.MovieApplication;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.model.Result;
import com.johnnymolina.androidudacitymovieproject.api.model.ResultMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.ResultReview;
import com.johnnymolina.androidudacitymovieproject.eventBus.RxBus;
import com.johnnymolina.androidudacitymovieproject.mvp.mainSearch.ActivityMain;
import com.johnnymolina.androidudacityspotifyproject.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFrag extends MvpViewStateFragment<DetailsFragView,DetailsFragPresenter> implements DetailsFragView {
    public static final int VIEWFLIPPER_RESULTS = 0;
    public static final int VIEWFLIPPER_LOADING = 1;

    @Inject MovieService movieService;
    @Inject MovieApplication movieApplication;
    @Inject RxBus _rxBus;
    private CompositeSubscription _subscriptions;
    AppComponent appComponent;

    @Bind(R.id.detail_linear_layout) LinearLayout linearLayout;
    @Bind(R.id.detail_media_linear_layout) LinearLayout detailMediaLinearLayout;
    @Bind(R.id.detail_review_linear_layout) LinearLayout detailReviewLinearLayout;
    @Bind(R.id.detail_title) TextView title;
    @Bind(R.id.detail_plot) TextView plot;
    @Bind(R.id.detail_user_rating) TextView userRating;
    @Bind(R.id.detail_release_date) TextView releaseDate;
    @Bind(R.id.detail_image) ImageView image;
    @Bind(R.id.view_flipper) ViewFlipper viewFlipper;

    private static final String VIEWSTATE0 = "0";
    private static final String VIEWSTATE1 = "1";
    private static final String VIEWSTATE2 = "2";

    Result retainedResult;



    // using a retained fragment to hold the ViewState
    // and the adapter for the RecyclerView


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(false);

    }

    @Override
    public boolean isRetainingInstance() {
        return super.isRetainingInstance();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    //Injecting our dagger dependencies
    @Override
    protected void injectDependencies() {
        ((MovieApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.initalize();
        if (((ActivityMain) getActivity()).getCurrentResult()!=null) {
            presenter.presentDetails(((ActivityMain) getActivity()).getCurrentResult());
            presenter.requestMovieMedia();
        }



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
    public void setData(Result result) {
            ((ActivityMain) getActivity()).getSupportActionBar().setTitle("Movie Details");
            linearLayout.setVisibility(View.VISIBLE);
            title.setText(result.getTitle());
            plot.setText(result.getOverview());
            userRating.setText("" + String.valueOf(result.getVoteAverage()) + "/10");
            releaseDate.setText(result.getReleaseDate().substring(0, 4));

            String imageUrl = "http://image.tmdb.org/t/p/w185/" + result.getPosterPath();
            Glide.with(this)
                    .load(imageUrl)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.placeholderdrawable)
                    .fitCenter()
                    .into(image);
    }

    @Override
    public void setDataMedia(List<ResultMedia> resultMedia) {
        for (ResultMedia resultMediaTemp : resultMedia){
            String linkText = resultMediaTemp.getName();
            String key = resultMediaTemp.getKey();

            TextView linkTextView = new TextView(movieApplication);
            linkTextView.setText(linkText);
            Log.e("setMedia", linkText);
            linkTextView.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            detailMediaLinearLayout.addView(linkTextView);
            linkTextView.setTextColor(Color.blue());
            //(getResources().getColor(R.color.abc_primary_text_material_light));
        }
    }

    @Override
    public void setDataReview(List<ResultReview> resultsReview) {

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
    public void showLoading() {
        getViewState().setStateShowLoading();
        viewFlipper.setDisplayedChild(VIEWFLIPPER_LOADING);
    }

    @Override
    public void showError(Throwable e) {
        viewFlipper.setDisplayedChild(VIEWFLIPPER_RESULTS);
        Toast.makeText(movieApplication, "error: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
    }


}
