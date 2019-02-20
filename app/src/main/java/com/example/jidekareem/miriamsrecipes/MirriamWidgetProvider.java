package com.example.jidekareem.miriamsrecipes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MirriamWidgetProvider extends AppWidgetProvider {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String idimgOld,
                                        int appWidgetId) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews rv;
        if (width < 300) {
            rv = getSingleView(context, idimgOld);
        } else {
            rv = getGridRemoteView(context);
        }

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mirriam_widget_grid_view);
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        Intent recipeDetailIntent = new Intent(context, SplashScreen.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, recipeDetailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);

        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);


        return views;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void updateMirriamWidgets(Context context, AppWidgetManager appWidgetManager,
                                            String idimgOld, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, idimgOld, appWidgetId);
        }
    }

    private static RemoteViews getSingleView(Context context, String idimgOld) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mirriam_widget_provider);
        Intent intent = new Intent(context, SplashScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setTextViewText(R.id.mirriam_widget_text, String.valueOf(idimgOld));
        views.setOnClickPendingIntent(R.id.miriam_widget_image, pendingIntent);

        return views;
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        WidgetIntentService.startMirriamIntent(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetIntentService.startMirriamIntent(context);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

