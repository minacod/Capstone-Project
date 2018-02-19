package com.example.android.tellme.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by shafy on 15/02/2018.
 */

public class ArticlesContract {
    public interface ArticlesEntry{
        @DataType(DataType.Type.INTEGER)
        @AutoIncrement
        @PrimaryKey
        String ARTICLE_ID = "article_id";
        @DataType(DataType.Type.TEXT)
        String ARTICLE_TITLE = "article_title";
        @DataType(DataType.Type.TEXT)
        String ARTICLE_AUTHOR= "author";
        @DataType(DataType.Type.TEXT)
        String DESCRIPTION = "description";
        @DataType(DataType.Type.TEXT)
        String SOURCE_NAME = "source_name";
        @DataType(DataType.Type.TEXT)
        String DATE = "date";
        @DataType(DataType.Type.TEXT)
        String IMAGE_URL = "image_url";
        @DataType(DataType.Type.TEXT)
        String URL = "url";
    }
    @Database(version = ArticlesDatabase.VERSION)
    public final class ArticlesDatabase {

        public static final int VERSION = 1;

        @Table(ArticlesEntry.class) public static final String ARTICLES = "articles";
    }
}
