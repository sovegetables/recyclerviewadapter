package com.albert.recyclerviewadapterdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.albert.recyclerview.adapter.SparseArrayRecyclerAdapter;
import com.albert.recyclerview.decoration.HorizontalDividerDecoration;
import com.albert.recyclerview.adapter.XOLazyRecyclerViewHolder;
import com.xogrp.recyclerviewadapterdemo.R;

/**
 * Created by albert on 2016/8/5.
 */
public class DecorationActivity extends AppCompatActivity{

    private RecyclerView mRv;
    private Adapter mAdapter;

    public  static void start(Activity activity){
        activity.startActivity(new Intent(activity, DecorationActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.addItemDecoration(new HorizontalDividerDecoration(this, R.dimen.divider, R.color.colorAccent));
        SparseArray<String> data = new SparseArray<>();
        for(int i = 0;i < Data.ITEMS.length; i++){
            data.put(i, Data.ITEMS[i]);
        }
        mAdapter = new Adapter(this, data);
        mRv.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_decoration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_horizontal:

                break;
            case R.id.menu_grid:

                break;
        }
        return false;
    }

    private class Adapter extends SparseArrayRecyclerAdapter<String> {

        public Adapter(Context context, SparseArray<String> data) {
            super(context, data);
        }

        public Adapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutRes() {
            return R.layout.mulit_choice_item;
        }

        @Override
        protected void onBindDataViewHolder(XOLazyRecyclerViewHolder holder, String item, int position) {
            TextView tv = holder.get(R.id.tv_item);
            tv.setText(item);
            holder.get(R.id.cb).setVisibility(View.GONE);
        }
    }
}
