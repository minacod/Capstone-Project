package com.example.android.tellme.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.tellme.BuildConfig;
import com.example.android.tellme.R;
import com.example.android.tellme.databinding.ActivitySourcesBinding;
import com.example.android.tellme.model.Source;
import com.example.android.tellme.utils.JsonUtils;
import com.example.android.tellme.utils.NetworkUtils;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SourcesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Source>>
        ,SourcesListAdapter.OnItemClicked
        ,NavigationView.OnNavigationItemSelectedListener{

    private static final int SOURCES_LOADER_ID = 2000;
    private static final String API_KEY = BuildConfig.API_KEY;
    private String mCategory = "general";
    private ActivitySourcesBinding mBinding;
    private ArrayList<Source> mSources;
    private SourcesFragment mSourcesFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private Parcelable mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(savedInstanceState!=null){
            mSources=savedInstanceState.getParcelableArrayList(getString(R.string.source));
            mState =savedInstanceState.getParcelable(getString(R.string.state));
        }


        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_sources);
        Intent intentOpenedThis= getIntent();
        String tmp=intentOpenedThis.getStringExtra(getString(R.string.category));
        if(tmp!=null)
            mCategory=tmp;

        mActivityTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                R.string.navigation_drawer_close, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.nav_categories);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mBinding.navigation.setNavigationItemSelectedListener(this);
        mBinding.drawerLayout.addDrawerListener(mDrawerToggle);
        if(NetworkUtils.isConnected(SourcesActivity.this)){
            loadSources();
        }
        else {
            Toast.makeText(this,R.string.offline,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mSources!=null)
        outState.putParcelableArrayList(getString(R.string.source),mSources);
        if(mSourcesFragment!=null)
        outState.putParcelable(getString(R.string.state),mSourcesFragment.getState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void loadSources(){
        Bundle sourcesLoaderBundle = new Bundle();
        LoaderManager sourcesLoaderManager = getSupportLoaderManager();
        Loader sourcesLoader = sourcesLoaderManager.getLoader(SOURCES_LOADER_ID);
        if (sourcesLoader == null)
            sourcesLoaderManager.initLoader(SOURCES_LOADER_ID, sourcesLoaderBundle, this);
        else
            sourcesLoaderManager.restartLoader(SOURCES_LOADER_ID, sourcesLoaderBundle, this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ArrayList<Source>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Source>>(this) {
            @Override
            protected void onStartLoading() {
                if(mSources==null) {
                    forceLoad();
                    mBinding.pbLoading.setVisibility(View.VISIBLE);
                }
                else
                    deliverResult(mSources);
            }

            @Override
            public ArrayList<Source> loadInBackground() {
                URL url = NetworkUtils.buildSourcesUrl(API_KEY,mCategory);
                FirebaseCrash.log("url "+url.toString());
                String source="";
                if(NetworkUtils.isConnected(SourcesActivity.this)) {
                    try {
                        source = NetworkUtils.getResponseFromHttpUrl(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FirebaseCrash.log("Source "+ source);
                    ArrayList<Source> sourceArrayList = new ArrayList<>();
                    try {
                        sourceArrayList = JsonUtils.getSourcesFormJson(source);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return sourceArrayList;
                }
                else
                    return null;
            }

            @Override
            public void deliverResult(ArrayList<Source> data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Source>> loader, ArrayList<Source> data) {
        mBinding.pbLoading.setVisibility(View.GONE);
        mSources=data;
        final int WHAT = 1;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == WHAT) changeFragment();
            }
        };
        handler.sendEmptyMessage(WHAT);
    }

    void changeFragment(){
        if(mSourcesFragment!=null){
            mSourcesFragment.setClicked(SourcesActivity.this);
            mSourcesFragment.setData(mSources);
            mSourcesFragment.setState(mState);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_sources_fragment, mSourcesFragment)
                    .commit();
        }
        else {

            mSourcesFragment =new SourcesFragment();
            mSourcesFragment.setClicked(SourcesActivity.this);
            mSourcesFragment.setData(mSources);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fl_sources_fragment, mSourcesFragment)
                    .commit();
        }
    }

    @Override
    public void onItemClickListener(int position) {
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra(getString(R.string.source),mSources.get(position).getName());
        i.putExtra(getString(R.string.source_id),mSources.get(position).getId());
        startActivity(i);
        finish();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Source>> loader) {

    }


    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (NetworkUtils.isConnected(this)) {
            if (id == R.id.nav_general) {
                Intent i = new Intent(this, SourcesActivity.class);
                i.putExtra(getString(R.string.category), getString(R.string.q_general));
                startActivity(i);
                finish();
            } else if (id == R.id.nav_business) {
                Intent i = new Intent(this, SourcesActivity.class);
                i.putExtra(getString(R.string.category), getString(R.string.q_business));
                startActivity(i);
                finish();
            } else if (id == R.id.nav_entertainment) {
                Intent i = new Intent(this, SourcesActivity.class);
                i.putExtra(getString(R.string.category), getString(R.string.q_entertainment));
                startActivity(i);
                finish();
            } else if (id == R.id.nav_sports) {
                Intent i = new Intent(this, SourcesActivity.class);
                i.putExtra(getString(R.string.category), getString(R.string.q_sports));
                startActivity(i);
                finish();
            } else if (id == R.id.nav_technology) {
                Intent i = new Intent(this, SourcesActivity.class);
                i.putExtra(getString(R.string.category), getString(R.string.q_technology));
                startActivity(i);
                finish();
            } else if (id == R.id.nav_science) {
                Intent i = new Intent(this, SourcesActivity.class);
                i.putExtra(getString(R.string.category), getString(R.string.q_science));
                startActivity(i);
                finish();
            } else if (id == R.id.nav_health) {
                Intent i = new Intent(this, SourcesActivity.class);
                i.putExtra(getString(R.string.category), getString(R.string.q_health));
                startActivity(i);
                finish();
            }
        }else {
            Toast.makeText(this,R.string.offline,Toast.LENGTH_LONG).show();
        }
        if (id == R.id.nav_saved) {
            Intent i = new Intent(this,CashedActivity.class);
            startActivity(i);
            finish();
        }else if (id == R.id.nav_home) {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
