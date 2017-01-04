package com.albert.recyclerviewadapterdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.albert.recyclerview.adapter.MultiChoiceListRecyclerAdapter;
import com.albert.recyclerview.adapter.XOLazyRecyclerViewHolder;
import com.xogrp.recyclerviewadapterdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.albert.recyclerviewadapterdemo.Data.ITEMS;

/**
 * Created by albert on 2016/7/23.
 */

public class MultiChoiceActivity extends AppCompatActivity {

    public  static void start(Activity activity){
        activity.startActivity(new Intent(activity, MultiChoiceActivity.class));
    }

    private Adapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter(this, new ArrayList<>(Arrays.asList(ITEMS)));
        rv.setAdapter(mAdapter);
        mAdapter.setMultiChoiceModeListener(new MultiChoiceListRecyclerAdapter.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_delete:
                        List<String> selectedData = mAdapter.getSelectedDataItems();
                        Toast.makeText(mAdapter.getContext(), "Delete : " + selectedData.toString(), Toast.LENGTH_SHORT).show();
                        final List<String> data = mAdapter.getData();
                        data.removeAll(selectedData);
                        mAdapter.setData(data);
                        mode.finish();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.multi_choice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_multi_choice:
                mAdapter.setChoiceMode(Adapter.MULTIPLE_MODAL);
                break;
            case R.id.menu_single_choice:
                mAdapter.setChoiceMode(Adapter.SINGLE_MODAL);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Adapter extends MultiChoiceListRecyclerAdapter<String>{

        private int mModal;
        public static final int SINGLE_MODAL = 1;
        public static final int MULTIPLE_MODAL = 2;

        public Adapter(Context context) {
            super(context);
        }

        public Adapter(Context context, List<String> data) {
            super(context, data);
        }

        public void setChoiceMode(int modal){
            if(modal == SINGLE_MODAL){
                mModal = CHOICE_MODE_SINGLE_MODAL;
            }else if(modal == MULTIPLE_MODAL){
                mModal = CHOICE_MODE_MULTIPLE_MODAL;
            }
        }

        @Override
        protected int getChoiceMode() {
            return mModal == 0 ? CHOICE_MODE_MULTIPLE_MODAL : mModal;
        }

        @Override
        protected void onBindMultiChoiceViewHolder(XOLazyRecyclerViewHolder holder, String item, int dataIndex, int position) {
            TextView tv = holder.get(R.id.tv_item);
            tv.setText(item);

            CheckBox cb = holder.get(R.id.cb);
            final SparseBooleanArray selectedMap = getSelectStates();
            if(selectedMap != null && selectedMap.get(position)){
                cb.setChecked(true);
            }else {
                cb.setChecked(false);
            }
        }

        @Override
        protected int getItemLayoutRes() {
            return R.layout.mulit_choice_item;
        }
    }
}
