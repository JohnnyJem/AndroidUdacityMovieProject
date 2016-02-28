package com.johnnymolina.popularmovies.mvp.mainSearch;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.dd.realmbrowser.RealmBrowser;
import com.dd.realmbrowser.RealmFilesActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.RestoreableViewState;
import com.johnnymolina.popularmovies.adapters.RealmMovieAdapter;
import com.johnnymolina.popularmovies.adapters.SearchListAdapter;
import com.johnnymolina.popularmovies.AppComponent;
import com.johnnymolina.popularmovies.MovieApplication;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmMovieInfo;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmMovieMedia;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmMovieReview;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.popularmovies.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.popularmovies.eventBus.RxBus;
import com.johnnymolina.popularmovies.extended.RecyclerItemClickListener;
import com.johnnymolina.popularmovies.R;
import com.johnnymolina.popularmovies.api.MovieService;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import io.realm.Realm;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public class SearchFragment extends MvpViewStateFragment<SearchListView,SearchListPresenter> implements SearchListView {
    public static final int VIEWFLIPPER_RESULTS = 0;
    public static final int VIEWFLIPPER_LOADING = 1;

    @Inject MovieService movieService;
    @Inject MovieApplication movieApplication;
    @Inject RxBus rxBus;
    @Inject Realm realm;

    @Bind(R.id.search_box) EditText searchBox;
    @Bind(R.id.rv_movie_search_images) RecyclerView recyclerView;
    @Bind(R.id.view_flipper) ViewFlipper viewFlipper;

    AppComponent appComponent;
    SearchListAdapter searchListAdapter;
    RealmMovieAdapter realmMovieAdapter;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RealmBrowser.getInstance().addRealmModel(RealmReturnedMovie.class,RealmMovieInfo.class, RealmMovieMedia.class,
                RealmMovieReview.class);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

           if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
               recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
           }
           else{
               recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
           }

            recyclerView.setHasFixedSize(true);
            searchListAdapter = searchListAdapter == null ? new SearchListAdapter() : searchListAdapter;
            realmMovieAdapter = realmMovieAdapter == null ? new RealmMovieAdapter() : realmMovieAdapter;
           // recyclerView.setAdapter(searchListAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (rxBus.hasObservers()) {

                        //sending either a RealmMovieInfo object or RealmReturnedMovie object through rxBus depending on the
                        //type of adapter that is currently being viewed.'
                        //must move this into a method in this frag's presenter. This current implementation violates MVP. This should be a "dumb" view and not manipulate data.
                        if (searchListAdapter != null && searchListAdapter.getMovies().get(position) instanceof MovieInfo && recyclerView.getAdapter() == searchListAdapter) {
                            MovieInfo movieInfo = searchListAdapter.getMovies().get(position);
                            RealmMovieInfo realmMovieInfo = new RealmMovieInfo();
                            realmMovieInfo.setId(movieInfo.getId());
                            realmMovieInfo.setTitle(movieInfo.getTitle());
                            realmMovieInfo.setPosterPath(movieInfo.getPosterPath());
                            realmMovieInfo.setOverview(movieInfo.getOverview());
                            realmMovieInfo.setReleaseDate(movieInfo.getReleaseDate());
                            realmMovieInfo.setVoteAverage(movieInfo.getVoteAverage());
                            rxBus.send(realmMovieInfo);
                            Log.e("RX", "pushed RealmReturnedMovieInfoObject "+movieInfo.getId() );
                        } else if (realmMovieAdapter != null && realmMovieAdapter.getRealmMovies().get(position) instanceof RealmReturnedMovie && recyclerView.getAdapter() == realmMovieAdapter) {
                            RealmReturnedMovie realmReturnedMovie = realmMovieAdapter.getRealmMovies().get(position);
                            rxBus.send(realmReturnedMovie);
                            Log.e("RX", "pushed RealmReturnedMovieObject " + realmReturnedMovie.getId());
                        }
                    } else {
                        Log.e("RX", "Does not hasObservers");
                    }
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));


    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setMovies(realm);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    //Injecting our dagger dependencies
    @Override protected void injectDependencies() {
        ((MovieApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public SearchListPresenter createPresenter() {
        return new SearchListPresenter(movieService);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_search;
    }

    @Override
    public RestoreableViewState createViewState() {
        return new SearchListViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        showSearchList();
    }

    @Override
    public void setData(List<MovieInfo> list) {
        recyclerView.setAdapter(searchListAdapter);
        searchListAdapter.setMovies(list);
        searchListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRealmData(List<RealmReturnedMovie> realmList) {
        recyclerView.setAdapter(realmMovieAdapter);
        realmMovieAdapter.setRealmMovies(realmList);
        realmMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public SearchListViewState getViewState() {
        return (SearchListViewState) super.getViewState();
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
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
        viewFlipper.setDisplayedChild(VIEWFLIPPER_LOADING);
    }

    @Override
    public void showError(Throwable e) {
        viewFlipper.setDisplayedChild(VIEWFLIPPER_RESULTS);
        Toast.makeText(movieApplication, "error: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String mostPopular = "most_popular";
        String highestRated = "highest_rated";
        String favorites = "favorites";
        String realmBrowser = "realm_browser";

        switch (item.getItemId()) {
            case android.R.id.home:
                ((ActivityMain) getActivity()).openDrawr();
                return true;
            case R.id.most_popular:
                this.overflowMenuTasks(mostPopular);
                return true;
            case R.id.highest_rated:
                this.overflowMenuTasks(highestRated);
                return true;
            case R.id.favorites_filter:
                this.overflowMenuTasks(favorites);
                return true;
            case R.id.realm_browser:
                this.overflowMenuTasks(realmBrowser);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void overflowMenuTasks(String itemName){
        switch (itemName) {
            case "most_popular":
                presenter.searchForMovies("popularity.desc");
                return;
            case "highest_rated":
                presenter.searchForMovies("vote_count.desc");
                return;
            case "favorites":
                presenter.searchForRealmMovies(realm);
                return;
            case "realm_browser":
                RealmFilesActivity.start(getActivity());
                return;
            default:
                return;
        }
    }



}
