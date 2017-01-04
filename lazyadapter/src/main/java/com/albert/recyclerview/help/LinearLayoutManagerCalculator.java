package com.albert.recyclerview.help;

import android.support.v7.widget.LinearLayoutManager;

import com.albert.recyclerview.listener.ItemCalculator;


/**
 * Created by albert on 2016/11/5.
 */

public class LinearLayoutManagerCalculator implements ItemCalculator {

    private LinearLayoutManager mManager;

    public LinearLayoutManagerCalculator(LinearLayoutManager manager){
        mManager = manager;
    }

    @Override
    public int findFirstVisibleItemPosition() {
        return mManager.findFirstVisibleItemPosition();
    }

    @Override
    public int findLastVisibleItemPosition() {
        return mManager.findLastVisibleItemPosition();
    }
}
