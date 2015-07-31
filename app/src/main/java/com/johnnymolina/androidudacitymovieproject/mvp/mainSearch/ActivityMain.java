package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.johnnymolina.androidudacitymovieproject.AppComponent;
import com.johnnymolina.androidudacitymovieproject.MovieApplication;
import com.johnnymolina.androidudacitymovieproject.api.model.Result;
import com.johnnymolina.androidudacitymovieproject.mvp.detailsView.DetailsFrag;
import com.johnnymolina.androidudacityspotifyproject.R;

import javax.inject.Inject;



public class ActivityMain extends AppCompatActivity {
    @Inject MovieApplication movieApplication;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private SearchFragment retainedFragment;
    private Result result;

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
                retainedFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, retainedFragment)
                        .commit();
            } else if (savedInstanceState == null) {
                //If no fragment present then create a new one and place it in our main UI.
                android.support.v4.app.Fragment f = new SearchFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, f)
                        .commit();
            }



    }

    public AppComponent getComponent(){
        MovieApplication movieApplication = (MovieApplication) getApplication();
         return movieApplication.getAppComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        SearchFragment f =(SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        String mostPopular = "most_popular";
        String highestRated = "highest_rated";

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.most_popular:
                f.overflowMenuTasks(mostPopular);
                return true;
            case R.id.highest_rated:
                f.overflowMenuTasks(highestRated);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void setCurrentResult(Result result){
        result = this.result;
        android.support.v4.app.Fragment f = new DetailsFrag();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, f)
                .commit();
    }

    public Result getCurrentResult(){
        return result;
    }

}