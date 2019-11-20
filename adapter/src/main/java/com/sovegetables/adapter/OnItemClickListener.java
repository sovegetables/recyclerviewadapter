package com.sovegetables.adapter;

import android.view.View;

public interface OnItemClickListener<T> {
    void onItemClick(View view, T t, int position);
}
