package com.sovegetables.adapter;


import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by albert on 2018/2/8.
 * 支持一种Type的RecyclerView Adapter
 *
 */

public abstract class AbsListAdapter<T> extends AbsDelegationAdapter<List<T>>{

    private boolean mIsDiffContentType;
    protected RecyclerView mRecyclerView;

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        final SparseArrayCompat<AdapterDelegate<List<T>>> delegates = delegatesManager.delegates;
        for(int i = 0, len = delegates.size(); i < len; i++){
            delegates.valueAt(i).setOnItemClickListener(onItemClickListener);
        }
    }

    public AbsListAdapter() {
        Class<?> clazz = getClass();
        Type type = clazz.getGenericSuperclass();
        if(type instanceof ParameterizedType){
            ParameterizedType pt = (ParameterizedType)type;
            Type[] typeArgs = pt.getActualTypeArguments();
            if(typeArgs[0] instanceof DiffCallBack.DiffContent){
                mIsDiffContentType = true;
            }
        }
        ListAdapterDelegate<T> delegate = new ListAdapterDelegate<T>() {

            @Override
            protected void onViewCreated(ViewGroup parent, CommonViewHolder holder) {
                AbsListAdapter.this.onViewCreated(parent, holder);
            }

            @Override
            protected void onBindView(CommonViewHolder holder, T t, int position) {
                AbsListAdapter.this.onBindView(holder, t, position);
            }

            @Override
            protected void onBindView(CommonViewHolder holder, T t, int position, List payloads) {
                AbsListAdapter.this.onBindView(holder, t, position, payloads);
            }

            @Override
            protected int getLayoutRes() {
                return AbsListAdapter.this.getLayoutRes();
            }

            @Override
            protected boolean isForViewType(@NonNull List<T> items, int position) {
                return true;
            }
        };
        delegatesManager.addDelegate(delegate);
    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
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

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    /**
     * recycler view item layout resource
     * @return layout resource
     */
    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    @Override
    public long getItemId(int position) {
        if(items == null || position >= items.size()){
            return 0;
        }
        return items.get(position).hashCode();
    }

    @Override @SuppressWarnings("unchecked")
    public void setItems(List<T> items) {
        if(mIsDiffContentType && items instanceof ArrayList) {
            ArrayList newData = DeepCloneUtil.copy((ArrayList) items);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(items, newData), false);
            this.items = items;
            diffResult.dispatchUpdatesTo(this);
        }else {
            this.items = items;
            notifyDataSetChanged();
        }
    }
}
