package com.example.alex.mybakingapp2.MediaUtils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class CustomOnScrollListener extends RecyclerView.OnScrollListener{
    private final RecyclerViewStateListener recyclerViewStateListener;
    private int firstPosition;
    private int lastPosition;
    private final LinearLayoutManager mLayoutManager;

    public CustomOnScrollListener(RecyclerViewStateListener listener,LinearLayoutManager linearLayoutManager) {
        super();
        recyclerViewStateListener = listener;
        mLayoutManager=linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        firstPosition = mLayoutManager.findFirstVisibleItemPosition();
        lastPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        recyclerViewStateListener.onScroll(firstPosition,lastPosition);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

    }


}
