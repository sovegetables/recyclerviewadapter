package com.albert.recyclerview.help;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.albert.recyclerview.adapter.XOLazyRecyclerViewHolder;


/**
 * Created by alliu on 10/14/2016.
 */

public class RecyclerViewAdapterUtils {

    private RecyclerViewAdapterUtils(){}

    public static void fuLLSpan(XOLazyRecyclerViewHolder holder){
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if(params instanceof StaggeredGridLayoutManager.LayoutParams){
            ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
    }
}
