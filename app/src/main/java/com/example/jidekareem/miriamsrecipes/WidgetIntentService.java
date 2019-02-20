package com.example.jidekareem.miriamsrecipes;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.jidekareem.miriamsrecipes.fragments.MainFragment;


public class WidgetIntentService extends IntentService {

    private static final String ACTION_MIRRIAM = "com.example.jidekareem.miriamsrecipes.action.MIRRIAM";
    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    public static void startMirriamIntent(Context context) {
        Intent intent = new Intent(context, WidgetIntentService.class);
        intent.setAction(ACTION_MIRRIAM);
        context.startService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
                if (ACTION_MIRRIAM.equals(action)) {
                loadData();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void loadData() {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(MainFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        String idOld = sharedPref.getString(MainFragment.MyRECIPEName, "");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MirriamWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        MirriamWidgetProvider.updateMirriamWidgets(this, appWidgetManager, idOld, appWidgetIds);
    }

}
