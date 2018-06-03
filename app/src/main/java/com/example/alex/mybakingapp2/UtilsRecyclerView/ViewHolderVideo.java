package com.example.alex.mybakingapp2.UtilsRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alex.mybakingapp2.R;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

class ViewHolderVideo extends RecyclerView.ViewHolder{

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.video_step_description_tv)
    TextView videoDescription;
    private String urlString;
    private String idKey;

    public ViewHolderVideo(View itemView) {

        super(itemView);

        ButterKnife.bind(this,itemView);
    }


}
