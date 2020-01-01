package com.albert.recyclerview.adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class StaggeredGridLayoutManagerLoadMoreHelper implements LoadMoreHelper{

    private int mTotalItemCount;
    private int[] mFirstVisibleItemPositions;
    private int mVisibleItemCount;

    @Override
    public boolean isLastPosition() {
        return mTotalItemCount <= mFirstVisibleItemPositions[mFirstVisibleItemPositions.length - 1] + mVisibleItemCount;
    }

    @Override
    public void attachRecyclerView(RecyclerView recyclerView) {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        mFirstVisibleItemPositions = new int[layoutManager.getSpanCount()];
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        mVisibleItemCount = layoutManager.getChildCount();
        mTotalItemCount = layoutManager.getItemCount();
        layoutManager.findFirstCompletelyVisibleItemPositions(mFirstVisibleItemPositions);
    }
}
