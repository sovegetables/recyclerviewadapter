package com.sovegetables.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdapter extends AbsListAdapter<SimpleItem> {

    private final Builder mBuilder;

    private final static int PAYLOAD_CHECK_BOX = 123;

    private SimpleAdapter(Builder builder){
        mBuilder = builder;
    }

    @Override
    public void setItems(List<SimpleItem> items) {
        super.setItems(items);
    }

    @Override
    protected void onCreateAdapterDelegates(AdapterDelegatesManager<List<SimpleItem>> delegatesManager) {
        ListAdapterDelegate<SimpleItem> delegate = new ListAdapterDelegate<SimpleItem>() {

            @Override
            protected void onHandlerItemClickEvent(CommonViewHolder holder) {
            }

            @Override
            protected void onViewCreated(ViewGroup parent, CommonViewHolder holder) {
                SimpleAdapter.this.onViewCreated(parent, holder);
            }

            @Override
            protected void onBindView(CommonViewHolder holder, SimpleItem t, int position) {
                SimpleAdapter.this.onBindView(holder, t, position);
            }

            @Override
            protected void onBindView(CommonViewHolder holder, SimpleItem t, int position, List payloads) {
                final OnItemClickListener<SimpleItem> onItemClickListener = getOnItemClickListener();
                if(onItemClickListener != null && t.selected()){
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SimpleItem item = getItemByTag(v);
                            int positionByTag = getPositionByTag(v);
                            if(item.enableCheckBoxMode()){
                                boolean checked = !item.checked();
                                item.setChecked(checked);
                                notifyItemChanged(positionByTag, PAYLOAD_CHECK_BOX);
                                SimpleItem.OnItemCheckedChangedListener listener = item.onItemCheckedChangedListener();
                                if(listener != null){
                                    listener.onChanged(checked);
                                }
                            }else {
                                onItemClickListener.onItemClick(v, item, positionByTag);
                            }
                        }
                    });
                }
                SimpleAdapter.this.onBindView(holder, t, position, payloads);
            }

            @Override
            protected int getLayoutRes() {
                return SimpleAdapter.this.getLayoutRes();
            }

            @Override
            protected boolean isForViewType(@NonNull List<SimpleItem> items, int position) {
                return true;
            }
        };
        delegatesManager.addDelegate(delegate);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    protected void onBindView(CommonViewHolder holder, SimpleItem simpleItem, int position, List payloads) {
        if(payloads != null && payloads.size() > 0 && payloads.get(0) instanceof Integer && ((Integer) payloads.get(0)) == PAYLOAD_CHECK_BOX){
            SwitchCompat switchSimple = holder.findViewById(R.id.switch_simple);
            switchSimple.setChecked(simpleItem.checked());
        }else {
            super.onBindView(holder, simpleItem, position, payloads);
        }
    }

    @Override
    protected void onBindView(CommonViewHolder holder, SimpleItem simpleItem, int position) {
        TextView tvSimpleName = holder.findViewById(R.id.tv_simple_name);
        tvSimpleName.setTextAppearance(tvSimpleName.getContext(), mBuilder.textStyle);
        ImageView ivRightArrow = holder.findViewById(R.id.iv_right_arrow);
        SwitchCompat switchSimple = holder.findViewById(R.id.switch_simple);
        ImageView ivIcon = holder.findViewById(R.id.iv_simple_logo);

        boolean hasArrowIcon = mBuilder.rightArrowIconRes > 0;
        boolean enableCheckBoxMode = simpleItem.enableCheckBoxMode();
        if(enableCheckBoxMode){
            ivRightArrow.setVisibility(View.GONE);
            switchSimple.setVisibility(View.VISIBLE);
            switchSimple.setChecked(simpleItem.checked());
        }else {
            switchSimple.setVisibility(View.GONE);
            if(hasArrowIcon){
                ivRightArrow.setImageResource(mBuilder.rightArrowIconRes);
            }
            ivRightArrow.setVisibility(hasArrowIcon? View.VISIBLE: View.GONE);
        }

        tvSimpleName.setText(simpleItem.title());
        boolean hasIcon = simpleItem.icon() > 0;
        ivIcon.setVisibility(hasIcon ? View.VISIBLE: View.GONE);
        if(hasIcon){
            ivIcon.setImageResource(simpleItem.icon());
        }
        View line = holder.findViewById(R.id.view_line);
        line.setBackgroundResource(mBuilder.lineColor);
        boolean lineAligned = mBuilder.lineAligned;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) line.getLayoutParams();
        if(lineAligned){
            params.leftMargin = line.getResources().getDimensionPixelSize(R.dimen.d_item_simple_margin_r);
            params.rightMargin = line.getResources().getDimensionPixelSize(R.dimen.d_item_simple_margin_r);
        }else {
            params.leftMargin = 0;
            params.rightMargin = 0;
        }
        if(simpleItem.selected()){
            holder.itemView.setBackgroundResource(mBuilder.itemBgColorRes);
        }else {
            holder.itemView.setBackgroundResource(mBuilder.unSelectedItemBgColorRes);
        }

        holder.itemView.setVisibility(simpleItem.visible()? View.VISIBLE: View.GONE);

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = simpleItem.visible()? mBuilder.itemHeight : 0;
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_recyclerview_simple;
    }

    public static class Builder{

        @DrawableRes int rightArrowIconRes = R.drawable.ic_arrow_back;
        @StyleRes int textStyle = R.style.AdapterSimpleText;
        @ColorRes int lineColor = R.color.c_horizontal_divider;
        @ColorRes int unSelectedItemBgColorRes = R.color.c_unselected_item_bg;
        @ColorRes int itemBgColorRes = android.R.color.white;
        @DimenRes int itemHeight = R.dimen.d_item_h;
        boolean lineAligned = false;
        List<SimpleItem> defaults;

        public Builder rightArrowIcon(@DrawableRes int rightArrowIcon){
            this.rightArrowIconRes = rightArrowIcon;
            return this;
        }

        public Builder textStyle(@StyleRes int style){
            this.textStyle = style;
            return this;
        }

        public Builder itemHeight(@DimenRes int itemHeight){
            this.itemHeight = itemHeight;
            return this;
        }

        public Builder lineColor(@ColorRes int lineColor){
            this.lineColor = lineColor;
            return this;
        }

        public Builder unSelectedItemBgColorRes(@ColorRes int unSelectedItemBgColor){
            this.unSelectedItemBgColorRes = unSelectedItemBgColor;
            return this;
        }

        public Builder itemBgColorRes(@ColorRes int itemBgColor){
            this.itemBgColorRes = itemBgColor;
            return this;
        }

        public Builder lineAligned(boolean lineAligned){
            this.lineAligned = lineAligned;
            return this;
        }

        public Builder item(List<SimpleItem.Default> items){
            ArrayList<SimpleItem> list = new ArrayList<>();
            if(items != null){
                list.addAll(items);
            }
            defaults = list;
            return this;
        }

        public SimpleAdapter build() {
            SimpleAdapter simpleAdapter = new SimpleAdapter(this);
            simpleAdapter.items = defaults;
            return simpleAdapter;
        }
    }
}
