package com.albert.recyclerview.adapter;

import android.content.Context;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ViewGroup;

import com.albert.recyclerview.help.LinearLayoutManagerCalculator;
import com.albert.recyclerview.help.StaggeredGridLayoutManagerCalculator;
import com.albert.recyclerview.listener.ItemCalculator;
import com.albert.recyclerview.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albert on 2016/4/7.
 */
public abstract class BaseRecyclerAdapter<TYPE, ITEM> extends RecyclerView.Adapter<XOLazyRecyclerViewHolder>{

    private static final String TAG = "BaseRecyclerAdapter";

    private Context mContext;
    private TYPE mData;
    private List<RecyclerViewType> mRecyclerViewTypes = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private OnItemClickListener<ITEM> mOnItemClickListener;

    public BaseRecyclerAdapter(Context context, @Nullable TYPE data){
        mContext = context;
        mData = data;
        buildRecyclerViewType(new RecyclerViewTypeBuilder());
    }

    public BaseRecyclerAdapter(Context context){
        this(context, null);
    }

    public void setOnItemClickListener(OnItemClickListener<ITEM> listener){
        mOnItemClickListener = listener;
    }

    public OnItemClickListener<ITEM> getOnItemClickListener() {
        return mOnItemClickListener;
    }

    protected final RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public final Context getContext(){
        return mContext;
    }

    public void setData(TYPE data){
        mData = data;
        notifyDataSetChanged();
    }

    public void clearData() {
        mData = null;
        notifyDataSetChanged();
    }

    @Nullable
    public final TYPE getData(){
        return mData;
    }

    @Override @CallSuper
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        if(recyclerView != null){
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    switch (newState){
                        case RecyclerView.SCROLL_STATE_IDLE:
                            break;
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                            break;
                        case RecyclerView.SCROLL_STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    ItemCalculator itemCalculator = createItemCalculator(recyclerView.getLayoutManager());
                    int firstVisiblePos = itemCalculator.findFirstVisibleItemPosition();
                    int lastVisiblePos = itemCalculator.findLastVisibleItemPosition();
                    Log.i(TAG, "firstVisiblePos: " + firstVisiblePos + " lastVisiblePos:" + lastVisiblePos);

                    if(firstVisiblePos < 0 || lastVisiblePos < 0 || lastVisiblePos < firstVisiblePos){
                       return;
                    }

                    int count = getItemCount();
                    for (int i = 0; i < count; i++) {
                        for (RecyclerViewType recyclerViewType:mRecyclerViewTypes) {
                            if(recyclerViewType.isMatchViewType(i)){
                                if(lastVisiblePos >= i && i >= firstVisiblePos){
                                    recyclerViewType.onInScreen(i);
                                }else {
                                    recyclerViewType.onOutScreen(i);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private ItemCalculator createItemCalculator(RecyclerView.LayoutManager manager){
        ItemCalculator itemCalculator = null;
        if(manager instanceof LinearLayoutManager){
            itemCalculator = new LinearLayoutManagerCalculator(((LinearLayoutManager) manager));
        }else if(manager instanceof StaggeredGridLayoutManager){
            itemCalculator = new StaggeredGridLayoutManagerCalculator(((StaggeredGridLayoutManager) manager));
        }
        return itemCalculator;
    }

    @Override
    public XOLazyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mRecyclerViewTypes != null){
            for (RecyclerViewType recyclerViewType:mRecyclerViewTypes) {
                if(recyclerViewType.getItemViewType() == viewType){
                    return recyclerViewType.onCreateViewHolder(parent);
                }
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(XOLazyRecyclerViewHolder holder, int position) {
        if(mRecyclerViewTypes != null){
            for (RecyclerViewType recyclerViewType:mRecyclerViewTypes) {
                if(recyclerViewType.isMatchViewType(position)){
                    recyclerViewType.onBindViewHolder(holder, position);
                    break;
                }
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(XOLazyRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        for (RecyclerViewType recyclerViewType:mRecyclerViewTypes) {
            recyclerViewType.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(XOLazyRecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        for (RecyclerViewType recyclerViewType:mRecyclerViewTypes) {
            recyclerViewType.onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public void onViewRecycled(XOLazyRecyclerViewHolder holder) {
        super.onViewRecycled(holder);
        for (RecyclerViewType recyclerViewType:mRecyclerViewTypes) {
            recyclerViewType.onViewRecycled(holder);
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if(mRecyclerViewTypes != null){
            for (RecyclerViewType recyclerViewType:mRecyclerViewTypes) {
                if(recyclerViewType.isMatchViewType(position)){
                    return recyclerViewType.getItemViewType();
                }
            }
        }
        return 0;
    }

    public abstract ITEM getItemByPosition(int position);

    protected abstract void buildRecyclerViewType(RecyclerViewTypeBuilder builder);

    protected final class RecyclerViewTypeBuilder {

        private ArrayMap<Integer, RecyclerViewType> mItemViewTypeMap;
        private RecyclerViewTypeBuilder(){}

        protected RecyclerViewTypeBuilder addRecyclerViewType(@NonNull RecyclerViewType recyclerViewType){
            if(mItemViewTypeMap == null){
                mItemViewTypeMap = new ArrayMap<>();
            }
            if(mItemViewTypeMap.get(recyclerViewType.getItemViewType()) != null){
                throw new IllegalArgumentException(recyclerViewType.toString() + "- this value from getItemViewType() had already existed.");
            }
            mRecyclerViewTypes.add(recyclerViewType);
            mItemViewTypeMap.put(recyclerViewType.getItemViewType(), recyclerViewType);
            return this;
        }
    }
}
