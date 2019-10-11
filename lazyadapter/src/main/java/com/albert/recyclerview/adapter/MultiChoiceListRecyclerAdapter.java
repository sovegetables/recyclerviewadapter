package com.albert.recyclerview.adapter;

import android.content.Context;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.albert.recyclerview.listener.OnItemClickListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by albert on 2016/4/9.
 */
public abstract class MultiChoiceListRecyclerAdapter<T> extends HeaderAndFooterListRecyclerAdapter<T> {

    private MultiChoiceModeWrapper mMultiChoiceModeCallback;
    private OnMultiSelectionToggleListener mOnMultiSelectionToggleListener;
    private ActionMode mActionMode;

    @IntDef({CHOICE_MODE_NONE, CHOICE_MODE_SINGLE_MODAL, CHOICE_MODE_MULTIPLE_MODAL})
    @Retention(RetentionPolicy.SOURCE)
    private @interface ChoiceMode {}

    protected static final int CHOICE_MODE_NONE = 0;
    protected static final int CHOICE_MODE_SINGLE_MODAL = 1;
    protected static final int CHOICE_MODE_MULTIPLE_MODAL = 2;

    private SparseBooleanArray mItemSelectedMap;
    private int mCheckedItemCount;

    public MultiChoiceListRecyclerAdapter(Context context) {
        this(context, null);
    }

    @SuppressWarnings("unused")
    private void forceStartingActionMode() {
        RecyclerView recyclerView = getRecyclerView();
        if(recyclerView != null){
            mActionMode = recyclerView.startActionMode(getMultiChoiceModeCallback());
            notifyDataSetChanged();
        }
    }

    public MultiChoiceListRecyclerAdapter(Context context, List<T> data) {
        super(context, data);
        if(getChoiceMode() != CHOICE_MODE_NONE){
            mItemSelectedMap = new SparseBooleanArray(0);
            mCheckedItemCount = 0;
        }
    }

    protected abstract @ChoiceMode int getChoiceMode();

    @SuppressWarnings("unused")
    public void finishChoiceMode() {
        if(mActionMode != null){
            mActionMode.finish();
        }
    }

    public int getSelectedItemCount() {
        return mCheckedItemCount;
    }

    private void clearSelection() {
        if(mItemSelectedMap != null){
            mItemSelectedMap.clear();
            mCheckedItemCount = 0;
        }
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void setOnMultiChoiceToggleListener(OnMultiSelectionToggleListener onMultiSelectionToggleListener) {
        this.mOnMultiSelectionToggleListener = onMultiSelectionToggleListener;
    }

    @Override @SuppressWarnings("unchecked")
    protected void onBindDataViewHolder(XOLazyRecyclerViewHolder holder, final T item, final int dataIndex, final int position) {
        if(getChoiceMode() != CHOICE_MODE_NONE) {
            holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    boolean isEventConsumed = false;
                    if (mActionMode != null) {
                        mActionMode.finish();
                        isEventConsumed = true;
                    } else if (getChoiceMode() != CHOICE_MODE_NONE &&
                            mMultiChoiceModeCallback != null) {
                        final int preItemCount = getItemCount();
                        mActionMode = view.startActionMode(mMultiChoiceModeCallback);
                        notifyDataSetChanged();
                        setItemSelected(dataIndex, getPressedPositionIfRemoveHeaderWhenOpenChoiceMode(preItemCount, position), true);
                        isEventConsumed = true;
                    }
                    return isEventConsumed;
                }
            });
        }

        final OnItemClickListener onItemClickListener = getOnItemClickListener();
        if(onItemClickListener != null || (getChoiceMode() != CHOICE_MODE_NONE && mMultiChoiceModeCallback != null) ){
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionMode != null) {
                        boolean oldValue = mItemSelectedMap.get(dataIndex);
                        setItemSelected(dataIndex, position, !oldValue);
                    }else if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, item, position);
                    }
                }
            });
        }

        if(mActionMode != null) {
            holder.itemView.setSelected(mItemSelectedMap.get(dataIndex));
        }
        onBindMultiChoiceViewHolder(holder, item, dataIndex, position);
    }

    protected abstract void onBindMultiChoiceViewHolder(XOLazyRecyclerViewHolder holder, T item, int dataIndex, int position);

    protected void setItemSelected(int dataIndex, int pressedPosition, boolean isSelected) {
        boolean oldValue = mItemSelectedMap.get(dataIndex);
        switch (getChoiceMode()) {
            case CHOICE_MODE_MULTIPLE_MODAL:
                mItemSelectedMap.put(dataIndex, isSelected);
                if (oldValue != isSelected) {
                    if (isSelected) {
                        mCheckedItemCount++;
                    } else {
                        mCheckedItemCount--;
                    }
                    notifyItemChanged(pressedPosition);
                }
                break;
            case CHOICE_MODE_SINGLE_MODAL:
                mItemSelectedMap.clear();
                mItemSelectedMap.put(dataIndex, isSelected);
                if (oldValue != isSelected) {
                    if (isSelected) {
                        mCheckedItemCount = 1;
                    } else {
                        mCheckedItemCount = 0;
                    }
                    notifyDataSetChanged();
                }
                break;
            case CHOICE_MODE_NONE:
                break;
        }
        if (mActionMode != null) {
            mMultiChoiceModeCallback.onItemCheckedStateChanged(mActionMode,
                    pressedPosition, isSelected);
        }
    }

    private int getPressedPositionIfRemoveHeaderWhenOpenChoiceMode(int preItemCount, int preSelectedPosition){
        final int currentItemCount = getItemCount();
        if(currentItemCount != preItemCount && getHeaderCount() == 0){
            if(preSelectedPosition > 0){
                preSelectedPosition -= 1;
            }
        }
        return preSelectedPosition;
    }

    @SuppressWarnings("unused")
    public int[] getSelectedDataItemPositions(){
        SparseBooleanArray sparseBooleanArray = getSelectStates();
        int len =  sparseBooleanArray == null? 0 : sparseBooleanArray.size();
        int[] checkedItemPositions = new int[len];
        for (int i = 0;i < len; i++){
            if(sparseBooleanArray.valueAt(i)) {
                checkedItemPositions[i] = sparseBooleanArray.keyAt(i);
            }
        }
        return  checkedItemPositions;
    }

    public List<T> getSelectedDataItems(){
        final List<T> itemData = getData();
        SparseBooleanArray sparseBooleanArray = getSelectStates();
        int len =  sparseBooleanArray == null? 0 : sparseBooleanArray.size();
        List<T> checkedItems = new ArrayList<>(len);
        for (int i = 0;i < len && itemData != null; i++){
            if(sparseBooleanArray.valueAt(i)) {
                checkedItems.add(itemData.get(sparseBooleanArray.keyAt(i)));
            }
        }
        return checkedItems;
    }

    @SuppressWarnings("unused")
    public boolean isChoiceModeEnabled(){
        return mActionMode != null;
    }

    @Nullable
    protected SparseBooleanArray getSelectStates() {
        return getChoiceMode() != CHOICE_MODE_NONE? mItemSelectedMap : null;
    }

    public interface MultiChoiceModeListener extends ActionMode.Callback {
        void onItemCheckedStateChanged(ActionMode mode, int position, boolean checked);
    }

    protected MultiChoiceModeWrapper getMultiChoiceModeCallback() {
        return mMultiChoiceModeCallback;
    }

    public void setMultiChoiceModeListener(MultiChoiceModeListener listener) {
        if (mMultiChoiceModeCallback == null) {
            mMultiChoiceModeCallback = new MultiChoiceModeWrapper();
        }
        mMultiChoiceModeCallback.setWrapped(listener);
    }

    private void setRecyclerViewNestedScrollingEnabled(boolean enabled) {
        RecyclerView recyclerView = getRecyclerView();
        if(recyclerView != null){
            ViewCompat.setNestedScrollingEnabled(recyclerView, enabled);
        }
    }

    private class MultiChoiceModeWrapper implements MultiChoiceModeListener {
        private MultiChoiceModeListener mWrapped;

        public void setWrapped(MultiChoiceModeListener wrapped) {
            mWrapped = wrapped;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            if(mOnMultiSelectionToggleListener != null){
                mOnMultiSelectionToggleListener.onOpenMultiChoiceMode();
            }
            setRecyclerViewNestedScrollingEnabled(false);
            return mWrapped.onCreateActionMode(mode, menu);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return mWrapped.onPrepareActionMode(mode, menu);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return mWrapped.onActionItemClicked(mode, item);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(mOnMultiSelectionToggleListener != null){
                mOnMultiSelectionToggleListener.onCloseMultiChoiceMode();
            }
            setRecyclerViewNestedScrollingEnabled(true);
            mWrapped.onDestroyActionMode(mode);
            mActionMode = null;
            // Ending selection mode means deselecting everything.
            clearSelection();
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, boolean checked) {
            mWrapped.onItemCheckedStateChanged(mode, position, checked);
            // If there are no items selected we no longer need the selection mode.
            if (getSelectedItemCount() == 0) {
                mode.finish();
            }
        }
    }

    public interface OnMultiSelectionToggleListener {
        void onOpenMultiChoiceMode();
        void onCloseMultiChoiceMode();
    }
}
