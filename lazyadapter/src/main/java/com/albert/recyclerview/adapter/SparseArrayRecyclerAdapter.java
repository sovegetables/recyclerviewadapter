package com.albert.recyclerview.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.util.SparseArray;

/**
 * Created by albert on 2016/5/26.
 */
public abstract class SparseArrayRecyclerAdapter<T> extends BaseRecyclerAdapter<SparseArray<T>, T>{

    public SparseArrayRecyclerAdapter(Context context) {
        super(context);
    }

    public SparseArrayRecyclerAdapter(Context context, SparseArray<T> data) {
        super(context, data);
    }

    @LayoutRes
    protected abstract int getItemLayoutRes();

    @Override
    public int getItemCount() {
        return getData() == null? 0 : getData().size();
    }

    protected abstract void onBindDataViewHolder(XOLazyRecyclerViewHolder holder, T item, int position);

    @Override
    protected void buildRecyclerViewType(RecyclerViewTypeBuilder builder) {
        builder.addRecyclerViewType(new DataRecyclerViewType(this));
    }

    @Override
    public T getItemByPosition(int position) {
        SparseArray<T> data = getData();
        if(data != null){
            return data.get(data.indexOfKey(position));
        }
        return null;
    }

    private static class DataRecyclerViewType extends RecyclerViewType<SparseArrayRecyclerAdapter> {

        public DataRecyclerViewType(SparseArrayRecyclerAdapter adapter) {
            super(adapter);
        }

        @Override
        protected boolean isMatchViewType(int position) {
            return true;
        }

        @Override
        protected int getItemViewType() {
            return 0;
        }

        @Override
        protected int getItemLayoutRes() {
            return getAdapter().getItemLayoutRes();
        }

        @Override @SuppressWarnings("unchecked")
        protected void onBindViewHolder(XOLazyRecyclerViewHolder holder, Object item, int position) {
            getAdapter().onBindDataViewHolder(holder, item, position);
        }
    }
}
