package com.example.jidekareem.miriamsrecipes.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jidekareem.miriamsrecipes.R;
import com.example.jidekareem.miriamsrecipes.databinding.PlayerTextFragmentBinding;

public class PlayerTextFragment extends Fragment {
    private static final String SAVED_RECIPE_INGREDIENT = "SAVED_RECIPE_INGREDIENT" ;
    private String textIn;

    public PlayerTextFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PlayerTextFragmentBinding playerTextFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.player_text_fragment, container, false);
        View view = playerTextFragmentBinding.getRoot();

        if (savedInstanceState == null || !savedInstanceState.containsKey(SAVED_RECIPE_INGREDIENT)) {
            textIn= getTextIn();
        } else {
            textIn = savedInstanceState.getString(SAVED_RECIPE_INGREDIENT);
        }

        playerTextFragmentBinding.mplayertext.setText(textIn);
        return view;
    }

    private String getTextIn() {
        return textIn;
    }

    public void setTextIn(String textIn) {
        this.textIn = textIn;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SAVED_RECIPE_INGREDIENT, textIn);
        super.onSaveInstanceState(outState);
    }

}
