package com.sovegetables.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalDividerDecoration extends RecyclerView.ItemDecoration{
    private ShapeDrawable  mDivider;

    public HorizontalDividerDecoration(Context context,
                                       @DimenRes int dividerHeight,
                                       @ColorRes int dividerColor) {
        mDivider = new ShapeDrawable(new RectShape());
        mDivider.getPaint().setColor(ContextCompat.getColor(context, dividerColor));
        mDivider.setIntrinsicHeight(context.getResources().getDimensionPixelSize(dividerHeight));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
