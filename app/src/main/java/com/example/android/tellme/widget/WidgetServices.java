package com.example.android.tellme.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.tellme.R;
import com.example.android.tellme.data.ArticlesContentProvider;
import com.example.android.tellme.data.ArticlesContract;

/**
 * Created by shafy on 16/02/2018.
 */

public class WidgetServices extends IntentService {

    public static final String ACTION_UPDATE_WIDGET =
            "com.example.shafy.bakingpal.action.update_widget";

    public static void startActionUpdateWidget(Context context){
        Intent intent = new Intent(context,WidgetServices.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }
    public WidgetServices() {
        super("WidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {
        Uri uri = ArticlesContract.ArticlesEntry.CONTENT_URI;;
        Cursor c =getContentResolver().query(uri,null,null,null,null);
        assert c != null;
        boolean bool = (c.getCount()>0);
        c.close();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appwidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,CashedNewsWidget.class));
        CashedNewsWidget.updateWidget(this,appWidgetManager,appwidgetIds,bool);
        appWidgetManager.notifyAppWidgetViewDataChanged(appwidgetIds,R.id.w_lv_articles);
    }
}
