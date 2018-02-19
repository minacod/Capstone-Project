package com.example.android.tellme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shafy on 19/02/2018.
 */

public class DbHelper extends SQLiteOpenHelper{

    private final static String DATABASE_NAME="tell_me_articles.db";
    private final static int DATABASE_VERSION=1;

    public DbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQLQuery="CREATE TABLE " + ArticlesContract.ArticlesEntry.TABLE_NAME+" ("+
                ArticlesContract.ArticlesEntry.SOURCE_NAME+" TEXT, "+
                ArticlesContract.ArticlesEntry.ARTICLE_TITLE+" TEXT, "+
                ArticlesContract.ArticlesEntry.ARTICLE_AUTHOR+" TEXT, "+
                ArticlesContract.ArticlesEntry.DESCRIPTION+" TEXT, "+
                ArticlesContract.ArticlesEntry.DATE+" TEXT, "+
                ArticlesContract.ArticlesEntry.URL+" TEXT, "+
                ArticlesContract.ArticlesEntry.IMAGE_URL+" TEXT);"
                ;
        sqLiteDatabase.execSQL(SQLQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String SQLQuery="DROP TABLE IF EXISTS " + ArticlesContract.ArticlesEntry.TABLE_NAME ;
        sqLiteDatabase.execSQL(SQLQuery);
        onCreate(sqLiteDatabase);
    }
}
