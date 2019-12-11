package com.albert.recyclerviewadapterdemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sovegetables.adapter.SimpleItem
import com.xogrp.recyclerviewadapterdemo.R
import kotlinx.android.synthetic.main.activity_simple_adapter.*

class SimpleAdapterActivity : AppCompatActivity() {

    companion object{
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SimpleAdapterActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_adapter)

        val list = ArrayList<SimpleItem.Default>()
        list.add(SimpleItem.Default("栏目 1 ", R.drawable.me_setting))
        list.add(SimpleItem.Default("栏目 2 ", R.drawable.me_study))
        list.add(SimpleItem.Default("栏目 3 ", R.drawable.me_team))
        list.add(SimpleItem.Default("栏目 4 ", R.drawable.me_setting, selected = false))
        list.add(SimpleItem.Default("栏目 5 ", R.drawable.me_team, itemChecked = false, enableCheckBoxMode = true,
                listener = object : SimpleItem.OnItemCheckedChangedListener{
            override fun onChanged(checked: Boolean) {
                Toast.makeText(application, "开关:$checked", Toast.LENGTH_SHORT).show()
            }

        }))
        list.add(SimpleItem.Default("栏目 6 ", R.drawable.me_study))
        list.add(SimpleItem.Default("栏目 7 ", R.drawable.me_team))
        list.add(SimpleItem.Default("栏目 8 ", R.drawable.me_setting))
        val adapter = com.sovegetables.adapter.SimpleAdapter.Builder()
                .lineAligned(true)
                .textStyle(R.style.CustomAdapterSimpleText)
                .item(list)
                .build()
        adapter.setOnItemClickListener { _, t, _ ->
            Toast.makeText(this, t.title(), Toast.LENGTH_SHORT).show()
        }
        rv_data.adapter = adapter
    }
}
