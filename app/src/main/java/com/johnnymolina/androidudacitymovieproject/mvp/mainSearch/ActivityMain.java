package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.johnnymolina.androidudacitymovieproject.AppComponent;
import com.johnnymolina.androidudacitymovieproject.MovieApplication;
import com.johnnymolina.androidudacitymovieproject.api.model.Result;
import com.johnnymolina.androidudacitymovieproject.eventBus.RxBus;
import com.johnnymolina.androidudacitymovieproject.mvp.detailsView.DetailsFrag;
import com.johnnymolina.androidudacityspotifyproject.R;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindActivity;
import static rx.android.app.AppObservable.bindSupportFragment;


public class ActivityMain extends AppCompatActivity {
    @Inject MovieApplication movieApplication;
    @Inject RxBus _rxBus;
    private CompositeSubscription _subscriptions;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private SearchFragment retainedFragment;
    private Result result;
    private static final String VIEWSTATE0 = "0";
    private static final String VIEWSTATE1 = "1";
    private static final String VIEWSTATE2 = "2";


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
            if (savedInstanceState != null) { // saved instance state, fragment may exist
                // look up the instance that already exists by tag
                if(getResources().getBoolean(R.bool.dual_pane)) {

                    if (getSupportFragmentManager().findFragmentByTag(VIEWSTATE2) != null) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, getSupportFragmentManager().findFragmentByTag(VIEWSTATE1))
                                .replace(R.id.fragmentContainer1, getSupportFragmentManager().findFragmentByTag(VIEWSTATE2))
                                .commit();
                    } else {
                        if (getSupportFragmentManager().findFragmentByTag(VIEWSTATE1) instanceof DetailsFrag){
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, getSupportFragmentManager().findFragmentByTag(VIEWSTATE1))
                                    .commit();
                        }else {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, getSupportFragmentManager().findFragmentByTag(VIEWSTATE1))
                                    .replace(R.id.fragmentContainer1, new DetailsFrag(), VIEWSTATE2)
                                    .commit();
                        }
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
                                if (event instanceof Result) {
                                    initDetailFrag((Result) event);
                                }

                            }
                        }));
    }

    private void initDetailFrag(Result result) {
        this.result = result;
        Log.e("RX", result.getTitle().toString());

        if(getResources().getBoolean(R.bool.dual_pane)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer1, new DetailsFrag(), VIEWSTATE2)
                    .addToBackStack(VIEWSTATE1)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new DetailsFrag(), VIEWSTATE1)
                    .addToBackStack(VIEWSTATE1)
                    .commit();
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
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            toolbar.showOverflowMenu();
        }
    }

    public void openDrawr(){
        mDrawer.openDrawer(GravityCompat.START);
    }

    public Result getCurrentResult(){
        return result;
    }

}
