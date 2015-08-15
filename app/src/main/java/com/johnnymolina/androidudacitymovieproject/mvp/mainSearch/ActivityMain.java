package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.johnnymolina.androidudacitymovieproject.AppComponent;
import com.johnnymolina.androidudacitymovieproject.MovieApplication;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.androidudacitymovieproject.eventBus.RxBus;
import com.johnnymolina.androidudacitymovieproject.mvp.detailsView.DetailsFrag;
import com.johnnymolina.androidudacityspotifyproject.R;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindActivity;


public class ActivityMain extends AppCompatActivity {
    @Inject MovieApplication movieApplication;
    @Inject RxBus _rxBus;
    private CompositeSubscription _subscriptions;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private SearchFragment retainedFragment;
    private MovieInfo movieInfo;
    private static final String VIEWSTATE0 = "0";
    private static final String VIEWSTATE1 = "1";
    private static final String VIEWSTATE2 = "2";


    FrameLayout fragmentContainer;
    FrameLayout fragmentContainer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);
        fragmentContainer1 = (FrameLayout) findViewById(R.id.fragmentContainer1);
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);

       fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);
       fragmentContainer1 = (FrameLayout) findViewById(R.id.fragmentContainer1);

        if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            if(!(getResources().getBoolean(R.bool.dual_pane))) {
                fragmentContainer1.setVisibility(View.VISIBLE);
            }
            if(getResources().getBoolean(R.bool.dual_pane)) {

                if (getSupportFragmentManager().findFragmentByTag(VIEWSTATE2) != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, getSupportFragmentManager().findFragmentByTag(VIEWSTATE1))
                            .replace(R.id.fragmentContainer1, getSupportFragmentManager().findFragmentByTag(VIEWSTATE2))
                            .commit();
                } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, getSupportFragmentManager().findFragmentByTag(VIEWSTATE1))
                                .replace(R.id.fragmentContainer1, new DetailsFrag(), VIEWSTATE2)
                                .commit();
                }
            }else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, getSupportFragmentManager().findFragmentByTag(VIEWSTATE1))
                        .commit();
            }

        } else if (savedInstanceState == null) {
            //If no fragment present then create a new one and place it in our main UI.
            //Check if we are in dual pane mode or not.
            if(getResources().getBoolean(R.bool.dual_pane)){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new SearchFragment(), VIEWSTATE1)
                        .replace(R.id.fragmentContainer1, new DetailsFrag(),VIEWSTATE2)
                        .addToBackStack(VIEWSTATE2)
                        .commit();
            }else{
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new SearchFragment(),VIEWSTATE1)
                        .commit();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
       getComponent().inject(this);
        _subscriptions = new CompositeSubscription();
        _subscriptions
                .add(bindActivity(this, _rxBus.toObserverable())//
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object event) {
                                if (event instanceof MovieInfo) {
                                    initDetailFrag((MovieInfo) event);
                                }
                            }
                        }));
    }

    private void initDetailFrag(MovieInfo movieInfo) {
        this.movieInfo = movieInfo;
        if(getResources().getBoolean(R.bool.dual_pane)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer1, new DetailsFrag(), VIEWSTATE2)
                    .addToBackStack(VIEWSTATE2)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer1, new DetailsFrag(), VIEWSTATE2)
                    .addToBackStack(VIEWSTATE2)
                    .commit();
            fragmentContainer.setVisibility(View.GONE);
            fragmentContainer1.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        _subscriptions.unsubscribe();
    }


    public AppComponent getComponent(){
        MovieApplication movieApplication = (MovieApplication) getApplication();
         return movieApplication.getAppComponent();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            if (!(getResources().getBoolean(R.bool.dual_pane))){
                fragmentContainer.setVisibility(View.VISIBLE);
            }

        }else {
            super.onBackPressed();
        }
    }

    public void openDrawr(){
        mDrawer.openDrawer(GravityCompat.START);
    }

    public MovieInfo getCurrentResult(){
        return movieInfo;
    }

}
