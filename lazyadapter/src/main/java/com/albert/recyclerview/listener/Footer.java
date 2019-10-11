package com.albert.recyclerview.listener;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.View;

import com.albert.recyclerview.adapter.XOLazyRecyclerViewHolder;

/**
 * Created by albert on 2016/9/3.
 */
public interface Footer {

	void removeFooter();

	void addFooter(@LayoutRes int footerLayoutResId, Footer.OnBindFooterViewListener onBindFooterViewListener);

	void addFooter(@NonNull View footer, Footer.OnBindFooterViewListener onBindFooterViewListener);

	void addFooter(Object footerObj, @NonNull View header, Footer.OnBindFooterViewListener onBindFooterViewListener);

	void addFooter(Object headerObj, @LayoutRes int headerLayoutResId, Footer.OnBindFooterViewListener onBindFooterViewListener);

	interface OnBindFooterViewListener{
	    void onBindFooterViewHolder(XOLazyRecyclerViewHolder holder, Object headerObj, int position);
	}
}
