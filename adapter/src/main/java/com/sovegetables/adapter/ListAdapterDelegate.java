package com.sovegetables.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class ListAdapterDelegate<T> extends AdapterDelegate<List<T>> {

    private OnItemClickListener<T> mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        CommonViewHolder holder = new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T item = getItemByTag(v);
                    mOnItemClickListener.onItemClick(v, item, getPositionByTag(v));
                }
            });
        }
        onViewCreated(parent, holder);
        return holder;
    }

    @Override
    protected final void onBindViewHolder(List<T> items, @NonNull RecyclerView.ViewHolder holder, int position, List payloads) {
        setItemTag(holder, items.get(position));
        setPositionTag(holder, position);
        onBindView(((CommonViewHolder) holder), items.get(position), position, payloads);
    }

    @Override
    protected void onBindViewHolder(List<T> items, @NonNull RecyclerView.ViewHolder holder, int position) {
        onBindView(((CommonViewHolder)holder), items.get(position), position);
    }

    protected void onBindView(CommonViewHolder holder, T t, int position, List payloads){
        onBindView(holder, t, position);
    }

    /**
     * invoked after onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
     * @param holder CommonViewHolder
     * @param t data resource item
     */
    protected abstract void onBindView(CommonViewHolder holder, T t, int position);

    /**
     * invoked after onCreateViewHolder(@NonNull ViewGroup parent)
     * @param parent ViewGroup
     * @param holder CommonViewHolder
     */
    protected void onViewCreated(ViewGroup parent, CommonViewHolder holder){}

    /**
     * recycler view item layout resource
     * @return layout resource
     */
    @LayoutRes
    protected abstract int getLayoutRes();
}
