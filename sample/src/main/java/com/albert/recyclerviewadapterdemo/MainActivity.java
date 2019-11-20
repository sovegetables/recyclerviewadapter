package com.albert.recyclerviewadapterdemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.sovegetables.adapter.AbsListAdapter;
import com.sovegetables.adapter.CommonViewHolder;
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
    private static final String WRAPPER_ADAPTER = "Wrapper Adapter";
    private static final String MULTI_TYPE_ADAPTER = "Multi Type Adapter";

    private static final String[] CATEGORIES = new String[]{
            HEADER_AND_FOOTER,
            MULTI_CHOICE_LIST,
            SPARSEARRAY,
            DECORATION,
            LOAD_MORE,
            WRAPPER_ADAPTER,
            MULTI_TYPE_ADAPTER
    };

    private MainActivityContract.Presenter mMainPresenter;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter();
        mAdapter.setOnItemClickListener(new com.sovegetables.adapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, String s, int position) {
                switch (s){
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
                    case WRAPPER_ADAPTER:
                        WrapperAdapterActivity.start(MainActivity.this);
                        break;
                    case MULTI_TYPE_ADAPTER:
                        MultiTypeActivity.start(MainActivity.this);
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
        mAdapter.setItems(categories);
    }

    @Override
    public void setPresenter(MainActivityContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    private static class Adapter extends AbsListAdapter<String> {

        @Override
        protected void onBindView(CommonViewHolder holder, String s, int position) {
            Button btn = holder.findViewById(R.id.btn_category);
            btn.setText(s);
        }

        @Override
        protected int getLayoutRes() {
            return R.layout.item_categories;
        }
    }
}
