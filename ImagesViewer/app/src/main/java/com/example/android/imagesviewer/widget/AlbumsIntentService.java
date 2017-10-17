package com.example.android.imagesviewer.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.imagesviewer.R;

/**
 * Project name ImagesViewer
 * Created by kenneth on 15/10/2017.
 */

public class AlbumsIntentService extends IntentService {
    private static final String ACTION_UPDATE_ALBUMS_WIDGETS = "com.example.android.imagesviewer.action.update_albums_widgets";

    public AlbumsIntentService() {
        super("AlbumsIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_ALBUMS_WIDGETS.equals(action)) {
                handleActionUpdateAlbumsWidgets();
            }
        }
    }

    private void handleActionUpdateAlbumsWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(AlbumsIntentService.this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(AlbumsIntentService.this, AlbumAppWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        AlbumAppWidget.updateAlbumsWidgets(AlbumsIntentService.this, appWidgetManager, appWidgetIds);
    }

    public static void startActionUpdateAlbumsWidgets(Context context) {
        Intent intent = new Intent(context, AlbumsIntentService.class);
        intent.setAction(ACTION_UPDATE_ALBUMS_WIDGETS);
        context.startService(intent);
    }
}
