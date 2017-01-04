package com.albert.recyclerviewadapterdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.albert.recyclerview.adapter.HeaderAndFooterListRecyclerAdapter;
import com.albert.recyclerview.adapter.XOLazyRecyclerViewHolder;
import com.xogrp.recyclerviewadapterdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by albert on 2016/7/23.
 */

public class HeaderAndFooterActivity extends AppCompatActivity {

    public  static void start(Activity activity){
        activity.startActivity(new Intent(activity, HeaderAndFooterActivity.class));
    }

    private Adapter mAdapter;
    private RecyclerView mRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter(this, new ArrayList<>(Arrays.asList(Data.ITEMS)));
        mRv.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_and_remove, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                mAdapter.addHeader(R.layout.item_header, null);
                mRv.scrollToPosition(0);
                break;
            case R.id.menu_remove:
                mAdapter.removeHeader();
                break;
            case R.id.menu_add_footer:
                mAdapter.addFooter(R.layout.item_footer, null);
                mRv.scrollToPosition(mAdapter.getItemCount() - 1);
                break;
            case R.id.menu_remove_footer:
                mAdapter.removeFooter();
                break;

        }
        return false;
    }

    private class Adapter extends HeaderAndFooterListRecyclerAdapter<String>{

        public Adapter(Context context, List<String> data) {
            super(context, data);
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
