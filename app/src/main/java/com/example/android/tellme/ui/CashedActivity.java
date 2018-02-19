package com.example.android.tellme.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.tellme.R;
import com.example.android.tellme.data.ArticlesContentProvider;
import com.example.android.tellme.data.ArticlesContract;
import com.example.android.tellme.databinding.ActivityCashedBinding;
import com.example.android.tellme.model.Article;
import com.example.android.tellme.utils.NetworkUtils;
import com.google.firebase.crash.FirebaseCrash;

public class CashedActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        CashedListAdapter.OnItemClicked,
        NavigationView.OnNavigationItemSelectedListener{

    private CashedFragment mArticlesFragment;
    private Cursor mCursor;
    private ActivityCashedBinding mBinding;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private Parcelable mState;
    private final int ARTICLES_LOADER_ID=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashed);

        if(savedInstanceState!=null){
            mState =savedInstanceState.getParcelable(getString(R.string.state));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.cashed_articles);

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_cashed);

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
        loadArticle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mArticlesFragment!=null)
        getSupportFragmentManager().beginTransaction()
                .remove(mArticlesFragment)
                .commit();
        loadArticle();
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
    protected void onSaveInstanceState(Bundle outState) {
        if(mArticlesFragment!=null)
        outState.putParcelable(getString(R.string.state),mArticlesFragment.getState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClickListener(int position) {
        Intent i =new Intent(this,ArticleDetailsActivity.class);
        mCursor.moveToPosition(position);
        Article article = new Article(
                mCursor.getString(mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.SOURCE_NAME)),
                mCursor.getString(mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.ARTICLE_AUTHOR)),
                mCursor.getString(mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.ARTICLE_TITLE)),
                mCursor.getString(mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.DESCRIPTION)),
                mCursor.getString(mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.IMAGE_URL)),
                mCursor.getString(mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.URL)),
                mCursor.getString(mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.DATE))
        );
        i.putExtra(getString(R.string.article),article);
        startActivity(i);
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

    @Override
    protected void onDestroy() {
        FirebaseCrash.log("Cashed onDestroy()");
        mCursor.close();
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                if(mCursor==null||mCursor.getCount()==0)
                    forceLoad();
                else
                    forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                Uri uri = ArticlesContentProvider.Articles.ARTICLES;
                return getContentResolver().query(uri, null,
                        null, null, null);
            }

            @Override
            public void deliverResult(@Nullable Cursor data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor=data;
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
    protected void onPause() {
        super.onPause();
        mState = mArticlesFragment.getState();
    }

    private void changeFragment() {
        if(mCursor.getCount()>0) {
            if(mArticlesFragment!=null){
                FragmentManager fragmentManager = getSupportFragmentManager();
                mArticlesFragment=new CashedFragment();
                mArticlesFragment.setClicked(CashedActivity.this);
                mArticlesFragment.setState(mState);
                mArticlesFragment.setData(mCursor);
                fragmentManager.beginTransaction()
                        .add(R.id.fl_article_fragment, mArticlesFragment)
                        .commit();
                //mArticlesFragment.notifyRecycler();
            }
            else {

                mArticlesFragment =new CashedFragment();
                mArticlesFragment.setClicked(CashedActivity.this);
                mArticlesFragment.setData(mCursor);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fl_article_fragment, mArticlesFragment)
                        .commit();
            }
            if (mState != null)
                mArticlesFragment.setState(mState);
        }
        else {
            mBinding.tvMsg.setText(R.string.nothing_to_show);
            mBinding.tvMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
