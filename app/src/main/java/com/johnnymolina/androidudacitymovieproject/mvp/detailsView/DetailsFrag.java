package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;


import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.widget.ImageView;
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
import com.johnnymolina.androidudacitymovieproject.mvp.mainSearch.ActivityMain;
import com.johnnymolina.androidudacityspotifyproject.R;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFrag extends MvpViewStateFragment<DetailsFragView,DetailsFragPresenter> implements DetailsFragView {
    public static final int VIEWFLIPPER_RESULTS = 0;
    public static final int VIEWFLIPPER_LOADING = 1;

    @Inject
    MovieService movieService;
    @Inject
    MovieApplication movieApplication;

    @Bind(R.id.detail_title) TextView title;
    @Bind(R.id.detail_plot) TextView plot;
    @Bind(R.id.detail_user_rating) TextView userRating;
    @Bind(R.id.detail_release_date) TextView releaseDate;
    @Bind(R.id.detail_image) ImageView image;
    @Bind(R.id.view_flipper) ViewFlipper viewFlipper;

    Result result;
    AppComponent appComponent;
    // using a retained fragment to hold the ViewState
    // and the adapter for the RecyclerView

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

        result= ((ActivityMain)getActivity()).getCurrentResult();
        presenter.presentDetails(result);
    }

    //Injecting our dagger dependencies
    @Override
    protected void injectDependencies() {
        MovieApplication movieApplication = (MovieApplication) getActivity().getApplication();
        ((MovieApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public DetailsFragPresenter createPresenter() {
        return new DetailsFragPresenter();
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
        title.setText(result.getTitle());
        plot.setText(result.getOverview());
        userRating.setText(String.valueOf(result.getVoteAverage()));
        releaseDate.setText(result.getReleaseDate());

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
    public DetailsFragViewState getViewState() {
        return (DetailsFragViewState) super.getViewState();
    }

    @Override
    public void showSearchList() {
        getViewState().setStateShowSearchList();
        viewFlipper.setDisplayedChild(VIEWFLIPPER_RESULTS);
        setHasOptionsMenu(true);
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
