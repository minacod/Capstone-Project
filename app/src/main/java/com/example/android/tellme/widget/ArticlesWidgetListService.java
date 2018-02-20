package com.example.android.tellme.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.tellme.R;
import com.example.android.tellme.data.ArticlesContentProvider;
import com.example.android.tellme.data.ArticlesContract;
import com.example.android.tellme.model.Article;
import com.example.android.tellme.ui.ArticleDetailsActivity;
import com.example.android.tellme.ui.CashedActivity;

/**
 * Created by shafy on 16/02/2018.
 */

public class ArticlesWidgetListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ArticlesListRemoteViewsFactory(getApplicationContext());
    }
}
class  ArticlesListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    Context mContext;
    Cursor mCursor;

    public ArticlesListRemoteViewsFactory(Context applicationContext) {
        this.mContext=applicationContext;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Uri uri = ArticlesContract.ArticlesEntry.CONTENT_URI;;
        if(mCursor!=null)
            mCursor.close();
        mCursor = mContext.getContentResolver().query(uri,null,null,null,null);
    }

    @Override
    public void onDestroy() {
        if(mCursor!=null)
            mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }


    @Override
    public RemoteViews getViewAt(int i) {
        int titleIndex =mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.ARTICLE_TITLE);
        int desIndex =mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.DESCRIPTION);
        int authorIndex =mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.ARTICLE_AUTHOR);
        int sNameIndex =mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.SOURCE_NAME);
        int imageIndex =mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.IMAGE_URL);
        int urlIndex =mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.URL);
        int dateIndex =mCursor.getColumnIndex(ArticlesContract.ArticlesEntry.DATE);
        mCursor.moveToPosition(i);
        Article article = new Article(mCursor.getString(sNameIndex),
                mCursor.getString(authorIndex),
                mCursor.getString(titleIndex),
                mCursor.getString(desIndex),
                mCursor.getString(imageIndex),
                mCursor.getString(urlIndex),
                mCursor.getString(dateIndex));
        RemoteViews views =new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.w_tv_title,article.getTitle());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
