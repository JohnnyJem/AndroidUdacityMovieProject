package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.johnnymolina.androidudacitymovieproject.AppComponent;
import com.johnnymolina.androidudacitymovieproject.MovieApplication;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.androidudacitymovieproject.eventBus.RxBus;
import com.johnnymolina.androidudacitymovieproject.mvp.detailsView.DetailsFrag;
import com.johnnymolina.androidudacityspotifyproject.R;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindActivity;


public class ActivityMain extends AppCompatActivity {
    @Inject MovieApplication movieApplication;
    @Inject RxBus rxBus;
    private CompositeSubscription subscriptions;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private SearchFragment retainedFragment;
    private RealmMovieInfo movieInfo;
    private static final String VIEWSTATE0 = "0";
    private static final String VIEWSTATE1 = "1";
    private static final String VIEWSTATE2 = "2";
    private int movieID;

    FrameLayout fragmentContainer1;
    FrameLayout fragmentContainer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);

       fragmentContainer1 = (FrameLayout) findViewById(R.id.fragmentContainer1);
       fragmentContainer2 = (FrameLayout) findViewById(R.id.fragmentContainer2);

        if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag

            if(getResources().getBoolean(R.bool.dual_pane)) {
                fragmentContainer2.setVisibility(View.VISIBLE);
                if (getSupportFragmentManager().findFragmentByTag(VIEWSTATE2) != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer1, getSupportFragmentManager().findFragmentByTag(VIEWSTATE1))
                            .replace(R.id.fragmentContainer2, getSupportFragmentManager().findFragmentByTag(VIEWSTATE2))
                            .commit();
                } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer1, getSupportFragmentManager().findFragmentByTag(VIEWSTATE1))
                                .commit();
                }
            }else {
                if (getSupportFragmentManager().findFragmentByTag(VIEWSTATE2) != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer1, getSupportFragmentManager().findFragmentByTag(VIEWSTATE1))
                        .replace(R.id.fragmentContainer2, getSupportFragmentManager().findFragmentByTag(VIEWSTATE2))
                        .commit();
                    fragmentContainer1.setVisibility(View.GONE);
                    fragmentContainer2.setVisibility(View.VISIBLE);
                }

            }

        } else if (savedInstanceState == null) {
            //If no fragment present then create a new one and place it in our main UI.
            //Check if we are in dual pane mode or not.
            Log.d("FRAG", "Creating new Frag");
            if(getResources().getBoolean(R.bool.dual_pane)){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer1, new SearchFragment(), VIEWSTATE1)
                        .commit();
            }else{
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer1, new SearchFragment(),VIEWSTATE1)
                        .commit();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
       getComponent().inject(this);
        subscriptions = new CompositeSubscription();
        subscriptions
                .add(bindActivity(this, rxBus.toObserverable())//
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object event) {
                                if (event instanceof RealmMovieInfo) {
                                    movieInfo = (RealmMovieInfo) event;
                                    movieID = movieInfo.getId();
                                    initDetailFrag();
                                }else if (event instanceof RealmReturnedMovie){
                                    movieID = ((RealmReturnedMovie) event).getId();
                                    initDetailFrag();
                                }
                            }
                        }));
    }

    private void initDetailFrag() {
        if(getResources().getBoolean(R.bool.dual_pane)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer2, new DetailsFrag(), VIEWSTATE2)
                    .addToBackStack(VIEWSTATE2)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer2, new DetailsFrag(), VIEWSTATE2)
                    .addToBackStack(VIEWSTATE2)
                    .commit();
            fragmentContainer1.setVisibility(View.GONE);
            fragmentContainer2.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        subscriptions.unsubscribe();
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ) {
            getSupportFragmentManager().popBackStackImmediate();
            if (!(getResources().getBoolean(R.bool.dual_pane)) ){
                fragmentContainer1.setVisibility(View.VISIBLE);
            }
        }else {
            super.onBackPressed();
        }
    }

    public void openDrawr(){
        mDrawer.openDrawer(GravityCompat.START);
    }

    public RealmMovieInfo getCurrentResult(){
        return movieInfo;
    }

    public int getMovieID(){
        return  movieID;
    }

}
