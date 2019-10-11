package com.albert.recyclerview.adapter;

import androidx.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.albert.recyclerview.listener.OnItemClickListener;

/**
 * Created by albert on 2016/12/27.
 */
public abstract class RecyclerViewType<T extends BaseRecyclerAdapter> {
    private T mAdapter;

    public RecyclerViewType(T adapter) {
        mAdapter = adapter;
        if (adapter == null) {
            throw new IllegalArgumentException("Adapter is null");
        }
    }

    public T getAdapter() {
        return mAdapter;
    }

    protected abstract boolean isMatchViewType(int position);

    protected abstract int getItemViewType();

    @LayoutRes
    protected abstract int getItemLayoutRes();

    protected boolean enableItemClickListener(){
        return true;
    }

    protected abstract void onBindViewHolder(XOLazyRecyclerViewHolder holder, Object item, int position);

    protected XOLazyRecyclerViewHolder onCreateViewHolder(ViewGroup parent) {
        return new XOLazyRecyclerViewHolder(inflater(getItemLayoutRes(), parent));
    }

    public final View inflater(@LayoutRes int layoutResId, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
    }

    @SuppressWarnings("unchecked")
    protected void onBindViewHolder(XOLazyRecyclerViewHolder holder, final int position) {
        final Object item = getAdapter().getItemByPosition(position);
        onBindViewHolder(holder, item, position);
        final OnItemClickListener<Object> onItemClickListener = getAdapter().getOnItemClickListener();
        if (onItemClickListener != null && enableItemClickListener()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, item, position);
                }
            });
        }
    }

    protected void onViewRecycled(XOLazyRecyclerViewHolder holder) {
    }

    protected void onViewAttachedToWindow(XOLazyRecyclerViewHolder holder) {
    }

    protected void onViewDetachedFromWindow(XOLazyRecyclerViewHolder holder) {
    }

    /**
     * 某个Item完全显示在屏幕时该方法被调用
     *
     * @param position the item of position
     */
    protected void onInScreen(int position) {
    }

    /**
     * 某个Item完全离开屏幕时该方法被调用
     *
     * @param position the item of position
     */
    protected void onOutScreen(int position) {
    }
}
