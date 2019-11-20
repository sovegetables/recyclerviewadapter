package com.albert.recyclerviewadapterdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovegetables.adapter.AbsListAdapter
import com.sovegetables.adapter.CommonViewHolder
import com.xogrp.recyclerviewadapterdemo.R
import java.util.*

class Main2Activity : AppCompatActivity(), MainActivityContract.MainView{

    companion object{
        private const val HEADER_AND_FOOTER = "Header and Footer List"
        private const val MULTI_CHOICE_LIST = "Multi Choice List"
        private const val SPARSEARRAY = "SparseArray List"
        private const val DECORATION = "RecyclerView Decoration"
        private const val LOAD_MORE = "RecyclerView Load More"
        private const val WRAPPER_ADAPTER = "Wrapper Adapter"
        private const val MULTI_TYPE_ADAPTER = "Multi Type Adapter"

        private val CATEGORIES = arrayOf(HEADER_AND_FOOTER, MULTI_CHOICE_LIST, SPARSEARRAY, DECORATION, LOAD_MORE, WRAPPER_ADAPTER, MULTI_TYPE_ADAPTER)
    }

    private var mMainPresenter: MainActivityContract.Presenter? = null
    private lateinit var mAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rv = findViewById<RecyclerView>(R.id.rv)
        mAdapter = Adapter()
        mAdapter.setOnItemClickListener { _, item, _ ->
            when (item) {
                HEADER_AND_FOOTER -> HeaderAndFooterActivity.start(this@Main2Activity)
                MULTI_CHOICE_LIST -> MultiChoiceActivity.start(this@Main2Activity)
                SPARSEARRAY -> SpareArrayActivity.start(this@Main2Activity)
                DECORATION -> DecorationActivity.start(this@Main2Activity)
                LOAD_MORE -> {
                    LoadMoreActivity.start(this@Main2Activity)
                    WrapperAdapterActivity.start(this@Main2Activity)
                }
                WRAPPER_ADAPTER -> WrapperAdapterActivity.start(this@Main2Activity)
                MULTI_TYPE_ADAPTER -> MultiTypeActivity.start(this@Main2Activity)
            }
        }
        rv.adapter = mAdapter

        MainPresenter.injectPresenter(this, ArrayList(listOf(*CATEGORIES)))
    }

    override fun onStart() {
        super.onStart()
        mMainPresenter!!.start()
    }

    override fun showCategories(categories: List<String>) {
        mAdapter.items = categories
    }

    override fun setPresenter(mainPresenter: MainActivityContract.Presenter) {
        mMainPresenter = mainPresenter
    }

    private class Adapter : AbsListAdapter<String>() {

        override fun onBindView(holder: CommonViewHolder, s: String, position: Int) {
            val btn = holder.findViewById<TextView>(R.id.btn_category)
            btn.text = s
        }

        override fun getLayoutRes(): Int {
            return R.layout.item_categories
        }
    }
}
