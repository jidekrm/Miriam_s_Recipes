package com.example.jidekareem.miriamsrecipes.mirriamActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.jidekareem.miriamsrecipes.R;
import com.example.jidekareem.miriamsrecipes.SplashScreen;
import com.example.jidekareem.miriamsrecipes.WidgetIntentService;
import com.example.jidekareem.miriamsrecipes.adapters.HAdapter;
import com.example.jidekareem.miriamsrecipes.data.Ingredients;
import com.example.jidekareem.miriamsrecipes.data.MirriamData;
import com.example.jidekareem.miriamsrecipes.data.Steps;
import com.example.jidekareem.miriamsrecipes.fragments.MainFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HAdapter.MItemClickListener {
    private static final String FRAGMENT_SAVED_MAIN = "FRAGMENT_SAVED_MAIN";
    public static Boolean TABLET;
    private MainFragment mainFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.main_activity);
        Bundle bundle = getIntent().getExtras();
        String response = bundle.getString(SplashScreen.SPLASH_RESPONSE);
        List<MirriamData> mirriamDataList = bundle.getParcelableArrayList(SplashScreen.SPLASH_LIST);
        MainFragment.setMirriamDataList(mirriamDataList);
        MainFragment.setResponse(response);

        TABLET = findViewById(R.id.main_bake_fragment2) != null;

        mainFragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            mainFragment = (MainFragment) fragmentManager.getFragment(savedInstanceState, FRAGMENT_SAVED_MAIN);
        } else {

            fragmentManager.beginTransaction()
                    .replace(R.id.add_main_fragment, mainFragment)
                    .commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentManager.putFragment(outState, FRAGMENT_SAVED_MAIN, mainFragment);
    }

    @Override
    public void onMItemClick(MirriamData mirriamData, int clickedPosition) {
        List<Steps> steps = mirriamData.getSteps();
        String recipe = mirriamData.getName();
        StringBuilder builder = new StringBuilder();
        for (Ingredients ingredients : mirriamData.getIngredients()) {
            final String str = " ";
            final String str1 = "-";
            final String str2 = ",";
            builder.append(ingredients.getIngredient())
                    .append(str)
                    .append(str1)
                    .append(ingredients.getQuantity())
                    .append(ingredients.getMeasure())
                    .append(str2)
                    .append(str);
        }
        String ingredients = builder.toString();
        Bundle bundle = new Bundle();
        bundle.putString(RecipeDescriptionActivity.RECIPE_NAME, recipe);
        bundle.putString(RecipeDescriptionActivity.INGREDIENT_LIST, ingredients.substring(0, ingredients.length() - 1));
        bundle.putParcelableArrayList(RecipeDescriptionActivity.STEPS_ID_EXTRA, (ArrayList<? extends Parcelable>) steps);
        Intent intent = new Intent(this, RecipeDescriptionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(MainFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MainFragment.MyRECIPEName, recipe);
        editor.putInt(MainFragment.MY_SELECTED, clickedPosition);
        editor.apply();

        WidgetIntentService.startMirriamIntent(this);
    }
}
