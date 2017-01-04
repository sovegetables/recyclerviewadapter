package com.albert.recyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alliu on 3/10/2016.
 */
public class XOLazyRecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews = new SparseArray<>();
    private View mItemView;

    public View getItemView() {
        return mItemView;
    }

    public XOLazyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemView = itemView;
        parseViews(itemView);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T get(int viewId) {
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
