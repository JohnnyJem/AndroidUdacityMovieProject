package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

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

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.RestoreableViewState;
import com.johnnymolina.androidudacitymovieproject.adapters.SearchListAdapter;
import com.johnnymolina.androidudacitymovieproject.AppComponent;
import com.johnnymolina.androidudacitymovieproject.MovieApplication;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.androidudacitymovieproject.eventBus.RxBus;
import com.johnnymolina.androidudacitymovieproject.extended.RecyclerItemClickListener;
import com.johnnymolina.androidudacityspotifyproject.R;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public class SearchFragment extends MvpViewStateFragment<SearchListView,SearchListPresenter> implements SearchListView {
    public static final int VIEWFLIPPER_RESULTS = 0;
    public static final int VIEWFLIPPER_LOADING = 1;

    @Inject MovieService movieService;
    @Inject MovieApplication movieApplication;
    @Inject RxBus _rxBus;

    @Bind(R.id.search_box) EditText searchBox;
    @Bind(R.id.rv_movie_search_images) RecyclerView recyclerView;
    @Bind(R.id.view_flipper) ViewFlipper viewFlipper;

    AppComponent appComponent;
    SearchListAdapter searchListAdapter;
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
        ((ActivityMain)getActivity()).getSupportActionBar().setTitle("Movie Details");
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
            recyclerView.setAdapter(searchListAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (_rxBus.hasObservers()) {
                        Log.e("RX","hasObservers");
                                _rxBus.send(searchListAdapter.getMovies().get(position));
                    }else{
                        Log.e("RX","Does not hasObservers");
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
        presenter.setMovies();
        //presenter.startCompositeSubscription(movieApplication);
    }

    @Override
    public void onStop() {
        super.onStop();
        //presenter.stopCompositeSubscription();
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
        searchListAdapter.setMovies(list);
        searchListAdapter.notifyDataSetChanged();
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
            default:
                return;
        }
    }



}
