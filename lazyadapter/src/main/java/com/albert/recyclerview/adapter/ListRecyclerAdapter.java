package com.albert.recyclerview.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;

import java.util.List;

/**
 * Created by albert on 2016/4/8.
 */
public abstract class ListRecyclerAdapter<T> extends BaseRecyclerAdapter<List<T>, T> {

    public ListRecyclerAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public ListRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return getData() == null? 0 : getData().size();
    }

    @Override
    protected final void buildRecyclerViewType(RecyclerViewTypeBuilder builder) {
        builder.addRecyclerViewType(new DataRecyclerViewType(this));
    }

    @Override
    public final T getItemByPosition(int position) {
        List<T> data = getData();
        return data == null || position >= data.size() ? null : data.get(position);
    }

    @LayoutRes
    protected abstract int getItemLayoutRes();

    protected abstract void onBindDataViewHolder(XOLazyRecyclerViewHolder holder, T item, int position);

    protected static class DataRecyclerViewType extends RecyclerViewType<ListRecyclerAdapter> {

        public DataRecyclerViewType(ListRecyclerAdapter adapter){
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
