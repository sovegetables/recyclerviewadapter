package com.sovegetables.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alliu on 3/10/2016.
 *
 */
public class LazyRecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews = new SparseArray<>();

    public LazyRecyclerViewHolder(ViewGroup parent, int layoutRes) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
    }

    public LazyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        parseViews(itemView);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int viewId) {
        return (T) (mViews.get(viewId));
    }

    private void parseViews(View view) {
        if (view.getId() != View.NO_ID) {
            mViews.put(view.getId(), view);
        }
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                if (child != null) {
                    parseViews(child);
                }
            }
        }
    }

}
