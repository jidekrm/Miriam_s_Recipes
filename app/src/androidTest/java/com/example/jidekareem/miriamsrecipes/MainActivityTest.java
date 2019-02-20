package com.example.jidekareem.miriamsrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.jidekareem.miriamsrecipes.data.Ingredients;
import com.example.jidekareem.miriamsrecipes.data.MirriamData;
import com.example.jidekareem.miriamsrecipes.data.Steps;
import com.example.jidekareem.miriamsrecipes.mirriamActivities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void checkMainActivityIntent() {
        List<Steps> steps = new ArrayList<>();
        final String also_known = "also Known";
        final String not_too_fancy = "not too fancy";
        final String videoURL = "http://www.html5videoplayer.net/videos/toystory.mp4";
        final int id = 1;
        steps.add(new Steps(id, also_known, not_too_fancy, videoURL, videoURL));
        final int id1 = 2;
        steps.add(new Steps(id1, also_known, not_too_fancy, videoURL, videoURL));

        List<Ingredients> ingredients = new ArrayList<>();
        ingredients.add(new Ingredients(11.0, "came", "bane"));
        ingredients.add(new Ingredients(11.0, "came", "bane"));
        ingredients.add(new Ingredients(11.0, "came", "bane"));


        List<MirriamData> mirriamDataList = new ArrayList<>();
        mirriamDataList.add(new MirriamData(1, "bane", 2, ingredients, steps, "came"));
        mirriamDataList.add(new MirriamData(2, "bane", 2, ingredients, steps, "came"));
        mirriamDataList.add(new MirriamData(3, "bane", 2, ingredients, steps, "came"));

        final String amala = "AMALA";
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(SplashScreen.SPLASH_RESPONSE, amala);
        bundle.putParcelableArrayList(SplashScreen.SPLASH_LIST, (ArrayList<? extends Parcelable>) mirriamDataList);
        intent.putExtras(bundle);

        mActivityTestRule.launchActivity(intent);


    }


}
