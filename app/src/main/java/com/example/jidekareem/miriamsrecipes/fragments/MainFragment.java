package com.example.jidekareem.miriamsrecipes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jidekareem.miriamsrecipes.R;
import com.example.jidekareem.miriamsrecipes.adapters.HAdapter;
import com.example.jidekareem.miriamsrecipes.data.MirriamData;
import com.example.jidekareem.miriamsrecipes.databinding.MainFragmentBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {

    public static final String MyRECIPEName = "MyRECIPEName";
    public static final String MY_SELECTED = "MY_SELECTED";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String MyRESPONSE = "MyRes";
    private static final String RECIPE_LIST = "RECIPE_LIST";
    private static List<MirriamData> mirriamDataList;
    private static String response;
    private HAdapter.MItemClickListener itemClickListener;

    private static List<MirriamData> getMirriamDataList() {
        return mirriamDataList;
    }

    public static void setMirriamDataList(List<MirriamData> mirriamDataList) {
        MainFragment.mirriamDataList = mirriamDataList;
    }

    private static String getResponse() {
        return response;
    }

    public static void setResponse(String response) {
        MainFragment.response = response;
    }


    public MainFragment() {
    }

    private static void dataSave(String response, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MyRESPONSE, response);
        editor.apply();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            itemClickListener = (HAdapter.MItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement HAdapter.MItemClickListener");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainFragmentBinding mainFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        View rootView = mainFragmentBinding.getRoot();

        Context context = rootView.getContext();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, calculateBestPosterSpanCount());
        final RecyclerView mainFragmentRecycle = mainFragmentBinding.mainFragmentRecycle;
        mainFragmentRecycle.setLayoutManager(gridLayoutManager);
        mainFragmentRecycle.setHasFixedSize(true);
        HAdapter hAdapter = new HAdapter(itemClickListener);
        mainFragmentRecycle.setAdapter(hAdapter);
        if (savedInstanceState == null || !savedInstanceState.containsKey(RECIPE_LIST)) {
            mirriamDataList = getMirriamDataList();
        } else {
            mirriamDataList = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
        }
        hAdapter.setMoviesData(mirriamDataList);
        dataSave(getResponse(), Objects.requireNonNull(getActivity()).getApplicationContext());
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(RECIPE_LIST, (ArrayList<? extends Parcelable>) mirriamDataList);
        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private int calculateBestPosterSpanCount() {
        Display display = Objects.requireNonNull(this.getActivity()).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / 800);
    }

}
