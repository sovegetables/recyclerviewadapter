package com.albert.recyclerview.listener;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.View;

import com.albert.recyclerview.adapter.XOLazyRecyclerViewHolder;

/**
 * Created by albert on 2016/9/3.
 */
public interface Header {

	void addHeader(@LayoutRes int headerLayoutResId, Header.OnBindHeaderViewListener onBindHeaderViewListener);

	void addHeader(@NonNull View header, Header.OnBindHeaderViewListener onBindHeaderViewListener);

	void addHeader(Object headerObj, @NonNull View header, Header.OnBindHeaderViewListener onBindHeaderViewListener);

	void addHeader(Object headerObj, @LayoutRes int headerLayoutResId, Header.OnBindHeaderViewListener onBindHeaderViewListener);

	void removeHeader();

	interface OnBindHeaderViewListener{
	    void onBindHeaderViewHolder(XOLazyRecyclerViewHolder holder, Object headerObj, int position);
	}
}
