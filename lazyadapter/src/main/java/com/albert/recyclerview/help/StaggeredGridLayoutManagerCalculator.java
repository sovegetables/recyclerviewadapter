package com.albert.recyclerview.help;

import android.support.v7.widget.StaggeredGridLayoutManager;

import com.albert.recyclerview.listener.ItemCalculator;


/**
 * Created by albert on 2016/11/5.
 */

public class StaggeredGridLayoutManagerCalculator implements ItemCalculator {

    private StaggeredGridLayoutManager mManager;
    private int spanCount;
    private final int[] mTemp;

    public StaggeredGridLayoutManagerCalculator(StaggeredGridLayoutManager manager){
        mManager = manager;
        spanCount = manager.getSpanCount();
        mTemp = new int[spanCount];
        resetTemp();
    }

    @Override
    public int findFirstVisibleItemPosition() {
        resetTemp();
        mManager.findFirstVisibleItemPositions(mTemp);
        return mTemp[0];
    }

    @Override
    public int findLastVisibleItemPosition() {
        resetTemp();
        mManager.findLastVisibleItemPositions(mTemp);
        return 0;
    }

    private void resetTemp(){
        for (int i = 0; i < mTemp.length; i++){
            mTemp[i] = -1;
        }
    }
}
