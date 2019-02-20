package com.example.jidekareem.miriamsrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.jidekareem.miriamsrecipes.data.Steps;
import com.example.jidekareem.miriamsrecipes.mirriamActivities.RecipeDescriptionActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RecipeDescriptionActivityTest {

    @Rule
    public final ActivityTestRule<RecipeDescriptionActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeDescriptionActivity.class, false, false);


    @Test
    public void checkTextDisplayedInDynamicallyCreatedFragment() {

        List<Steps> steps = new ArrayList<>();
        final String also_known = "also Known";
        final String not_too_fancy = "not too fancy";
        final String videoURL = "";
        final int id = 1;
        steps.add(new Steps(id, also_known, not_too_fancy, videoURL, videoURL));
        final int id1 = 2;
        steps.add(new Steps(id1, also_known, not_too_fancy, videoURL, videoURL));
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        final String amala = "AMALA";
        bundle.putString(RecipeDescriptionActivity.RECIPE_NAME, amala);
        final String aLl_you_need = "ALl you need";
        bundle.putString(RecipeDescriptionActivity.INGREDIENT_LIST, aLl_you_need);
        bundle.putParcelableArrayList(RecipeDescriptionActivity.STEPS_ID_EXTRA, (ArrayList<? extends Parcelable>) steps);
        intent.putExtras(bundle);

        mActivityTestRule.launchActivity(intent);

    }


}
