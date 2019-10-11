package com.albert.recyclerviewadapterdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.albert.recyclerview.adapter.LoadMoreListRecyclerAdapter;
import com.albert.recyclerview.adapter.XOLazyRecyclerViewHolder;
import com.xogrp.recyclerviewadapterdemo.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by albert on 2016/8/29.
 */
public class LoadMoreActivity extends AppCompatActivity{

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity, LoadMoreActivity.class));
    }

    private Adapter mAdapter;
    private RecyclerView mRv;
    private static final int SPAN_COUNT = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = (RecyclerView) findViewById(R.id.rv);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mRv.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new Adapter(this);
        mAdapter.setLoadMoreLayout(R.layout.spinner);
        mAdapter.setOnLoadMoreListener(new LoadMoreListRecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreListRecyclerAdapter adapter) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.finishedLoadMore();
                        mAdapter.insertData(new ArrayList<>(Arrays.asList(Data.ITEMS)));
                    }
                }, 3000);
            }
        });
        mRv.setAdapter(mAdapter);
        mAdapter.setData(new ArrayList<>(Arrays.asList(Data.ITEMS)));
    }

    private class Adapter extends LoadMoreListRecyclerAdapter<String> {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutRes() {
            return R.layout.mulit_choice_item;
        }

        @Override
        protected void onBindDataViewHolder(XOLazyRecyclerViewHolder holder, String item, int dataIndex, int position) {
            TextView tv = holder.get(R.id.tv_item);
            tv.setText(item);
        }
    }

}
