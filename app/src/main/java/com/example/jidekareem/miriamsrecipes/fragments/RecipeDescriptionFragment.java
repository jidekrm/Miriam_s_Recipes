package com.example.jidekareem.miriamsrecipes.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jidekareem.miriamsrecipes.R;
import com.example.jidekareem.miriamsrecipes.adapters.BakeAdapter;
import com.example.jidekareem.miriamsrecipes.data.Steps;
import com.example.jidekareem.miriamsrecipes.databinding.RecipeDescriptionFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class RecipeDescriptionFragment extends Fragment {

    private static final String SAVED_STEPS = "SAVED_STEPS";
    private List<Steps> steps;
    private BakeAdapter.ItemClickListener itemClickListener;

    public RecipeDescriptionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecipeDescriptionFragmentBinding recipeDescriptionFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.recipe_description_fragment, container, false);
        View rootView = recipeDescriptionFragmentBinding.getRoot();
        Context context = rootView.getContext();
        recipeDescriptionFragmentBinding.mainRecycle.setLayoutManager(new LinearLayoutManager(context));
        recipeDescriptionFragmentBinding.mainRecycle.setHasFixedSize(true);
        BakeAdapter bakeAdapter = new BakeAdapter(itemClickListener);
        recipeDescriptionFragmentBinding.mainRecycle.setAdapter(bakeAdapter);
        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(SAVED_STEPS);
            bakeAdapter.setMoviesData(steps);
        } else {
            bakeAdapter.setMoviesData(getSteps());
        }

        return rootView;
    }

    private List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            itemClickListener = (BakeAdapter.ItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onItemClickListener");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(SAVED_STEPS, (ArrayList<? extends Parcelable>) steps);
        super.onSaveInstanceState(outState);
    }
}
