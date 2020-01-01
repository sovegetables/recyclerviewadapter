package com.albert.recyclerview.adapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LinearLayoutManagerLoadMoreHelper implements LoadMoreHelper{

    private int mTotalItemCount;
    private int mFirstVisibleItemPosition;
    private int mVisibleItemCount;

    @Override
    public boolean isLastPosition() {
        return mTotalItemCount == mFirstVisibleItemPosition + mVisibleItemCount;
    }

    @Override
    public void attachRecyclerView(RecyclerView recyclerView) {
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        mVisibleItemCount = layoutManager.getChildCount();
        mTotalItemCount = layoutManager.getItemCount();
        mFirstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
    }
}
