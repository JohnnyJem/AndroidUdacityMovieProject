package com.johnnymolina.popularmovies.mvp.mainSearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.johnnymolina.popularmovies.AppComponent;
import com.johnnymolina.popularmovies.MovieApplication;
import com.johnnymolina.popularmovies.R;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmMovieInfo;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.popularmovies.eventBus.RxBus;
import com.johnnymolina.popularmovies.mvp.detailsView.DetailsFrag;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.ActivityLifecycleProvider;
import com.trello.rxlifecycle.RxLifecycle;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

//TODO: choose whether or not to extend Mosby Activity.
public class ActivityMain extends AppCompatActivity implements ActivityLifecycleProvider {
    @Inject MovieApplication movieApplication;
    @Inject RxBus rxBus;
    private CompositeSubscription subscriptions;
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

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
        lifecycleSubject.onNext(ActivityEvent.CREATE);

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
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
        getComponent().inject(this);
        subscriptions = new CompositeSubscription();
        subscriptions
                .add(rxBus.toObserverable()//
                        .compose(bindToLifecycle())
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
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        subscriptions.unsubscribe();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
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

    @NonNull
    @Override
    public Observable<ActivityEvent> lifecycle() {
        return  lifecycleSubject.asObservable();
    }

    @NonNull
    @Override
    public <T> Observable.Transformer<T, T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @Override
    public <T> Observable.Transformer<T, T> bindToLifecycle() {
        return RxLifecycle.bindActivity(lifecycleSubject);
    }
}
