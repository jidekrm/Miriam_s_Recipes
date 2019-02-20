package com.example.jidekareem.miriamsrecipes.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jidekareem.miriamsrecipes.R;
import com.example.jidekareem.miriamsrecipes.data.Steps;
import com.example.jidekareem.miriamsrecipes.databinding.RecipePlayerFragmentBinding;
import com.example.jidekareem.miriamsrecipes.mirriamActivities.RecipeDescriptionActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RecipePlayerFragment extends Fragment {

    private static final String SAVED_PLAYER_URL = "SAVED_PLAYER_URL";
    private static final String SAVED_STEPS = "SAVED_STEPS";
    private static final String SELECTED_POSITION = "SELECTED_POSITION";
    private static final String CLICKED_POSITION = "CLICKED_POSITION";
    private static final String STEPS = "STEPS";
    private Context context;
    private String playerUrl;
    private String stepInstructions;
    private List<Steps> steps;
    private int clickedPosition;
    private long playbackPosition;
    private boolean playWhenReady = true;
    private RecipePlayerFragmentBinding recipePlayerFragmentBinding;
    private SimpleExoPlayer mExoPlayer;

    public RecipePlayerFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        recipePlayerFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.recipe_player_fragment, container, false);
        View rootView = recipePlayerFragmentBinding.getRoot();
        context = rootView.getContext();
        if (savedInstanceState != null) {
            playerUrl = savedInstanceState.getString(SAVED_PLAYER_URL);
            stepInstructions = savedInstanceState.getString(SAVED_STEPS);
            playbackPosition = savedInstanceState.getLong(SELECTED_POSITION, 0);
            clickedPosition = savedInstanceState.getInt(CLICKED_POSITION);
            steps = savedInstanceState.getParcelableArrayList(STEPS);


        } else {
            playerUrl = getPlayerUrl();
            stepInstructions = getStepInstructions();
        }


        alternateDisplay();
        Configuration newConfig = this.getResources().getConfiguration();
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT || RecipeDescriptionActivity.mTwoPane) {
            recipePlayerFragmentBinding.stepInstruction.setText(stepInstructions);
            buttonImplementations();
        }
        return rootView;
    }

    private void additionalSteps(int i) {
        clickedPosition = i;
        String instruction = steps.get(clickedPosition).getDescription();
        playerUrl = steps.get(clickedPosition).getVideoURL();
        recipePlayerFragmentBinding.stepInstruction.setText(instruction);
        releasePlayer();
        playbackPosition = 0;
        initializePlayer(Uri.parse(playerUrl));
        alternateDisplay();
    }

    private String getStepInstructions() {
        return stepInstructions;
    }

    public void setStepInstructions(String stepInstructions) {
        this.stepInstructions = stepInstructions;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateView(inflater, (ViewGroup) Objects.requireNonNull(getView()));
        alternateDisplay();
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recipePlayerFragmentBinding.stepInstruction.setText(stepInstructions);
            buttonImplementations();
        }

    }

    private void buttonImplementations() {
        recipePlayerFragmentBinding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedPosition < steps.size() - 1) {

                    additionalSteps(getClickedPosition() + 1);
                }

            }
        });

        recipePlayerFragmentBinding.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedPosition > 0) {
                    additionalSteps(getClickedPosition() - 1);
                }
            }
        });
    }

    private void alternateDisplay() {
        final String anObject = "";
        if (playerUrl.equals(anObject)) {
            recipePlayerFragmentBinding.playerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.food_logo));

        }
    }

    private void populateView(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        recipePlayerFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.recipe_player_fragment, viewGroup, true);
        recipePlayerFragmentBinding.playerView.setPlayer(mExoPlayer);
    }

    private void releasePlayer() {
        playbackPosition = mExoPlayer.getCurrentPosition();
        playWhenReady = mExoPlayer.getPlayWhenReady();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer(Uri.parse(playerUrl));
        }
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    private int getClickedPosition() {
        return clickedPosition;
    }

    public void setClickedPosition(int clickedPosition) {
        this.clickedPosition = clickedPosition;
    }

    private void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(context);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(defaultRenderersFactory, trackSelector, loadControl);
            recipePlayerFragmentBinding.playerView.setPlayer(mExoPlayer);
            MediaSource mediaSource = buildMediaSource(mediaUri);

            mExoPlayer.prepare(mediaSource, true, false);
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(playbackPosition);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        final String miriamsrecipes = "miriamsrecipes";
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(miriamsrecipes)).
                createMediaSource(uri);
    }


    private String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(SELECTED_POSITION, mExoPlayer.getCurrentPosition());
        outState.putString(SAVED_PLAYER_URL, playerUrl);
        outState.putString(SAVED_STEPS, stepInstructions);
        outState.putInt(CLICKED_POSITION, clickedPosition);
        outState.putParcelableArrayList(STEPS, (ArrayList<? extends Parcelable>) steps);
        super.onSaveInstanceState(outState);
    }
}
