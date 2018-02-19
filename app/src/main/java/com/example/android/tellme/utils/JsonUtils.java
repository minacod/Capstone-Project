package com.example.android.tellme.utils;

import com.example.android.tellme.model.Article;
import com.example.android.tellme.model.Source;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shafy on 10/02/2018.
 */

public class JsonUtils {
    public JsonUtils (){

    }

    public static ArrayList<Article> getArticlesFormJson(String jsonResponse,ArrayList<Article> articles) throws JSONException {

        String STATUS = "status";
        String ARTICLES_LIST="articles";
        String SOURCE="source";
        String SOURCE_NAME="name";
        String AUTHOR="author";
        String TITLE="title";
        String DESCRIPTION="description";
        String ARTICLE_URL="url";
        String IMAGE_URL="urlToImage";
        String DATE="publishedAt";
        String sourceName;
        String author;
        String title;
        String description;
        String url;
        String imageUrl;
        String date ;

        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        if(jsonResponseObject.getString(STATUS).equals("ok")) {
            JSONArray articleJsonArray = jsonResponseObject.getJSONArray(ARTICLES_LIST);
            if(articles==null)
                articles  = new ArrayList<>();
            for (int i = 0; i < articleJsonArray.length(); i++) {
                JSONObject articleJsonObject = articleJsonArray.getJSONObject(i);
                JSONObject object = articleJsonObject.getJSONObject(SOURCE);
                sourceName = object.getString(SOURCE_NAME);
                author = articleJsonObject.getString(AUTHOR);
                title = articleJsonObject.getString(TITLE);
                description = articleJsonObject.getString(DESCRIPTION);
                imageUrl = articleJsonObject.getString(IMAGE_URL);
                url = articleJsonObject.getString(ARTICLE_URL);
                date = articleJsonObject.getString(DATE);
                articles.add(new Article(sourceName,author,title,description,imageUrl,url,date));
            }
            return articles;
        }
        return null;
    }

    public static ArrayList<Source> getSourcesFormJson(String jsonResponse) throws JSONException {

        String STATUS = "status";
        String SOURCES ="sources";
        String SOURCE_ID = "id";
        String SOURCE_NAME="name";
        String DESCRIPTION="description";
        String SOURCE_URL="url";
        String sourceId;
        String sourceName;
        String description;
        String url;

        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        if(jsonResponseObject.getString(STATUS).equals("ok")) {
            JSONArray sourceJsonArray = jsonResponseObject.getJSONArray(SOURCES);
            ArrayList<Source> sourceArrayList  = new ArrayList<>();
            for (int i = 0; i < sourceJsonArray.length(); i++) {
                JSONObject sourceJsonObject = sourceJsonArray.getJSONObject(i);
                sourceId = sourceJsonObject.getString(SOURCE_ID);
                sourceName = sourceJsonObject.getString(SOURCE_NAME);
                description = sourceJsonObject.getString(DESCRIPTION);
                url = sourceJsonObject.getString(SOURCE_URL);
                sourceArrayList.add(new Source(sourceId,sourceName,description,url));
            }
            return sourceArrayList;
        }
        return null;
    }

}
