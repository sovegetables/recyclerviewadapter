package com.albert.recyclerviewadapterdemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.sovegetables.adapter.AbsDelegationAdapter
import com.sovegetables.adapter.AdapterDelegate
import com.sovegetables.adapter.CommonViewHolder
import com.sovegetables.adapter.ListAdapterDelegate
import com.xogrp.recyclerviewadapterdemo.R
import kotlinx.android.synthetic.main.activity_multi_type.*

class MultiTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_type)

        val adapter = InternalAdapter()
        adapter.setOnActionListener(object : InternalAdapter.OnActionListener{
            override fun onAction1(item: Item) {
                Toast.makeText(this@MultiTypeActivity, "onAction1", Toast.LENGTH_LONG).show()
            }

            override fun onAction2(item: Item) {
                Toast.makeText(this@MultiTypeActivity, "onAction2", Toast.LENGTH_LONG).show()
            }

            override fun onAction3(item: Item) {
                Toast.makeText(this@MultiTypeActivity, "onAction3", Toast.LENGTH_LONG).show()
            }

        })

        val list = arrayListOf<Item>()
        list.add(object : Item{
            override fun type(): Item.TYPE {
                return Item.TYPE.THREE
            }
        })
        list.add(object : Item{
            override fun type(): Item.TYPE {
                return Item.TYPE.ONE
            }
        })
        list.add(object : Item{
            override fun type(): Item.TYPE {
                return Item.TYPE.TWO
            }
        })
        list.add(object : Item{
            override fun type(): Item.TYPE {
                return Item.TYPE.TWO
            }
        })
        list.add(object : Item{
            override fun type(): Item.TYPE {
                return Item.TYPE.THREE
            }
        })
        list.add(object : Item{
            override fun type(): Item.TYPE {
                return Item.TYPE.ONE
            }
        })
        list.add(object : Item{
            override fun type(): Item.TYPE {
                return Item.TYPE.TWO
            }
        })
        list.add(object : Item{
            override fun type(): Item.TYPE {
                return Item.TYPE.THREE
            }
        })
        list.add(object : Item{
            override fun type(): Item.TYPE {
                return Item.TYPE.THREE
            }
        })
        adapter.items = list
        rv_data.adapter = adapter
    }


    private interface Item{
        enum class TYPE{
            ONE, TWO, THREE
        }

        fun type(): TYPE
    }

    private class InternalAdapter: AbsDelegationAdapter<List<Item>>(){

        interface OnActionListener{
            fun onAction1(item: Item)
            fun onAction2(item: Item)
            fun onAction3(item: Item)
        }

        private var listener : OnActionListener? = null

        fun setOnActionListener(l: OnActionListener?){
            listener = l
        }

        init {
            delegatesManager.addDelegate(object : ListAdapterDelegate<Item>(){
                override fun onBindView(holder: CommonViewHolder?, t: Item?, position: Int) {
                }

                override fun isForViewType(items: MutableList<Item>, position: Int): Boolean {
                    return items[position].type() == Item.TYPE.ONE
                }

                override fun getLayoutRes(): Int {
                    return R.layout.item_mutli_type_one
                }

            })

            delegatesManager.addDelegate(object : ListAdapterDelegate<Item>(){
                override fun onBindView(holder: CommonViewHolder?, t: Item?, position: Int) {
                }

                override fun isForViewType(items: MutableList<Item>, position: Int): Boolean {
                    return items[position].type() == Item.TYPE.TWO
                }

                override fun getLayoutRes(): Int {
                    return R.layout.item_mutli_type_two
                }

            })

            delegatesManager.addDelegate(object : ListAdapterDelegate<Item>(){

                override fun onViewCreated(parent: ViewGroup?, holder: CommonViewHolder) {
                    super.onViewCreated(parent, holder)

                    val listener = View.OnClickListener {
                        when(it.id){
                            R.id.btn_one ->{
                                listener?.onAction1(AdapterDelegate.getItemByTag(holder))
                            }
                            R.id.btn_two ->{
                                listener?.onAction2(AdapterDelegate.getItemByTag(holder))
                            }
                            R.id.btn_three ->{
                                listener?.onAction3(AdapterDelegate.getItemByTag(holder))
                            }
                        }
                    }
                    holder.findViewById<Button>(R.id.btn_one).setOnClickListener(listener)
                    holder.findViewById<Button>(R.id.btn_two).setOnClickListener(listener)
                    holder.findViewById<Button>(R.id.btn_three).setOnClickListener(listener)
                }

                override fun onBindView(holder: CommonViewHolder?, t: Item?, position: Int) {
                    AdapterDelegate.setItemTag(holder, t)
                    AdapterDelegate.setPositionTag(holder, position)
                }

                override fun isForViewType(items: MutableList<Item>, position: Int): Boolean {
                    return items[position].type() == Item.TYPE.THREE
                }

                override fun getLayoutRes(): Int {
                    return R.layout.item_mutli_type_three
                }

            })
        }

        override fun getItemCount(): Int {
            return items?.size?: 0
        }

    }

    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, MultiTypeActivity::class.java))
        }
    }
}
