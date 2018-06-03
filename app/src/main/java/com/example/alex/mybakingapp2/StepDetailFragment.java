package com.example.alex.mybakingapp2;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_EXO_STATE = "exo";


    private SimpleExoPlayer simpleExoPlayer;
    private Step mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (Step) getArguments().getSerializable(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= null;

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            if (mItem.getVideoURL()!= null && !mItem.getVideoURL().isEmpty() && mItem.getVideoURL() !=""){
                rootView = inflater.inflate(R.layout.media_player_layout, container, false);
                ((TextView) rootView.findViewById(R.id.video_step_description_tv)).setText(mItem.getDescription());
                SimpleExoPlayerView mPlayerView = rootView.findViewById(R.id.playerView);
                initializePlayer(mPlayerView);
            }
            else {
                rootView = inflater.inflate(R.layout.step_image_view_layout, container, false);
                ((TextView) rootView.findViewById(R.id.step_description_tv)).setText(mItem.getDescription());
                ImageView imageView = rootView.findViewById(R.id.thumbnail_im);
                if (mItem.getThumbnailURL()!= null && !mItem.getThumbnailURL().isEmpty() && mItem.getThumbnailURL()!=""){
                    Picasso.get()
                            .load(mItem.getThumbnailURL())
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_broken_image_black_24dp)
                            .into(imageView);
                }
                else {
                    imageView.setVisibility(View.GONE);
                }
            }

        }

        return rootView;
    }


    private void initializePlayer(SimpleExoPlayerView mPlayerView) {

        String userAgent = Util.getUserAgent(this.getContext(),"MyBakingApp2");

        // Create an instance of the ExoPlayer.
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);

        // Prepare the MediaSource.
        MediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(mItem.getVideoURL()),
                new DefaultDataSourceFactory(
                    this.getContext(), userAgent),
                new DefaultExtractorsFactory(),
                null, null);


        mPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
    }


    private void stopPlayer(){
        if (simpleExoPlayer!=null ){
            simpleExoPlayer.stop();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        long position = simpleExoPlayer.getCurrentPosition();
        outState.putSerializable(ARG_EXO_STATE,position);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) return;
        if (savedInstanceState.containsKey(ARG_EXO_STATE)){
            long position = (long) savedInstanceState.getSerializable(ARG_EXO_STATE);
            simpleExoPlayer.seekTo(position);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer!=null){
            simpleExoPlayer.release();
        }

    }
}
