package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.johnnymolina.androidudacitymovieproject.api.model.Result;
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

    @Bind(R.id.search_box) EditText searchBox;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

           if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
               recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
           }
           else{
               recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
           }

           recyclerView.setHasFixedSize(true);
           searchListAdapter = searchListAdapter == null ? new SearchListAdapter() : searchListAdapter;
           recyclerView.setAdapter(searchListAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));


        //Set a listener to be called when an action is performed on the text view.
        //searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //@Override
            //public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               // if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //    presenter.searchForMovies(v.getText().toString());
                  //  return true;
               // }
               // return false;
           // }
       // });
    }

    //Injecting our dagger dependencies
    @Override protected void injectDependencies() {
        MovieApplication movieApplication = (MovieApplication) getActivity().getApplication();
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
    public void setData(List<Result> list) {
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

    public void overflowMenuTasks(String itemName){
        switch (itemName) {
            case "most_popular":
                presenter.searchForMovies("popularity.desc");
                return;
            case "highest_rated":
                presenter.searchForMovies("vote_count.desc");
                return;
        }
    }


}
