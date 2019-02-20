package com.example.jidekareem.miriamsrecipes.mirriamActivities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.jidekareem.miriamsrecipes.R;
import com.example.jidekareem.miriamsrecipes.adapters.BakeAdapter;
import com.example.jidekareem.miriamsrecipes.data.Steps;
import com.example.jidekareem.miriamsrecipes.fragments.PlayerTextFragment;
import com.example.jidekareem.miriamsrecipes.fragments.RecipeDescriptionFragment;
import com.example.jidekareem.miriamsrecipes.fragments.RecipePlayerFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDescriptionActivity extends AppCompatActivity implements BakeAdapter.ItemClickListener {

    public static final String STEPS_ID_EXTRA = "STEPS_ID_EXTRA";
    public static final String RECIPE_NAME = "RECIPE_NAME";
    public static final String INGREDIENT_LIST = "INGREDIENT_LIST";
    private static final String SAVED_RECIPE_NAME = "SAVED_RECIPE_NAME";
    private static final String FRAGMENT_SAVED_TEXT = "FRAGMENT_SAVED_TEXT";
    private static final String FRAGMENT_SAVED_REC = "FRAGMENT_SAVED_REC";
    public static boolean mTwoPane;
    private PlayerTextFragment playerTextFragment;
    private RecipeDescriptionFragment recipeDescriptionFragment;
    private List<Steps> steps;
    private FragmentManager fragmentManager;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.recipe_description_main_activity);
        Bundle bundle = getIntent().getExtras();
        steps = bundle.getParcelableArrayList(STEPS_ID_EXTRA);
        String ingredientList = bundle.getString(INGREDIENT_LIST);
        if (savedInstanceState == null || !savedInstanceState.containsKey(SAVED_RECIPE_NAME)) {
            recipeName = bundle.getString(RECIPE_NAME);


        } else {
            recipeName = savedInstanceState.getString(SAVED_RECIPE_NAME);

        }

        setTitle(getString(R.string.mirriam) + recipeName);

        fragmentManager = getSupportFragmentManager();
        playerTextFragment = new PlayerTextFragment();
        recipeDescriptionFragment = new RecipeDescriptionFragment();
        recipeDescriptionFragment.setSteps(steps);
        playerTextFragment.setTextIn(ingredientList);

        if (savedInstanceState != null) {
            playerTextFragment = (PlayerTextFragment) fragmentManager.getFragment(savedInstanceState, FRAGMENT_SAVED_TEXT);
            recipeDescriptionFragment = (RecipeDescriptionFragment) fragmentManager.getFragment(savedInstanceState, FRAGMENT_SAVED_REC);
        } else {

            fragmentManager.beginTransaction()
                    .replace(R.id.ingredient_text, playerTextFragment)
                    .commit();

            fragmentManager.beginTransaction()
                    .replace(R.id.details_main, recipeDescriptionFragment)
                    .commit();
        }

        if (findViewById(R.id.player_main_default) != null) {
            mTwoPane = true;

            RecipePlayerFragment recipePlayerFragment = new RecipePlayerFragment();
            final String playerUrl = "";
            recipePlayerFragment.setPlayerUrl(playerUrl);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_test, recipePlayerFragment)
                    .commit();
        } else {
            mTwoPane = false;
        }

    }

    @Override
    public void onItemClick(String videoUrl, String stepInstruction, int clickedPosition) {
        if (mTwoPane) {
            RecipePlayerFragment recipePlayerFragment = new RecipePlayerFragment();
            recipePlayerFragment.setPlayerUrl(videoUrl);
            recipePlayerFragment.setStepInstructions(stepInstruction);
            recipePlayerFragment.setClickedPosition(clickedPosition);
            recipePlayerFragment.setSteps(steps);
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_test, recipePlayerFragment)
                    .commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(RecipePlayerActivity.PLAYER_VIDEO_URL, videoUrl);
            bundle.putString(RecipePlayerActivity.PLAYER_STEPS, stepInstruction);
            bundle.putInt(RecipePlayerActivity.POSITION, clickedPosition);
            bundle.putParcelableArrayList(RecipePlayerActivity.STEPS, (ArrayList<? extends Parcelable>) steps);
            Intent intent = new Intent(this, RecipePlayerActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SAVED_RECIPE_NAME, recipeName);
        fragmentManager.putFragment(outState, FRAGMENT_SAVED_TEXT, playerTextFragment);
        fragmentManager.putFragment(outState, FRAGMENT_SAVED_REC, recipeDescriptionFragment);
        super.onSaveInstanceState(outState);
    }


}
