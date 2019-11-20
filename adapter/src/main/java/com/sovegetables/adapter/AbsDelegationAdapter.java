/*
 * Copyright (c) 2015 Hannes Dorfmann.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.sovegetables.adapter;

import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @param <T> The type of the datasource / items
 * @author Hannes Dorfmann
 */
public abstract class AbsDelegationAdapter<T> extends RecyclerView.Adapter {

    @NonNull
    protected final AdapterDelegatesManager<T> delegatesManager;
    protected T items;

    public AbsDelegationAdapter() {
        this(new AdapterDelegatesManager<T>());
    }

    public AbsDelegationAdapter(@NonNull AdapterDelegatesManager<T> delegatesManager) {
        Preconditions.checkNotNull(delegatesManager, "AdapterDelegatesManager is null");
        this.delegatesManager = delegatesManager;
        delegatesManager.attachAdapter(this);
    }

    @NonNull
    @Override @CallSuper
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        delegatesManager.onAttachedToRecyclerView(recyclerView);
    }

    @Override @CallSuper
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(items, holder, position);
    }

    @Override @CallSuper
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        delegatesManager.onBindViewHolder(items, holder, position, payloads);
    }

    @Override @CallSuper
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(items, position);
    }

    @Override @CallSuper
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        delegatesManager.onViewRecycled(holder);
    }

    @Override @CallSuper
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        return delegatesManager.onFailedToRecycleView(holder);
    }

    @Override @CallSuper
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        delegatesManager.onViewAttachedToWindow(holder);
    }

    @Override @CallSuper
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        delegatesManager.onViewDetachedFromWindow(holder);
    }

    /**
     * Get the items / data source of this adapter
     *
     * @return The items / data source
     */
    public T getItems() {
        return items;
    }

    /**
     * Set the items / data source of this adapter
     *
     * @param items The items / data source
     */
    public void setItems(T items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
