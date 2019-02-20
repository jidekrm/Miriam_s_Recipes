package com.example.jidekareem.miriamsrecipes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.jidekareem.miriamsrecipes.data.MirriamData;
import com.example.jidekareem.miriamsrecipes.databinding.ActivitySplashScreenBinding;
import com.example.jidekareem.miriamsrecipes.mirriamActivities.MainActivity;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    public static final String SPLASH_RESPONSE ="SPLASH_RESPONSE" ;
    public static final String SPLASH_LIST = "SPLASH_LIST" ;

    private List<MirriamData> mirriamDataList;
    private ActivitySplashScreenBinding activitySplashScreenBinding;


   private String mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activitySplashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen) ;
       setContentView( R.layout.activity_splash_screen);

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        if (checkInternetAvailability()){
            new NetworkAssess().invoke();
            showDataView();
        }else {
            showNoInternet();
        }

        hideSystemUi();

    }

    //hide no internet connection view
    private void showDataView() {
        activitySplashScreenBinding.noInternet.setVisibility(View.INVISIBLE);

    }

    //Show no internet connection view
    private void showNoInternet() {

        activitySplashScreenBinding.noInternet.setVisibility(View.VISIBLE);
    }

    //Set to FullScreen
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }



        //call NetWork
    private class NetworkAssess {
        private static final String RECIPE_DIRECTORY = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        void invoke() {
            AndroidNetworking.get(RECIPE_DIRECTORY)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            dataRetrieval(response);
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onError(ANError anError) {
                        }
                    });
        }

        private void dataRetrieval(String jayJay) {
            mResponse= jayJay;
            Moshi moshi = new Moshi.Builder().build();
            Type type = com.squareup.moshi.Types.newParameterizedType(List.class, MirriamData.class);
            JsonAdapter<List<MirriamData>> adapter = moshi.adapter(type);
            try {
                mirriamDataList = adapter.fromJson(jayJay);
                myIntents();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void myIntents(){
        Bundle bundle = new Bundle();
        bundle.putString(SPLASH_RESPONSE, mResponse);
        bundle.putParcelableArrayList(SPLASH_LIST, (ArrayList<? extends Parcelable>) mirriamDataList);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Check if Internet is available
    private boolean checkInternetAvailability() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnected();
    }


}
