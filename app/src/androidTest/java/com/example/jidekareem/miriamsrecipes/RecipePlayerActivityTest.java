package com.example.jidekareem.miriamsrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.jidekareem.miriamsrecipes.data.Steps;
import com.example.jidekareem.miriamsrecipes.mirriamActivities.RecipePlayerActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class RecipePlayerActivityTest {

    @Rule
    public final ActivityTestRule<RecipePlayerActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipePlayerActivity.class, false, false);

    @Test
    public void checkTextDisplayedInDynamicallyCreatedFragment() {

        List<Steps> steps = new ArrayList<>();
        final int id = 1;
        final String also_known = "also Known";
        final String not_too_fancy = "not too fancy";
        final String value = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";
        final String thumbnailURL = "";
        steps.add(new Steps(id, also_known, not_too_fancy, value, thumbnailURL));
        steps.add(new Steps(id, also_known, not_too_fancy, value, thumbnailURL));
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("PLAYER_VIDEO_URL", value);
        bundle.putString("PLAYER_STEPS", "Hello There");
        bundle.putInt("POSITION", id);

        bundle.putParcelableArrayList("STEPS", (ArrayList<? extends Parcelable>) steps);
        intent.putExtras(bundle);

        mActivityTestRule.launchActivity(intent);


    }

}
