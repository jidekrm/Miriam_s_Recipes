package com.example.jidekareem.miriamsrecipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.androidnetworking.AndroidNetworking;
import com.example.jidekareem.miriamsrecipes.data.Ingredients;
import com.example.jidekareem.miriamsrecipes.data.MirriamData;
import com.example.jidekareem.miriamsrecipes.fragments.MainFragment;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class GridWidgetService extends RemoteViewsService {

    private List<Ingredients> ingredientsList;
    private int pos;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }


    private void dataRetrieval(String jayJay) {
        Moshi moshi = new Moshi.Builder().build();
        Type type = com.squareup.moshi.Types.newParameterizedType(List.class, MirriamData.class);
        JsonAdapter<List<MirriamData>> adapter = moshi.adapter(type);
        try {
            List<MirriamData> mirriamDataList = adapter.fromJson(jayJay);
            ingredientsList = mirriamDataList.get(pos).getIngredients();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        final Context context;

        GridRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            AndroidNetworking.initialize(getApplicationContext());
            AndroidNetworking.setParserFactory(new JacksonParserFactory());
        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences sharedPref = context.getSharedPreferences(MainFragment.MyPREFERENCES, Context.MODE_PRIVATE);
            final String defValue = "";
            String sh = sharedPref.getString(MainFragment.MyRESPONSE, defValue);
            pos = sharedPref.getInt(MainFragment.MY_SELECTED, -1);
            dataRetrieval(sh);

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredientsList == null) return 0;
            return ingredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            final String str = " ";
            final String str1 = "-";
            final String str2 = ".";

            String ingredients = ingredientsList.get(position).getIngredient() +
                    str +
                    str1 +
                    ingredientsList.get(position).getQuantity() +
                    ingredientsList.get(position).getMeasure() +
                    str2;
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mirriam_widget_provider);
            views.setTextViewText(R.id.mirriam_widget_text, ingredients);
            views.setViewVisibility(R.id.miriam_widget_image, View.GONE);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
