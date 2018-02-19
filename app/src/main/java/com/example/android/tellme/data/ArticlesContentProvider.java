package com.example.android.tellme.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by shafy on 15/02/2018.
 */

public class ArticlesContentProvider extends android.content.ContentProvider {
    public static final int ARTICLES=100;
    public static final int ARTICLE_WITH_URL = 101;
    public DbHelper mDbHelper;
    public static UriMatcher sUriMatcher=buildUriMatcher() ;

    @Override
    public boolean onCreate() {
        mDbHelper=new DbHelper(getContext());
        return true;
    }

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ArticlesContract.AUTHORITY,ArticlesContract.PATH_CASHED,ARTICLES);
        uriMatcher.addURI(ArticlesContract.AUTHORITY,ArticlesContract.PATH_CASHED+ "/*",ARTICLE_WITH_URL);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int mode=sUriMatcher.match(uri);
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor result;
        switch (mode) {
            case ARTICLES:
                result = db.query(ArticlesContract.ArticlesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(uri.toString());
        }
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int mode=sUriMatcher.match(uri);
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        long result;
        Uri returnUri;
        switch (mode) {
            case ARTICLES:
                result =db.insert(ArticlesContract.ArticlesEntry.TABLE_NAME,
                        null,
                        contentValues);
                if ( result > 0 ) {
                    returnUri = ContentUris.withAppendedId(ArticlesContract.ArticlesEntry.CONTENT_URI, result);
                } else {
                    throw new android.database.SQLException(uri.toString());
                }
                break;
            default:
                throw new UnsupportedOperationException( uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int mode=sUriMatcher.match(uri);
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        int result=0;
        switch (mode) {
            case ARTICLE_WITH_URL:
                String url=uri.getLastPathSegment();
                try {
                    result =db.delete(ArticlesContract.ArticlesEntry.TABLE_NAME,
                            ArticlesContract.ArticlesEntry.URL+"=?",
                            new String[]{url});
                }catch (Exception e){
                    e.printStackTrace();
                    FirebaseCrash.log(uri.toString());
                }

                break;
            default:
                throw new UnsupportedOperationException(uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
