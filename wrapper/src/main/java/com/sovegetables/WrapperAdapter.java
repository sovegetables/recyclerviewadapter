package com.sovegetables;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams;

@SuppressWarnings("unused")
public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = "WrapperAdapter";
    private static final int TYPE_SUFFIX = 1000000;

    private final List<RecyclerView.Adapter> mWrapperAdapters = new ArrayList<>();
    private final ViewItemContainer mHeaderViews = new ViewItemContainer();
    private final ViewItemContainer mFooterViews = new ViewItemContainer();

    public WrapperAdapter(RecyclerView.Adapter adapter){
        if(adapter == null){
            throw new IllegalArgumentException("adapter can not be null!");
        }
        mWrapperAdapters.add(adapter);
        registerWrapperAdapters();
    }

    public WrapperAdapter(List<RecyclerView.Adapter> wrapperAdapters) {
        if(wrapperAdapters == null){
            throw new IllegalArgumentException("wrapperAdapters can not be null!");
        }
        mWrapperAdapters.addAll(wrapperAdapters);
        registerWrapperAdapters();
    }

    private void registerWrapperAdapters() {
        for (RecyclerView.Adapter a: mWrapperAdapters) {
            a.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    positionStart += getHeaderItemCount();
                    notifyItemRangeChanged(positionStart, itemCount);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    positionStart += getHeaderItemCount();
                    notifyItemRangeChanged(positionStart, itemCount, payload);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    positionStart += getHeaderItemCount();
                    notifyItemRangeInserted(positionStart, itemCount);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    positionStart += getHeaderItemCount();
                    notifyItemRangeRemoved(positionStart, itemCount);
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    fromPosition += getHeaderItemCount();
                    toPosition += getHeaderItemCount();
                    notifyItemMoved(fromPosition, toPosition);
                }
            });
        }
    }

    private int getHeaderItemCount() {
        return mHeaderViews.size();
    }

    private static class ViewItemContainer extends ArrayList<ViewItem>{

        int indexOfView(View view) {
            for (int i = 0, len = size(); i < len;i++){
                if(view == get(i).view){
                    return i;
                }
            }
            return -1;
        }

        int indexOfViewType(int viewType) {
            for (int i = 0, len = size(); i < len;i++){
                if(viewType == get(i).viewType){
                    return i;
                }
            }
            return -1;
        }

        View getViewByViewType(int viewType) {
            ViewItem item;
            for (int i = 0, len = size(); i < len;i++){
                item = get(i);
                if(viewType == item.viewType){
                    return item.view;
                }
            }
            return null;
        }
    }

    private static class ViewItem{
        int viewType;
        View view;

        ViewItem(int viewType, View view) {
            this.viewType = viewType;
            this.view = view;
        }
    }

    @Override @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //check whether header
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.getViewByViewType(viewType);
            return createHeaderOrFooterViewHolder(headerView);
        }
        //check whether footer
        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.getViewByViewType(viewType);
            return createHeaderOrFooterViewHolder(footerView);
        }
        // realViewType = adapter's viewType + indexOf * 1000000
        int index = viewType / TYPE_SUFFIX;
        int restoreViewType = viewType % TYPE_SUFFIX;
        return mWrapperAdapters.get(index).onCreateViewHolder(parent, restoreViewType);
    }


    private RecyclerView.ViewHolder createHeaderOrFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {};
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return mHeaderViews.get(position).viewType;
        }
        if (isFooterPosition(position)) {
            position = position - mHeaderViews.size() - getContentItemCount();
            return mFooterViews.get(position).viewType;
        }
        AdapterItem adapterItem = getRealAdapter(position);
        if(adapterItem != null){
            int indexOf = mWrapperAdapters.indexOf(adapterItem.adapter);
            //realViewType = adapter's viewType + indexOf * 1000000
            return adapterItem.adapter.getItemViewType(adapterItem.position) + indexOf * TYPE_SUFFIX;
        }else {
            return super.getItemViewType(position);
        }
    }

    private static class AdapterItem{
        RecyclerView.Adapter adapter;
        int position;

        AdapterItem(RecyclerView.Adapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }
    }

    @Nullable
    private AdapterItem getRealAdapter(int position) {
        if(position == RecyclerView.NO_POSITION){
            return null;
        }
        position = position - mHeaderViews.size();
        AdapterItem adapterItem = null;
        int startPosition = 0;
        int endPosition = 0;
        for (RecyclerView.Adapter a: mWrapperAdapters){
            int itemCount = a.getItemCount();
            if(itemCount > 0){
                endPosition += itemCount;
                if(position < endPosition){
                    int pos = position - startPosition;
                    adapterItem = new AdapterItem(a, pos);
                    break;
                }
                startPosition = endPosition;
            }
        }
        return adapterItem;
    }

    @Override @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }
        AdapterItem adapterItem = getRealAdapter(position);
        if(adapterItem != null){
            adapterItem.adapter.onBindViewHolder(holder, adapterItem.position, payloads);
        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }
        AdapterItem adapterItem = getRealAdapter(position);
        if(adapterItem != null){
            adapterItem.adapter.onBindViewHolder(holder, adapterItem.position);
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = getContentItemCount();
        itemCount += mHeaderViews.size() + mFooterViews.size();
        return itemCount;
    }

    @Override
    public long getItemId(int position) {
        if (isHeaderPosition(position) ) {
            return mHeaderViews.get(position).hashCode();
        }
        if(isFooterPosition(position)){
            return mFooterViews.get(position).hashCode();
        }
        AdapterItem adapterItem = getRealAdapter(position);
        if(adapterItem != null){
            return adapterItem.adapter.getItemId(position);
        }
        return super.getItemId(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        for (RecyclerView.Adapter a: mWrapperAdapters){
            a.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position == RecyclerView.NO_POSITION){
            return;
        }
        if (!isFooterOrHeader(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof LayoutParams) {
                LayoutParams p = (LayoutParams) lp;
                p.setFullSpan(true);
            }
        } else {
            AdapterItem adapterItem = getRealAdapter(position);
            if(adapterItem != null){
                adapterItem.adapter.onViewAttachedToWindow(holder);
            }
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position == RecyclerView.NO_POSITION){
            return;
        }
        if (isFooterOrHeader(position)){
            AdapterItem adapterItem = getRealAdapter(position);
            if(adapterItem != null){
                adapterItem.adapter.onViewRecycled(holder);
            }
        }
    }

    private boolean isFooterOrHeader(int position) {
        return !isHeaderView(position) && !isFooterView(position);
    }

    @Override @SuppressWarnings("unchecked")
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position == RecyclerView.NO_POSITION){
            return false;
        }
        if (isFooterOrHeader(position)){
            AdapterItem adapterItem = getRealAdapter(position);
            if(adapterItem != null){
                return adapterItem.adapter.onFailedToRecycleView(holder);
            }
        }
        return false;
    }

    @Override @SuppressWarnings("unchecked")
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position == RecyclerView.NO_POSITION){
            return;
        }
        if (isFooterOrHeader(position)){
            AdapterItem adapterItem = getRealAdapter(position);
            if(adapterItem != null){
                adapterItem.adapter.onViewDetachedFromWindow(holder);
            }
        }
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }


    @Override
    public void setHasStableIds(boolean hasStableIds) {
        for (RecyclerView.Adapter a: mWrapperAdapters){
            a.setHasStableIds(hasStableIds);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        for (RecyclerView.Adapter a: mWrapperAdapters){
            a.onDetachedFromRecyclerView(recyclerView);
        }
    }

    private boolean isHeaderView(int position) {
        return position >= 0 && position < getHeaderItemCount();
    }

    private boolean isFooterView(int position) {
        return position >= getHeaderItemCount() + getContentItemCount();
    }

    private int getContentItemCount() {
        int itemCount = 0;
        for (RecyclerView.Adapter a: mWrapperAdapters){
            itemCount += a.getItemCount();
        }
        return itemCount;
    }

    public void addHeaderView(View view) {
        int position = mHeaderViews.indexOfView(view);
        if (position < 0) {
            //can't repeatedly add header
            mHeaderViews.add(new ViewItem(view.hashCode(), view));
            try {
                notifyItemInserted(mHeaderViews.size() - 1);
//                notifyDataSetChanged();
            } catch (Exception ignored) {
                notifyDataSetChanged();
            }
        }
    }

    public void addFooterView(View view) {
        int position = mFooterViews.indexOfView(view);
        if (position < 0) {
            //can't repeatedly add footer
            mFooterViews.add(new ViewItem(view.hashCode(), view));
            try {
                notifyItemInserted(getItemCount() - 1);
//                notifyDataSetChanged();
            }catch (Exception ignored){
                notifyDataSetChanged();
            }
        }
    }

    private void removeAllFooterView(){
        if(mFooterViews.size() == 0){
            return;
        }
        mFooterViews.clear();
        try {
//            notifyItemRangeRemoved(getHeaderItemCount() + getContentItemCount(), mFooterViews.size());
            notifyDataSetChanged();
        } catch (Exception e) {
            notifyDataSetChanged();
        }
    }

    private void removeAllHeaderView(){
        if(mHeaderViews.size() == 0){
            return;
        }
        mHeaderViews.clear();
        try {
//            notifyItemRangeRemoved(0, mHeaderViews.size());
            notifyDataSetChanged();
        } catch (Exception e) {
            notifyDataSetChanged();
        }
    }

    public boolean hasFooter(){
        return mFooterViews.size() > 0;
    }

    public boolean hasHeader(){
        return mHeaderViews.size() > 0;
    }

    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfView(view);
        if (index < 0) {
            return;
        }
        mHeaderViews.remove(index);
        try {
            notifyItemRemoved(index);
        } catch (Exception e) {
            notifyDataSetChanged();
        }
    }

    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfView(view);
        if (index < 0) {
            return;
        }
        mFooterViews.remove(index);
        try {
            notifyItemRemoved(getHeaderItemCount() + getContentItemCount() + index);
        } catch (Exception e) {
            notifyDataSetChanged();
        }
    }

    private boolean isFooterViewType(int viewType) {
        int footerPosition = mFooterViews.indexOfViewType(viewType);
        return footerPosition >= 0;
    }

    private boolean isHeaderViewType(int viewType) {
        int headerPosition = mHeaderViews.indexOfViewType(viewType);
        return headerPosition >= 0;
    }

    private boolean isFooterPosition(int position) {
        return position >= (mHeaderViews.size() + getContentItemCount());
    }

    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }

    public List<RecyclerView.Adapter> getWrapAdapter() {
        return mWrapperAdapters;
    }

}
