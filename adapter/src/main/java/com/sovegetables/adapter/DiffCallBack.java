package com.sovegetables.adapter;


import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * Created by albert on 2018/1/26.
 * 一个通用DiffUtil.Callback，继承DiffUtil.Callback.
 * 数据实体类需要继承DiffCallBack.DiffContent接口。
 */

public class DiffCallBack<T extends DiffCallBack.DiffContent<T>> extends DiffUtil.Callback{

    private List<T> mOldData;
    private List<T> mNewData;

    public DiffCallBack(List<T> oldData, List<T> newData) {
        mOldData = oldData;
        mNewData = newData;
    }

    @Override
    public int getOldListSize() {
        return mOldData == null? 0 : mOldData.size();
    }

    @Override
    public int getNewListSize() {
        return mNewData == null? 0 : mNewData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        try {
            return mNewData.get(newItemPosition).areItemsTheSame(mOldData.get(oldItemPosition));
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        try {
            return mNewData.get(newItemPosition).areContentsTheSame(mOldData.get(oldItemPosition));
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public interface DiffContent<T>{
        boolean areItemsTheSame(T t);
        boolean areContentsTheSame(T t);
    }
}
