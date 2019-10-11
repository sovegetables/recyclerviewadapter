package com.albert.recyclerviewadapterdemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.albert.recyclerview.adapter.ListRecyclerAdapter
import com.albert.recyclerview.adapter.XOLazyRecyclerViewHolder
import com.sovegetables.WrapperAdapter
import com.xogrp.recyclerviewadapterdemo.R
import kotlinx.android.synthetic.main.activity_wrapper_adapter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class WrapperAdapterActivity : AppCompatActivity() {

    private lateinit var mAdapter:Adapter
    private lateinit var mAdapter2:Adapter2
    private lateinit var mWrapperAdapter:WrapperAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wrapper_adapter)

        mAdapter = Adapter(this)
        mAdapter2 = Adapter2()

        val adapters = arrayListOf<RecyclerView.Adapter<out RecyclerView.ViewHolder>>()
        adapters.add(mAdapter)
        adapters.add(mAdapter2)
        mWrapperAdapter = WrapperAdapter(adapters)
        rv_data.adapter = mWrapperAdapter

        GlobalScope.launch {
            delay(100)
            val list = arrayListOf<Item>()
            for (i in 0..5){
                list.add(Item(i))
            }
            mAdapter2.items = list
            GlobalScope.launch(Dispatchers.Main) {
                mAdapter.data = listOf(*Data.ITEMS)
                mAdapter2.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_and_remove, menu)
        return true
    }

    private var headerIndex = 0
    private var footerIndex = 0
    private val headerViews = arrayListOf<View>()
    private val footerViews = arrayListOf<View>()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                val header = LayoutInflater.from(this).inflate(R.layout.item_header, null)
                headerViews.add(header)
                val textView = header.findViewById<TextView>(R.id.tv_item)
                textView.text = "我是header" + headerIndex
                mWrapperAdapter.addHeaderView(header)
                rv_data.scrollToPosition(0)
                headerIndex ++
            }
            R.id.menu_remove -> {
                if(headerViews.isNotEmpty()){
                    val view = headerViews[headerViews.size - 1]
                    mWrapperAdapter.removeHeaderView(view)
                    headerViews.remove(view)
                    headerIndex --
                }
            }
            R.id.menu_add_footer -> {
                val footer = LayoutInflater.from(this).inflate(R.layout.item_footer, null)
                val textView = footer.findViewById<TextView>(R.id.tv_item)
                textView.text = "我是Footer" + footerIndex
                footerViews.add(footer)
                mWrapperAdapter.addFooterView(footer)
                rv_data.scrollToPosition(mWrapperAdapter.itemCount - 1)
                footerIndex ++
            }
            R.id.menu_remove_footer -> {
                if(footerViews.isNotEmpty()){
                    val view = footerViews[footerViews.size - 1]
                    mWrapperAdapter.removeFooterView(view)
                    footerViews.remove(view)
                    footerIndex --
                }
            }
        }
        return false
    }


    private data class Item(var index: Int)

    private class Adapter2 : RecyclerView.Adapter<XOLazyRecyclerViewHolder>(){

        var items : ArrayList<Item>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XOLazyRecyclerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wrapper2, parent, false)
            return XOLazyRecyclerViewHolder(view)
        }

        override fun getItemCount(): Int {
            return if(items == null) 0 else items!!.size
        }

        override fun onBindViewHolder(holder: XOLazyRecyclerViewHolder, position: Int) {
        }

    }

    private class Adapter(context: Context) : ListRecyclerAdapter<String>(context) {

        override fun onBindDataViewHolder(holder: XOLazyRecyclerViewHolder, item: String, position: Int) {
            val tv = holder.get<TextView>(R.id.tv_item)
            tv.text = item
        }

        override fun getItemLayoutRes(): Int {
            return R.layout.mulit_choice_item
        }

    }

    companion object {

        @JvmStatic
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, WrapperAdapterActivity::class.java))
        }
    }
}
