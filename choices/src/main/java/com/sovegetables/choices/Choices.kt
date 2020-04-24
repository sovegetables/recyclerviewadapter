package com.sovegetables.choices

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

interface MultiChoiceModeListener : ActionMode.Callback {
    fun onItemCheckedStateChanged(mode: ActionMode, position: Int, checked: Boolean)
}

interface OnMultiSelectionToggleListener {
    fun onOpenMultiChoiceMode()
    fun onCloseMultiChoiceMode()
}

class MultiChoiceModeWrapper : MultiChoiceModeListener {
    private var mWrapped: MultiChoiceModeListener? = null
    private var mOnMultiSelectionToggleListener: OnMultiSelectionToggleListener? = null
    private lateinit var recycleView : RecyclerView

    fun attach(rv: RecyclerView){
        recycleView = rv
    }

    fun setWrapped(wrapped: MultiChoiceModeListener) {
        mWrapped = wrapped
    }

    fun setOnMultiSelectionToggleListener(listener: OnMultiSelectionToggleListener){
        mOnMultiSelectionToggleListener = listener
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mOnMultiSelectionToggleListener?.onOpenMultiChoiceMode()
        ViewCompat.setNestedScrollingEnabled(recycleView, false)
        return mWrapped!!.onCreateActionMode(mode, menu)
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return mWrapped!!.onPrepareActionMode(mode, menu)
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return mWrapped!!.onActionItemClicked(mode, item)
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        mOnMultiSelectionToggleListener?.onCloseMultiChoiceMode()
        ViewCompat.setNestedScrollingEnabled(recycleView, true)
        mWrapped!!.onDestroyActionMode(mode)
//        mActionMode = null
        // Ending selection mode means deselecting everything.
//        clearSelection()
    }

    override fun onItemCheckedStateChanged(mode: ActionMode,
                                           position: Int, checked: Boolean) {
        mWrapped!!.onItemCheckedStateChanged(mode, position, checked)
        // If there are no items selected we no longer need the selection mode.
//        if (getSelectedItemCount() == 0) {
//            mode.finish()
//        }
    }
}