package com.example.android.tellme.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by shafy on 15/02/2018.
 */

@ContentProvider(authority = ArticlesContentProvider.AUTHORITY, database = ArticlesContract.ArticlesDatabase.class)
public class ArticlesContentProvider {
    public static final String AUTHORITY = "com.example.shafy.tellme";

    @TableEndpoint(table = ArticlesContract.ArticlesDatabase.ARTICLES) public static class Articles {
        @ContentUri(
                path = ArticlesContract.ArticlesDatabase.ARTICLES,
                type = "vnd.android.cursor.dir/list")
        public static final Uri ARTICLES = Uri.parse("content://" + AUTHORITY ).buildUpon()
                .appendPath(ArticlesContract.ArticlesDatabase.ARTICLES).build();
    }
}
