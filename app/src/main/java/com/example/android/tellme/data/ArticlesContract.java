package com.example.android.tellme.data;

import android.net.Uri;
import android.provider.BaseColumns;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by shafy on 15/02/2018.
 */

public class ArticlesContract {

    public static final String AUTHORITY = "com.example.android.tellme";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_CASHED="cashed";


    public static class ArticlesEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CASHED).build();
        public static final String TABLE_NAME="articles";
        public static final String ARTICLE_TITLE = "article_title";
        public static final String ARTICLE_AUTHOR= "author";
        public static final String DESCRIPTION = "description";
        public static final String SOURCE_NAME = "source_name";
        public static final String DATE = "date";
        public static final String IMAGE_URL = "image_url";
        public static final String URL = "url";
    }
}
