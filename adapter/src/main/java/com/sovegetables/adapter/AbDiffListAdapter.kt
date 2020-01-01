package com.sovegetables.adapter

import android.os.Handler
import android.os.Looper
import androidx.annotation.UiThread
import androidx.core.util.Consumer
import androidx.recyclerview.widget.DiffUtil
import java.io.*
import java.util.concurrent.Executors

abstract class AbDiffListAdapter<E : DiffCallBack.DiffItem<E>> : AbsListAdapter<E>(){

    private val uiHandler = Handler(Looper.getMainLooper())
    private val taskThread = Executors.newFixedThreadPool(1)

    @UiThread
    override fun setItems(items: MutableList<E>?) {
        setItems(items, null)
    }

    @UiThread
    open fun setItems(items: MutableList<E>?, deepClone: DeepClone<MutableList<E>?>? = null, consumer: Consumer<MutableList<E>?>? = null) {
        if(deepClone == null){
            super.setItems(items)
        }else{
            taskThread.execute {
                val newData = deepClone.deepClone(items)
                val diffResult = DiffUtil.calculateDiff(DiffCallBack(getItems(), newData), true)
                uiHandler.post {
                    consumer?.accept(items)
                    this.items = items
                    diffResult.dispatchUpdatesTo(this)
                }
            }
        }
    }
}

interface DeepClone<T>{
    fun deepClone(source: T): T
}