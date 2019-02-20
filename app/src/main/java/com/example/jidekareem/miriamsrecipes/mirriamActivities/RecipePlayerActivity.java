package com.example.jidekareem.miriamsrecipes.mirriamActivities;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jidekareem.miriamsrecipes.R;
import com.example.jidekareem.miriamsrecipes.data.Steps;
import com.example.jidekareem.miriamsrecipes.fragments.RecipePlayerFragment;

public class RecipePlayerActivity extends AppCompatActivity {

    public static final String PLAYER_VIDEO_URL = "PLAYER_VIDEO_URL";
    public static final String PLAYER_STEPS = "PLAYER_STEPS";
    public static final String POSITION = "POSITION";
    public static final String STEPS = "STEPS";
    private static final String FRAGMENT_SAVED = "FRAGMENT_SAVED" ;
    private RecipePlayerFragment recipePlayerFragment;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.recipe_player_main_activity);
        recipePlayerFragment = new RecipePlayerFragment();
        if (!RecipeDescriptionActivity.mTwoPane) {
            Bundle bundle = getIntent().getExtras();
            recipePlayerFragment.setPlayerUrl(bundle.getString(PLAYER_VIDEO_URL));
            recipePlayerFragment.setStepInstructions(bundle.getString(PLAYER_STEPS));
            recipePlayerFragment.setClickedPosition(bundle.getInt(POSITION, -1));
            recipePlayerFragment.setSteps(bundle.<Steps>getParcelableArrayList(STEPS));
            Configuration newConfig = this.getResources().getConfiguration();
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (savedInstanceState != null){
               recipePlayerFragment = (RecipePlayerFragment) fragmentManager.getFragment(savedInstanceState, FRAGMENT_SAVED);
            }else {

                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_test, recipePlayerFragment)
                        .commit();
            }

            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                getSupportActionBar().hide();
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                getSupportActionBar().show();
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportActionBar().show();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_SAVED, recipePlayerFragment);
    }
}
