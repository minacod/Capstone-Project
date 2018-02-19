package com.example.android.tellme.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.databinding.DataBindingUtil;
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
import com.example.android.tellme.databinding.ActivityMainBinding;
import com.example.android.tellme.model.Article;
import com.example.android.tellme.utils.JsonUtils;
import com.example.android.tellme.utils.NetworkUtils;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Article>>
        ,ArticlesListAdapter.OnItemClicked
        ,NavigationView.OnNavigationItemSelectedListener{


    private ArrayList<Article> mArticles;
    private static final int ARTICLES_LOADER_ID = 1000;
    private static final String API_KEY = BuildConfig.API_KEY;
    private String mSource;
    private ArticlesFragment mArticlesFragment;
    private ActivityMainBinding mBinding;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private Boolean mIsScrollLoad;
    private Boolean mScrollLock;
    private int mPageNum;
    private SharedPreferences mDefault;
    private Parcelable mState;
    private boolean mIsSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(savedInstanceState!=null){
            mArticles=savedInstanceState.getParcelableArrayList(getString(R.string.article));
            mState =savedInstanceState.getParcelable(getString(R.string.state));
        }
        mScrollLock = true;
        Intent intentOpenedThis= getIntent();
        String tmp=intentOpenedThis.getStringExtra(getString(R.string.source_id));
        mDefault = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        if(tmp!=null) {
            mSource = tmp;
            mActivityTitle = intentOpenedThis.getStringExtra(getString(R.string.source));
            mIsSearch=intentOpenedThis.getBooleanExtra(getString(R.string.is_s),false);
            getSupportActionBar().setTitle(mActivityTitle);
        }
        else {
            mSource = mDefault.getString(getString(R.string.sp_s_name),"");
            mActivityTitle=getString(R.string.home);
            getSupportActionBar().setTitle(mActivityTitle);
        }

        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_main);


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
        mBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkUtils.isConnected(MainActivity.this)){
                    if(!mBinding.etSearch.getText().toString().equals("")){
                        Intent i = new Intent(MainActivity.this,MainActivity.class);
                        i.putExtra(MainActivity.this.getString(R.string.source_id),mBinding.etSearch.getText().toString());
                        i.putExtra(MainActivity.this.getString(R.string.source),"Search");
                        i.putExtra(getString(R.string.is_s),true);
                        startActivity(i);
                    }
                    else {

                        Toast.makeText(MainActivity.this, R.string.no_search,Toast.LENGTH_LONG).show();
                    }
                }
                else
                    Toast.makeText(MainActivity.this,R.string.offline,Toast.LENGTH_LONG).show();
            }
        });

        mIsScrollLoad=false;

        if(NetworkUtils.isConnected(MainActivity.this)){
            if(mSource==null||mSource.equals("")){
                mBinding.tvMsg.setText(R.string.no_def);
                mBinding.tvMsg.setVisibility(View.VISIBLE);
            }else {
                mPageNum=1;
                loadArticle();
            }
        }
        else {
            mBinding.tvMsg.setText(R.string.offline);
            mBinding.tvMsg.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mArticles!=null)
        outState.putParcelableArrayList(getString(R.string.article),mArticles);
        if(mArticlesFragment!=null)
        outState.putParcelable(getString(R.string.state),mArticlesFragment.getState());
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

    private void loadArticle() {
        Bundle articlesLoaderBundle = new Bundle();
        LoaderManager ArticlesLoaderManager = getSupportLoaderManager();
        Loader articlesLoader = ArticlesLoaderManager.getLoader(ARTICLES_LOADER_ID);
        if (articlesLoader == null)
            ArticlesLoaderManager.initLoader(ARTICLES_LOADER_ID, articlesLoaderBundle, this);
        else
            ArticlesLoaderManager.restartLoader(ARTICLES_LOADER_ID, articlesLoaderBundle, this);
    }

    @Override
    protected void onDestroy() {
        FirebaseCrash.log("Main activity onDestroy()");
        super.onDestroy();
    }

    public void loadMoreArticle() {
        if(mScrollLock) {
            mIsScrollLoad = true;
            Bundle articlesLoaderBundle = new Bundle();
            getSupportLoaderManager().restartLoader(ARTICLES_LOADER_ID, articlesLoaderBundle, this);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Article>>(this) {
            @Override
            protected void onStartLoading() {
                if(mIsScrollLoad){
                    mIsScrollLoad = false;
                    forceLoad();
                }
                else if(mArticles==null) {
                    mBinding.tvMsg.setVisibility(View.GONE);
                    mBinding.pbLoading.setVisibility(View.VISIBLE);
                    forceLoad();
                }
                else
                    deliverResult(mArticles);

            }

            @Override
            public ArrayList<Article> loadInBackground() {
                URL url;
                if (mIsSearch) {
                    url = NetworkUtils.buildEverythingByQueryUrl(API_KEY, mSource, 1, mPageNum);
                } else
                    url = NetworkUtils.buildEverythingBySourceUrl(API_KEY, mSource, 2, mPageNum);
                mPageNum++;
                FirebaseCrash.log("url "+url.toString());
                String article = "";
                try {
                    article = NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int i = (mArticles == null) ? 0 : mArticles.size();
                ArrayList<Article> articleArrayList = new ArrayList<>();
                try {
                    articleArrayList = JsonUtils.getArticlesFormJson(article, mArticles);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mScrollLock = articleArrayList != null && i < articleArrayList.size();

                return articleArrayList;
            }

            @Override
            public void deliverResult(ArrayList<Article> data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
        mBinding.pbLoading.setVisibility(View.GONE);
        mBinding.flArticleFragment.setVisibility(View.VISIBLE);
        mArticles=data;
        final int WHAT = 1;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == WHAT) changeFragment();
            }
        };
        handler.sendEmptyMessage(WHAT);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Article>> loader) {

    }
    void changeFragment(){
        if(mArticlesFragment!=null){
            mArticlesFragment.setClicked(MainActivity.this);
            mArticlesFragment.setData(mArticles);
            mArticlesFragment.setState(mState);
            mArticlesFragment.notifyRecycler();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_article_fragment, mArticlesFragment)
                    .commit();
        }
        else {

            mArticlesFragment =new ArticlesFragment();
            mArticlesFragment.setClicked(MainActivity.this);
            mArticlesFragment.setData(mArticles);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fl_article_fragment, mArticlesFragment)
                    .commit();
        }
    }

    @Override
    public void onItemClickListener(int position) {
        Intent i =new Intent(this,ArticleDetailsActivity.class);
        i.putExtra(getString(R.string.article),mArticles.get(position));
        startActivity(i);
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
        MenuInflater inflater =new MenuInflater(this);
        inflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.mam_make_it_default)
            mDefault.edit().putString(getString(R.string.sp_s_name),mSource).apply();

        return mDrawerToggle.onOptionsItemSelected(item);
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
