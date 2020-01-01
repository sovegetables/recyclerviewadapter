package com.albert.recyclerview.adapter;

import androidx.recyclerview.widget.RecyclerView;

public interface LoadMoreHelper {
    boolean isLastPosition();
    void attachRecyclerView(RecyclerView recyclerView);
    void onScrollStateChanged(RecyclerView recyclerView, int newState);
    void onScrolled(RecyclerView recyclerView, int dx, int dy);
}
