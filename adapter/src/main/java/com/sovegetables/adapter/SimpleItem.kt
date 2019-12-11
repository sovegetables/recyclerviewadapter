package com.sovegetables.adapter;

import androidx.annotation.DrawableRes;

interface SimpleItem {
    fun title() : String
    @DrawableRes
    fun icon() : Int
    fun selected(): Boolean
    fun setChecked(checked: Boolean)
    fun checked(): Boolean
    fun enableCheckBoxMode(): Boolean
    fun onItemCheckedChangedListener(): OnItemCheckedChangedListener?

    interface OnItemCheckedChangedListener{
        fun onChanged(checked: Boolean)
    }

    data class Default(private var title: String, private var icon : Int = -1, private var selected: Boolean = true,
                       private var itemChecked: Boolean = false, private var enableCheckBoxMode: Boolean = false,
                       private var listener: OnItemCheckedChangedListener? = null): SimpleItem{

        override fun onItemCheckedChangedListener(): OnItemCheckedChangedListener? {
            return listener
        }

        override fun enableCheckBoxMode(): Boolean {
            return enableCheckBoxMode
        }

        override fun title(): String {
            return title
        }

        override fun icon(): Int {
            return icon
        }

        override fun selected(): Boolean {
            return selected
        }

        override fun setChecked(checked: Boolean) {
            itemChecked = checked
        }

        override fun checked(): Boolean {
            return itemChecked
        }
    }
}
