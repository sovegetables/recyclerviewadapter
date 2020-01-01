package com.sovegetables.adapter

import androidx.recyclerview.widget.DiffUtil

class DiffCallBack<T : DiffCallBack.DiffItem<T>>(private val mOldData: List<T>?, private val mNewData: List<T>?) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldData?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return mNewData?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        try {
            return mNewData!![newItemPosition].areItemsTheSame(mOldData!![oldItemPosition])
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        try {
            return mNewData!![newItemPosition].areContentsTheSame(mOldData!![oldItemPosition])
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    interface DiffItem<T> {
        fun areItemsTheSame(t: T): Boolean
        fun areContentsTheSame(t: T): Boolean
    }
}