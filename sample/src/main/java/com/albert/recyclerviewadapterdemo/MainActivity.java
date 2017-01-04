package com.albert.recyclerviewadapterdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.albert.recyclerview.adapter.ListRecyclerAdapter;
import com.albert.recyclerview.adapter.XOLazyRecyclerViewHolder;
import com.albert.recyclerview.listener.OnItemClickListener;
import com.xogrp.recyclerviewadapterdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityContract.MainView {

    private static final String HEADER_AND_FOOTER = "Header and Footer List";
    private static final String MULTI_CHOICE_LIST = "Multi Choice List";
    private static final String SPARSEARRAY = "SparseArray List";
    private static final String DECORATION = "RecyclerView Decoration";
    private static final String LOAD_MORE = "RecyclerView Load More";

    private static final String[] CATEGORIES = new String[]{
            HEADER_AND_FOOTER,
            MULTI_CHOICE_LIST,
            SPARSEARRAY,
            DECORATION,
            LOAD_MORE,
    };

    private MainActivityContract.Presenter mMainPresenter;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter(this);
        mAdapter.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, String item, int position) {
                switch (item){
                    case HEADER_AND_FOOTER:
                        HeaderAndFooterActivity.start(MainActivity.this);
                        break;
                    case MULTI_CHOICE_LIST:
                        MultiChoiceActivity.start(MainActivity.this);
                        break;
                    case SPARSEARRAY:
                        SpareArrayActivity.start(MainActivity.this);
                        break;
                    case DECORATION:
                        DecorationActivity.start(MainActivity.this);
                        break;
                    case LOAD_MORE:
                        LoadMoreActivity.start(MainActivity.this);
                        break;
                }
            }
        });
        rv.setAdapter(mAdapter);

        MainPresenter.injectPresenter(this, new ArrayList<>(Arrays.asList(CATEGORIES)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainPresenter.start();
    }

    @Override
    public void showCategories(List<String> categories) {
        mAdapter.setData(categories);
    }

    @Override
    public void setPresenter(MainActivityContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    private class Adapter extends ListRecyclerAdapter<String> {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutRes() {
            return R.layout.item_categories;
        }

        @Override
        protected void onBindDataViewHolder(XOLazyRecyclerViewHolder holder, String item, int position) {
            Button btn = holder.get(R.id.btn_category);
            btn.setText(item);
        }

    }
}
