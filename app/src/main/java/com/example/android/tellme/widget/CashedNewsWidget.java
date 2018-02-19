package com.example.android.tellme.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.tellme.R;
import com.example.android.tellme.ui.ArticleDetailsActivity;
import com.example.android.tellme.ui.CashedActivity;

/**
 * Implementation of App Widget functionality.
 */
public class CashedNewsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,Boolean bool) {

        // Construct the RemoteViews object
        RemoteViews views = getArticlesListRemoteView(context,bool);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        WidgetServices.startActionUpdateWidget(context);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager ,int[] appWidgetIds,boolean bool){

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,appWidgetId,bool);
        }

    }
    private static RemoteViews getArticlesListRemoteView(Context context,boolean bool){
        RemoteViews views =new RemoteViews(context.getPackageName(),R.layout.cashed_news_widget);
        if(bool){
            Intent i =new Intent(context,ArticlesWidgetListService.class);
            views.setRemoteAdapter(R.id.w_lv_articles,i);
            views.setViewVisibility(R.id.w_lv_articles, View.VISIBLE);
            views.setViewVisibility(R.id.w_tv_nothing, View.GONE);
        }
        else{
            views.setViewVisibility(R.id.w_lv_articles, View.GONE);
            views.setViewVisibility(R.id.w_tv_nothing, View.VISIBLE);
        }
        Intent launch = new Intent(context, CashedActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0,launch,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.w_tv_app_name,pi);

        return views;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

