package com.albert.recyclerviewadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.sovegetables.WrapperAdapter;
import com.sovegetables.adapter.AbDiffListAdapter;
import com.sovegetables.adapter.CommonViewHolder;
import com.sovegetables.adapter.DeepClone;
import com.sovegetables.adapter.DiffCallBack;
import com.sovegetables.loadmore.LoadMoreHelper;
import com.sovegetables.loadmore.OnLoadMoreListener;
import com.xogrp.recyclerviewadapterdemo.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by albert on 2016/8/29.
 */
public class LoadMoreActivity extends AppCompatActivity{

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity, LoadMoreActivity.class));
    }

    private Adapter mAdapter;
    private final Executor mTaskExecutor = Executors.newFixedThreadPool(1);
    private WrapperAdapter mWrapperAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = findViewById(R.id.rv);
        mAdapter = new Adapter();
        mWrapperAdapter = new WrapperAdapter(mAdapter);
        LoadMoreHelper.create().attach(rv, new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NotNull final RecyclerView recyclerView, final @NotNull LoadMoreHelper.LoadMoreFinisher loadMoreFinisher) {
                final View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.spinner, recyclerView, false);
                mWrapperAdapter.addFooterView(view);
                mTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ArrayList<Item> newData = new ArrayList<>(10);
                        for (int index = 0; index < 10; index ++) {
                            newData.add(new Item("item" + index));
                        }
                        final List<Item> items = mAdapter.getItems();
                        items.addAll(newData);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setItems(items, new DeepClone<List<Item>>() {
                                    @Override
                                    public List<Item> deepClone(List<Item> source) {
                                        ArrayList<Item> newData = new ArrayList<>(source.size());
                                        for (Item item : source) {
                                            newData.add(new Item(item.value));
                                        }
                                        return newData;
                                    }
                                }, new Consumer<List<Item>>() {
                                    @Override
                                    public void accept(List<Item> items) {
                                        mWrapperAdapter.removeFooterView(view);
                                        loadMoreFinisher.finishLoadMore();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        ArrayList<Item> data = new ArrayList<>(10);
        for (int index = 0; index < 10; index ++) {
            data.add(new Item("item" + index));
        }
        mAdapter.setItems(data);
        rv.setAdapter(mWrapperAdapter);
    }

    static class Item implements DiffCallBack.DiffItem<Item>{

        private String value;

        Item(String value) {
            this.value = value;
        }

        @Override
        public boolean areItemsTheSame(Item s) {
            return this == s;
        }

        @Override
        public boolean areContentsTheSame(Item s) {
            return this.equals(s);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Item)) return false;
            Item item = (Item) o;
            return value != null ? value.equals(item.value) : item.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    static class Adapter extends AbDiffListAdapter<Item> {

        @Override
        protected void onBindView(CommonViewHolder holder, Item s, int position) {
            TextView tv = holder.findViewById(R.id.tv_item);
            tv.setText(s.value);
        }

        @Override
        protected int getLayoutRes() {
            return R.layout.load_more_item;
        }
    }

}
