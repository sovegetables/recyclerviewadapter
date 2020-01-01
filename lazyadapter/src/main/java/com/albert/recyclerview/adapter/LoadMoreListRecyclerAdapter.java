package com.albert.recyclerview.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.List;

/**
 * Created by albert on 2016/9/10.
 *
 * LoadMoreListRecyclerAdapter support StaggeredGridLayoutManager and LinearLayoutManager,
 * but not support GridLayoutManager.
 */
public abstract class LoadMoreListRecyclerAdapter<T> extends HeaderAndFooterListRecyclerAdapter<T>{

	private OnLoadMoreListener mOnLoadMoreListener;
	private boolean mIsLoading;
	private LoadMoreHelper mLoadMoreHelper;
	private @LayoutRes int mLoadMoreLayoutRes;
	private final ArrayMap<Class<? extends RecyclerView.LayoutManager>, LoadMoreHelper> mLoadMoreHelperMap = new ArrayMap<>();

	public LoadMoreListRecyclerAdapter(Context context) {
		this(context, null);
	}

	public LoadMoreListRecyclerAdapter(Context context, List<T> data) {
		super(context, data);
		mLoadMoreHelperMap.put(StaggeredGridLayoutManager.class, new StaggeredGridLayoutManagerLoadMoreHelper());
		mLoadMoreHelperMap.put(LinearLayoutManager.class, new LinearLayoutManagerLoadMoreHelper());
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		this.mOnLoadMoreListener = onLoadMoreListener;
	}

	public void setLoadMoreLayout(@LayoutRes int loadMoreLayoutRes) {
		this.mLoadMoreLayoutRes = loadMoreLayoutRes;
	}

	public void finishedLoadMore(){
		mIsLoading = false;
		removeFooter();
	}

	@Override
	public void addFooter(Object footerObj, @LayoutRes int footerLayoutResId, OnBindFooterViewListener onBindFooterViewListener) {
		// not support
	}

	@Override
	public void addFooter(Object footerObj, @NonNull View footer, OnBindFooterViewListener onBindFooterViewListener) {
		// not support
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		if(mOnLoadMoreListener != null) {
			mLoadMoreHelper = mLoadMoreHelperMap.get(recyclerView.getLayoutManager().getClass());
			if(mLoadMoreHelper != null) {
				mLoadMoreHelper.attachRecyclerView(recyclerView);
				recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
					@Override
					public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
						mLoadMoreHelper.onScrollStateChanged(recyclerView, newState);
					}

					@Override
					public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
						mLoadMoreHelper.onScrolled(recyclerView, dx, dy);
						if (!mIsLoading && mLoadMoreHelper.isLastPosition()) {
							mIsLoading = true;
							LoadMoreListRecyclerAdapter.super.addFooter(null, mLoadMoreLayoutRes, null);
							if (mOnLoadMoreListener != null) {
								mOnLoadMoreListener.onLoadMore(LoadMoreListRecyclerAdapter.this);
							}
						}
					}
				});
			}
		}
	}

}
