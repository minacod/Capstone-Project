package com.example.android.tellme.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by shafy on 10/02/2018.
 */

public class NetworkUtils {

    final private static String API_URL = "https://newsapi.org/v2/";
    final private static String TOP_HEADLINE="top-headlines";
    final private static String EVERYTHING="everything";
    final private static String SOURCES="sources";
    final private static String API_KEY="apiKey";
    final private static String CATEGORY="category";
    final private static String CATEGORIES[]={"general","business","entertainment","health","science", "sports", "technology"};
    final private static String LANGUAGE="language";
    final private static String QUERY="q";
    final private static String SORT_BY="sortBy";
    final private static String SORT_BY_PRAM []={"relevancy","popularity","publishedAt"};
    final private static String PAGE="page";




    public static URL buildTopHeadUrl(String apiKey,int category){
        Uri builtUri = Uri.parse(API_URL+TOP_HEADLINE).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(CATEGORY,CATEGORIES[category]).build();
        URL url =null;
        try{
            url =new URL(builtUri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildEverythingBySourceUrl(String apiKey,String source,int sortBy,int pageNum){
        Uri builtUri = Uri.parse(API_URL+EVERYTHING).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(SOURCES,source)
                .appendQueryParameter(SORT_BY,SORT_BY_PRAM[sortBy])
                .appendQueryParameter(PAGE,String.valueOf(pageNum)).build();
        URL url =null;
        try{
            url =new URL(builtUri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL buildEverythingByQueryUrl(String apiKey,String q,int sortBy,int pageNum){
        Uri builtUri = Uri.parse(API_URL+EVERYTHING).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(QUERY,q)
                .appendQueryParameter(SORT_BY,SORT_BY_PRAM[sortBy])
                .appendQueryParameter(PAGE,String.valueOf(pageNum)).build();
        URL url =null;
        try{
            url =new URL(builtUri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildSourcesUrl(String apiKey,String category){
        Uri builtUri = Uri.parse(API_URL+SOURCES).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(CATEGORY,category)
                .appendQueryParameter(LANGUAGE,"en").build();
        URL url =null;
        try{
            url =new URL(builtUri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner =new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isConnected(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null;
    }


}
