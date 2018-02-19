package com.example.android.tellme.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.android.tellme.R;
import com.example.android.tellme.data.ArticlesContentProvider;
import com.example.android.tellme.data.ArticlesContract;
import com.example.android.tellme.databinding.ActivityArticleDetailsBinding;
import com.example.android.tellme.model.Article;
import com.example.android.tellme.utils.GlideApp;
import com.example.android.tellme.utils.NetworkUtils;
import com.example.android.tellme.widget.WidgetServices;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.crash.FirebaseCrash;

public class ArticleDetailsActivity extends AppCompatActivity {

    Article mData;
    ActivityArticleDetailsBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Intent intentOpenedThis = getIntent();
        mData = intentOpenedThis.getParcelableExtra(getString(R.string.article));
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_details);
        if(savedInstanceState!=null){
            mBinding.svDetails.scrollTo(
                    savedInstanceState.getInt(getString(R.string.s_x)),
                    savedInstanceState.getInt(getString(R.string.s_y))
            );
        }
        updateUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.s_x),mBinding.svDetails.getScrollX());
        outState.putInt(getString(R.string.s_y),mBinding.svDetails.getScrollY());
        super.onSaveInstanceState(outState);
    }

    public void updateUi(){
        mBinding.tvArticleTitle.setText(mData.getTitle());
        String tmp = (mData.getAuthor().equals("null"))?getString(R.string.no_name):mData.getAuthor();
        mBinding.tvArticleAuthor.setText(tmp);
        mBinding.tvSource.setText(mData.getSourceName());
        mBinding.tvDate.setText(mData.getDate().substring(0,10));
        mBinding.tvArticleBody.setText(mData.getDescription());
        if(NetworkUtils.isConnected(this)){
            mBinding.btnOpenInBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri webpage = Uri.parse(mData.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(intent);
                }
            });
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mBinding.adTestAd.loadAd(adRequest);
        }
        else
            mBinding.btnOpenInBrowser.setVisibility(View.GONE);

        GlideApp.with(mBinding.getRoot().getContext())
                .load(mData.getImageUrl())
                .placeholder(R.drawable.ic_tell_me)
                .error(R.drawable.ic_tell_me)
                .into(mBinding.ivArticlePlaceholder);
    }

    boolean isSaved(){
        boolean isSaved=false;
        Uri uri = ArticlesContentProvider.Articles.ARTICLES;
        Cursor mCursor = getContentResolver().query(uri, null,
                ArticlesContract.ArticlesEntry.URL + "=?", new String[]{mData.getUrl()}, null);
        if (mCursor != null) {
            isSaved = mCursor.getCount() != 0;
            mCursor.close();
        }
        return isSaved;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =new MenuInflater(this);
        inflater.inflate(R.menu.details_activity_menu,menu);
        if(isSaved()){
            MenuItem item = menu.getItem(0);
            item.setTitle(R.string.dam_delete);
            item.setIcon(R.drawable.ic_delete_black_48px);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if (id==R.id.dam_add_to_database) {
            Uri uri = ArticlesContentProvider.Articles.ARTICLES;
            if(!isSaved()){
                addToDatabase(uri);
                item.setTitle(getString(R.string.dam_delete));
                item.setIcon(R.drawable.ic_delete_black_48px);

            }
            else {
                deleteFromDatabase(uri);
                item.setTitle(getString(R.string.dam_save));
                item.setIcon(R.drawable.ic_save);
            }
            return true;
        }
        else return false;
    }

    private void deleteFromDatabase(Uri uri) {
        getContentResolver().delete(uri,ArticlesContract.ArticlesEntry.URL+ "=?",new String[]{mData.getUrl()});
        WidgetServices.startActionUpdateWidget(this);
    }
    private void addToDatabase(Uri uri) {
        ContentValues cv = new ContentValues();
        cv.put(ArticlesContract.ArticlesEntry.ARTICLE_TITLE,mData.getTitle());
        cv.put(ArticlesContract.ArticlesEntry.ARTICLE_AUTHOR, mData.getAuthor());
        cv.put(ArticlesContract.ArticlesEntry.DESCRIPTION, mData.getDescription());
        cv.put(ArticlesContract.ArticlesEntry.IMAGE_URL,mData.getImageUrl());
        cv.put(ArticlesContract.ArticlesEntry.URL,mData.getUrl());
        cv.put(ArticlesContract.ArticlesEntry.SOURCE_NAME,mData.getSourceName());
        cv.put(ArticlesContract.ArticlesEntry.DATE,mData.getDate());
        Uri i = getContentResolver().insert(uri,cv);
        FirebaseCrash.log("uri   "+i.toString());
        WidgetServices.startActionUpdateWidget(this);
    }

}
