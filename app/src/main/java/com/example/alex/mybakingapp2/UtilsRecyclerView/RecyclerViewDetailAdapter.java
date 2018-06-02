package com.example.alex.mybakingapp2.UtilsRecyclerView;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.mybakingapp2.MediaUtils.RecyclerViewStateListener;
import com.example.alex.mybakingapp2.R;
import com.example.alex.mybakingapp2.model.Ingredient;
import com.example.alex.mybakingapp2.model.Label;
import com.example.alex.mybakingapp2.model.Recipe;
import com.example.alex.mybakingapp2.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewStateListener, ExoPlayer.EventListener {

    private final List<Object> items = new ArrayList<>();
    private final Context context;
    private final HashMap<String , MediaSource> mediaSourceHashMap;
    private final HashMap<String , SimpleExoPlayer> simpleExoPlayerHashMap;


    private final int INGREDIENT_CL =0;
    private final int STEP_CL = 1;
    private final int LABEL_CL=2;
    private final int VIDEO_STEP_CL=3;


    public RecyclerViewDetailAdapter(Context context) {
        this.context = context;
        mediaSourceHashMap =new HashMap<>();
        simpleExoPlayerHashMap= new HashMap<>();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder ;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType){
            case INGREDIENT_CL:
                view=inflater.inflate(R.layout.ingredient_layout,parent,false);
                viewHolder= new ViewHolderIngredients(view);
                break;

            case STEP_CL:
                view=inflater.inflate(R.layout.step_layout,parent,false);
                viewHolder= new ViewHolderStep(view);
                break;

            case VIDEO_STEP_CL:
                view=inflater.inflate(R.layout.media_player_layout,parent,false);
                viewHolder = new ViewHolderVideo(view);
                break;
            case LABEL_CL:
                view=inflater.inflate(R.layout.label_layout,parent,false);
                viewHolder= new ViewHolderLabel(view);
                break;
            default:
                viewHolder= null;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()){
            case INGREDIENT_CL:
                ViewHolderIngredients viewHolderIngredients= (ViewHolderIngredients) holder;
                configureIngredients(viewHolderIngredients,position);
                break;

            case STEP_CL:
                ViewHolderStep viewHolderStep = (ViewHolderStep) holder;
                configureSteps(viewHolderStep,position);
                break;

            case VIDEO_STEP_CL:
                ViewHolderVideo viewHolderVideo = (ViewHolderVideo) holder;
                configureVideoSteps(viewHolderVideo,position);
                break;
            case LABEL_CL:
                ViewHolderLabel viewHolderLabel= (ViewHolderLabel) holder;
                configureLabels(viewHolderLabel,position);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        if (items==null || items.size()==0)return 0;
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = items.get(position);
        if ( o instanceof Ingredient){
            return INGREDIENT_CL;
        }
        else if (o instanceof Step){
            Step s = (Step) (items.get(position));
            if (s.getVideoURL()== null || s.getVideoURL().isEmpty()){
                return STEP_CL;
            }
            return VIDEO_STEP_CL;
        }
        else{
            return LABEL_CL;
        }
    }

    public void setmDataset(Recipe recipe){

        //Insert Ingredients Label
        items.add(new Label(Label.LABEL_INGREDIENTS));

        //retrieve list of ingredients and add to the items list
        Object[] ingredients = recipe.getIngredients().toArray();
        for (Object ingredient : ingredients){
            items.add(ingredient);
        }

        //Insert Steps Label
        items.add(new Label(Label.LABEL_STEPS));

        //retrieve list of steps and add them to the items list
        Object[] steps = recipe.getSteps().toArray();
        for ( Object step : steps){
            items.add(step);
        }

        notifyDataSetChanged();


    }

    private void configureSteps(ViewHolderStep viewHolderStep, int position){
        Step step = (Step) items.get(position);
        String stepString;
        if (step.getId()==0){
            stepString=step.getShortDescription();
            viewHolderStep.getStepIdTv().setText(stepString);
            viewHolderStep.getStepTv().setVisibility(View.GONE);
        }
        else {
            stepString=context.getString(R.string.step_label)+" "+step.getId()+" "+step.getShortDescription();
            viewHolderStep.getStepIdTv().setText(stepString);
            viewHolderStep.getStepTv().setText(step.getDescription());
        }

        if (step.getThumbnailURL()!=null && !step.getThumbnailURL().isEmpty()){
            viewHolderStep.setImageURL(step.getThumbnailURL());
        }
        else {
            viewHolderStep.getImageTv().setVisibility(View.GONE);
        }

    }

    private void configureLabels(ViewHolderLabel holderLabel, int position){
        if (position==0){
            holderLabel.getLabelTv().setText(Label.LABEL_INGREDIENTS);
        }
        else {
            holderLabel.getLabelTv().setText(Label.LABEL_STEPS);
        }
    }

    private void configureVideoSteps(ViewHolderVideo viewHolderVideo, int position){
        Step step = (Step) items.get(position);
        String stepString;
        if (step.getId()==0){
            stepString=step.getShortDescription();
            viewHolderVideo.getVideoStepId().setText(stepString);
            viewHolderVideo.getVideoDescription().setVisibility(View.GONE);

        }
        else {
            stepString=context.getString(R.string.step_label)+" "+step.getId()+" "+step.getShortDescription();
            viewHolderVideo.getVideoStepId().setText(stepString);
            viewHolderVideo.getVideoDescription().setText(step.getDescription());

        }
        viewHolderVideo.setIdKey(String.valueOf(step.getId()));
        viewHolderVideo.setUrlString(step.getVideoURL());
    }

    private void configureIngredients(ViewHolderIngredients viewHolderIngredients, int position){
        Ingredient i = (Ingredient) items.get(position);
        viewHolderIngredients.getIngredientTv().setText(i.getIngredient());
        viewHolderIngredients.getQuantityTv().setText(
               String.valueOf(i.getQuantity()) +
                        " " + i.getMeasure());

    }

    @Override
    public void onScroll(int firstVisible, int lastVisible) {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * local class to manage the label viewholder
     * */
    private class  ViewHolderLabel extends RecyclerView.ViewHolder{
        private TextView labelTv;

        ViewHolderLabel(View itemView) {
            super(itemView);
            labelTv= itemView.findViewById(R.id.label_tv);
        }

        TextView getLabelTv() {
            return labelTv;
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if ((holder instanceof ViewHolderVideo)){

            String uriString = ((ViewHolderVideo) holder).getUrlString();
            if (uriString== null){
                return;
            }
            Uri uri = Uri.parse(uriString);

            initializePlayer(uri, (ViewHolderVideo) holder);
        }

        else if (holder instanceof ViewHolderStep){

            ViewHolderStep viewHolderStep = (ViewHolderStep) holder;

            if (viewHolderStep.getImageURL()== null || viewHolderStep.getImageURL().isEmpty()){
                ImageView imageView = viewHolderStep.getImageTv();
                Picasso.get()
                        .load(viewHolderStep.getImageURL())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(viewHolderStep.getImageTv());
            }

        }


    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof ViewHolderVideo){
            String id = ((ViewHolderVideo)holder).getIdKey();
            releasePlayer(id);
        }


    }

    private void initializePlayer(Uri mediaUri, ViewHolderVideo holder) {
        
        SimpleExoPlayerView mPlayerView = holder.getmPlayerView();
        String id= holder.getIdKey();
        String userAgent = Util.getUserAgent(context,"MyBakingApp2");
        MediaSource mediaSource=mediaSourceHashMap.get(id);

        // Create an instance of the ExoPlayer.
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        simpleExoPlayerHashMap.put(id,exoPlayer);

        // Prepare the MediaSource.
        if(mediaSource == null){
           mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        context, userAgent), new DefaultExtractorsFactory(), null, null);
           mediaSourceHashMap.put(id, mediaSource);
            }

        mPlayerView.setPlayer(exoPlayer);
        exoPlayer.prepare(mediaSource);
    }

    /**if the viewholder is no longer visible we realease the player
     * */
    private void releasePlayer(String id){
        SimpleExoPlayer exoPlayer = simpleExoPlayerHashMap.get(id);
        if (exoPlayer!=null ){
            exoPlayer.stop();
            exoPlayer.release();
            simpleExoPlayerHashMap.remove(id);

        }
    }

}
