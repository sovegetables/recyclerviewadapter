package com.sovegetables.loadmore

import androidx.collection.ArrayMap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 紧紧支持StaggeredGridLayoutManager和LinearLayoutManager
 */
class LoadMoreHelper private constructor(){

    private var mIsLoading: Boolean = false
    private var mLoadMoreHelper: ILoadMore? = null
    private val mLoadMoreHelperMap = ArrayMap<Class<out RecyclerView.LayoutManager>, ILoadMore>()
    private val loadMoreFinisher: LoadMoreFinisher

    companion object{

        @JvmStatic
        fun create(): LoadMoreHelper{
            return LoadMoreHelper()
        }
    }

    init {
        mLoadMoreHelperMap[StaggeredGridLayoutManager::class.java] = StaggeredGridLayoutManagerLoadMore()
        mLoadMoreHelperMap[LinearLayoutManager::class.java] = LinearLayoutManagerLoadMore()
        loadMoreFinisher = LoadMoreFinisher(this)
    }

    fun attach(recyclerView: RecyclerView, onLoadMoreListener: OnLoadMoreListener){
        mLoadMoreHelper = mLoadMoreHelperMap[recyclerView.layoutManager!!.javaClass]
        if (mLoadMoreHelper != null) {
            mLoadMoreHelper!!.attachRecyclerView(recyclerView)
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    mLoadMoreHelper!!.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    mLoadMoreHelper!!.onScrolled(recyclerView, dx, dy)
                    if (!mIsLoading && mLoadMoreHelper!!.isLastPosition) {
                        mIsLoading = true
                        onLoadMoreListener.onLoadMore(recyclerView, loadMoreFinisher)
                    }
                }
            })
        }
    }

    class LoadMoreFinisher(private var loadMoreHelper: LoadMoreHelper){

        fun finishLoadMore(){
            loadMoreHelper.mIsLoading = false
        }
    }

}

interface OnLoadMoreListener {
    fun onLoadMore(recyclerView: RecyclerView, loadMoreFinisher: LoadMoreHelper.LoadMoreFinisher)
}

internal interface ILoadMore {
    val isLastPosition: Boolean
    fun attachRecyclerView(recyclerView: RecyclerView)
    fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
    fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
}

internal class StaggeredGridLayoutManagerLoadMore : ILoadMore {

    private var mTotalItemCount: Int = 0
    private var mFirstVisibleItemPositions: IntArray? = null
    private var mVisibleItemCount: Int = 0

    override val isLastPosition: Boolean
        get() =
            mTotalItemCount <= mFirstVisibleItemPositions!![mFirstVisibleItemPositions!!.size - 1] + mVisibleItemCount

    override fun attachRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager?
        mFirstVisibleItemPositions = IntArray(layoutManager!!.spanCount)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {}

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager?
        mVisibleItemCount = layoutManager!!.childCount
        mTotalItemCount = layoutManager.itemCount
        layoutManager.findFirstCompletelyVisibleItemPositions(mFirstVisibleItemPositions)
    }
}

internal class LinearLayoutManagerLoadMore : ILoadMore {

    private var mTotalItemCount: Int = 0
    private var mLastVisibleItemPosition: Int = 0
    private var mVisibleItemCount: Int = 0

    override val isLastPosition: Boolean
        get() = mTotalItemCount <= mLastVisibleItemPosition + 1

    override fun attachRecyclerView(recyclerView: RecyclerView) {}

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {}

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        mVisibleItemCount = layoutManager!!.childCount
        mTotalItemCount = layoutManager.itemCount
        mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
    }
}