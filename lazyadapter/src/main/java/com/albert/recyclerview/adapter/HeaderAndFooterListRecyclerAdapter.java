package com.albert.recyclerview.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import android.view.View;
import android.view.ViewGroup;

import com.albert.recyclerview.help.RecyclerViewAdapterUtils;
import com.albert.recyclerview.listener.Footer;
import com.albert.recyclerview.listener.Header;

import java.util.List;

/**
 * Created by albert on 2016/4/8.
 */
public abstract class HeaderAndFooterListRecyclerAdapter<T> extends BaseRecyclerAdapter<List<T>, T>
implements Header, Footer{

    private static final int TYPE_HEADER = 11;
    private static final int TYPE_FOOTER = 12;
    private static final int TYPE_DATA = 13;

    private int mHeaderLayoutResId;
    private int mFooterLayoutResId;
    private View mViewHeader;
    private View mViewFooter;
    private Object mHeaderObj;
    private Object mFooterObj;

    private Footer.OnBindFooterViewListener mOnBindFooterViewListener;
    private Header.OnBindHeaderViewListener mOnBindHeaderViewListener;

    public HeaderAndFooterListRecyclerAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public HeaderAndFooterListRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected void buildRecyclerViewType(RecyclerViewTypeBuilder builder) {
        builder.addRecyclerViewType(new DataRecyclerViewType(this))
                .addRecyclerViewType(new HeaderRecyclerViewType(this))
                .addRecyclerViewType(new FooterRecyclerViewType(this));
    }

    public int getHeaderCount(){
        return mHeaderObj == null ? 0 : 1;
    }

    public int getFooterCount(){
        return mFooterObj == null ? 0 : 1;
    }

    public int getDataCount(){
        return getData() == null? 0 : getData().size();
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + getDataCount();
    }

    @UiThread @SuppressWarnings("unused")
    public void insertData(@Nullable T data, int position){
        if(getData() != null && data != null) {
            getData().add(position, data);
            notifyItemInserted(getHeaderCount() + position);
        }
    }

    @UiThread
    public void insertData(@Nullable List<T> data){
        if(getData() != null && data != null) {
            int lastDataPos = getDataCount() + getHeaderCount();
            getData().addAll(data);
            notifyItemRangeChanged(lastDataPos, data.size());
        }
    }

    public void addHeader(@LayoutRes int headerLayoutResId, Header.OnBindHeaderViewListener onBindHeaderViewListener){
        addHeader(null, headerLayoutResId, onBindHeaderViewListener);
    }

    public void addHeader(@NonNull View header, Header.OnBindHeaderViewListener onBindHeaderViewListener){
        addHeader(null, header, onBindHeaderViewListener);
    }

    public void addHeader(Object headerObj, @NonNull View header, Header.OnBindHeaderViewListener onBindHeaderViewListener){
        mViewHeader = header;
        addHeaderObj(headerObj, onBindHeaderViewListener);
    }

    public void addHeader(Object headerObj, @LayoutRes int headerLayoutResId, Header.OnBindHeaderViewListener onBindHeaderViewListener){
        mHeaderLayoutResId = headerLayoutResId;
        addHeaderObj(headerObj, onBindHeaderViewListener);
    }

    private void addHeaderObj(Object headerObj, Header.OnBindHeaderViewListener onBindHeaderViewListener){
        if(headerObj != null){
            mHeaderObj = headerObj;
        }else {
            mHeaderObj = new Object();
        }
        this.mOnBindHeaderViewListener = onBindHeaderViewListener;
        notifyItemInserted(0);
    }

    public void removeHeader(){
        if(mHeaderObj != null){
            notifyItemRemoved(0);
            mHeaderObj = null;
        }
    }

    public void removeFooter() {
        if(mFooterObj != null) {
            notifyItemRemoved(getItemCount() - 1);
            mFooterObj = null;
        }
    }

    public void addFooter(@LayoutRes int footerLayoutResId, Footer.OnBindFooterViewListener onBindFooterViewListener){
        addFooter(null, footerLayoutResId, onBindFooterViewListener);
    }

    public void addFooter(@NonNull View footer, Footer.OnBindFooterViewListener onBindFooterViewListener){
        addFooter(null, footer, onBindFooterViewListener);
    }

    public void addFooter(Object footerObj, @NonNull View footer, Footer.OnBindFooterViewListener onBindFooterViewListener){
        mViewFooter = footer;
        addFooterObj(footerObj, onBindFooterViewListener);
    }

    public void addFooter(Object footerObj, @LayoutRes int footerLayoutResId, Footer.OnBindFooterViewListener onBindFooterViewListener){
        mFooterLayoutResId = footerLayoutResId;
        addFooterObj(footerObj, onBindFooterViewListener);
    }

    private void addFooterObj(Object footerObj, Footer.OnBindFooterViewListener onBindFooterViewListener){
        if(footerObj != null){
            mFooterObj = footerObj;
        }else {
            mFooterObj = new Object();
        }
        this.mOnBindFooterViewListener = onBindFooterViewListener;
        notifyItemInserted(getItemCount() - 1);
    }

    private void onBindFooterViewHolder(XOLazyRecyclerViewHolder holder, Object footerObj, int position) {
        if(mOnBindFooterViewListener != null){
            mOnBindFooterViewListener.onBindFooterViewHolder(holder, footerObj, position);
        }
    }

    private void onBindHeaderViewHolder(XOLazyRecyclerViewHolder holder, Object headerObj, int position) {
        if(mOnBindHeaderViewListener != null){
            mOnBindHeaderViewListener.onBindHeaderViewHolder(holder, headerObj, position);
        }
    }

    @LayoutRes
    protected abstract int getItemLayoutRes();

    protected abstract void onBindDataViewHolder(XOLazyRecyclerViewHolder holder, T item, int dataIndex, int position);

    private static class HeaderRecyclerViewType extends RecyclerViewType<HeaderAndFooterListRecyclerAdapter>{

        public HeaderRecyclerViewType(HeaderAndFooterListRecyclerAdapter adapter) {
            super(adapter);
        }

        @Override
        protected boolean isMatchViewType(int position) {
            return getAdapter().mHeaderObj != null && position == 0;
        }

        @Override
        protected int getItemViewType() {
            return TYPE_HEADER;
        }

        @Override
        protected int getItemLayoutRes() {
            return getAdapter().mHeaderLayoutResId;
        }

        @Override
        protected boolean enableItemClickListener() {
            return false;
        }

        @Override
        protected void onBindViewHolder(XOLazyRecyclerViewHolder holder, Object item, int position) {
            RecyclerViewAdapterUtils.fuLLSpan(holder);
            getAdapter().onBindHeaderViewHolder(holder, getAdapter().mHeaderObj, position);
        }

        @Override
        protected XOLazyRecyclerViewHolder onCreateViewHolder(ViewGroup parent) {
            View header = getAdapter().mViewHeader;
            if(getAdapter().mHeaderLayoutResId != 0 && header == null){
                return super.onCreateViewHolder(parent);
            }
            return new XOLazyRecyclerViewHolder(header);
        }

    }

    private static class FooterRecyclerViewType extends RecyclerViewType<HeaderAndFooterListRecyclerAdapter>{

        public FooterRecyclerViewType(HeaderAndFooterListRecyclerAdapter adapter) {
            super(adapter);
        }

        @Override
        protected boolean isMatchViewType(int position) {
            return getAdapter().mFooterObj != null && position == getAdapter().getItemCount() - 1;
        }

        @Override
        protected int getItemViewType() {
            return TYPE_FOOTER;
        }

        @Override
        protected int getItemLayoutRes() {
            return getAdapter().mFooterLayoutResId;
        }

        @Override
        protected void onBindViewHolder(XOLazyRecyclerViewHolder holder, Object item, int position) {
            RecyclerViewAdapterUtils.fuLLSpan(holder);
            getAdapter().onBindFooterViewHolder(holder, getAdapter().mFooterObj, position);
        }

        @Override
        protected XOLazyRecyclerViewHolder onCreateViewHolder(ViewGroup parent) {
            View footer = getAdapter().mViewFooter;
            if(getAdapter().mFooterLayoutResId != 0 && footer == null){
                return super.onCreateViewHolder(parent);
            }
            return new XOLazyRecyclerViewHolder(footer);
        }

        @Override
        protected boolean enableItemClickListener() {
            return false;
        }
    }

    private static class DataRecyclerViewType extends RecyclerViewType<HeaderAndFooterListRecyclerAdapter>{

        public DataRecyclerViewType(HeaderAndFooterListRecyclerAdapter adapter) {
            super(adapter);
        }

        @Override
        protected boolean isMatchViewType(int position) {
            return position >= getAdapter().getHeaderCount()
                    && position < getAdapter().getItemCount() - getAdapter().getFooterCount();
        }

        @Override
        protected int getItemViewType() {
            return TYPE_DATA;
        }

        @Override
        protected int getItemLayoutRes() {
            return getAdapter().getItemLayoutRes();
        }

        @Override @SuppressWarnings("unchecked")
        protected void onBindViewHolder(XOLazyRecyclerViewHolder holder, Object item, int position) {
            final int dataIndex = position - getAdapter().getHeaderCount();
            getAdapter().onBindDataViewHolder(holder, item, dataIndex, position);
        }

    }

    @Override
    public T getItemByPosition(int position) {
        if(position >= getHeaderCount()) {
            final int dataIndex = position - getHeaderCount();
            List<T> data = getData();
            if(data != null && data.size() > dataIndex && dataIndex >= 0) {
                return getData().get(dataIndex);
            }
        }
        return null;
    }
}
