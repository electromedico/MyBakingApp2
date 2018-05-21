package com.example.alex.mybakingapp2;

import android.app.Activity;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.mybakingapp2.MediaUtils.CustomOnScrollListener;
import com.example.alex.mybakingapp2.MediaUtils.RecyclerViewStateListener;
import com.example.alex.mybakingapp2.UtilsRecyclerView.RecyclerViewDetailAdapter;
import com.example.alex.mybakingapp2.UtilsRecyclerView.ViewHolderStep;
import com.example.alex.mybakingapp2.model.Recipe;
import com.example.alex.mybakingapp2.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment implements RecyclerViewStateListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The Recepi content passed as Extra
     */
    private Recipe mItem;

    /**
     *
     * */
    private SimpleExoPlayer mExoPlayer;
    private RecyclerViewDetailAdapter mDetailAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomOnScrollListener customOnScrollListener;
    private int firsVisible, lastVisible;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (Recipe) getArguments().getSerializable(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            mLayoutManager = new LinearLayoutManager(activity);
            mDetailAdapter = new RecyclerViewDetailAdapter(activity);
            customOnScrollListener = new CustomOnScrollListener(this,mLayoutManager);
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }

            if (savedInstanceState != null) {

                //Restore state of the RecyclerView
                String keyState = getString(R.string.manager_state);
                if(savedInstanceState.containsKey(keyState)){

                    Parcelable state =savedInstanceState.getParcelable(keyState);
                    mLayoutManager.onRestoreInstanceState(state);

                }
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        if (mItem != null) {

            mRecyclerView = rootView.findViewById(R.id.recipe_detail);
            mRecyclerView.addOnScrollListener(customOnScrollListener);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDetailAdapter);
            mDetailAdapter.setmDataset(mItem);
        }

        return rootView;
    }


    @Override
    public void onScroll(int firstVisible, int lastVisible) {
        if (this.firsVisible !=firsVisible){
            this.firsVisible=firstVisible;


        }
        if (this.lastVisible!= lastVisible){
           this.lastVisible=lastVisible;
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.manager_state),mLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {

            //Restore state of the RecyclerView
            String keyState = getString(R.string.manager_state);
            if(savedInstanceState.containsKey(keyState)){

                Parcelable state =savedInstanceState.getParcelable(keyState);
                mLayoutManager.onRestoreInstanceState(state);

            }
        }
    }
}
