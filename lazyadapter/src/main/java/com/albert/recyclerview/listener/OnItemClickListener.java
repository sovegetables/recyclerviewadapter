package com.albert.recyclerview.listener;

import android.view.View;

/**
 * Created by albert on 2016/5/26.
 */
public interface OnItemClickListener<T> {
    void onItemClick(View view, T item, int position);
}
