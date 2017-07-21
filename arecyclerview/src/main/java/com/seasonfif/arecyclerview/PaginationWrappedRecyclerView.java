package com.seasonfif.arecyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建时间：2017年06月24日17:08 <br>
 * 作者：zhangqiang <br>
 * 描述：继承自HeaderWrappedRecyclerView
 *       扩展功能：1、上拉加载
 *                 2、loadmoreview自定义
 */
public class PaginationWrappedRecyclerView extends HeaderWrappedRecyclerView {

  private static final String TAG = "LJPaginationWrappedRecyclerView";

  private PaginationWrappedAdapter mOriginalAdapter;

  private ILoadMoreView mLoadMoreView;

  public PaginationWrappedRecyclerView(Context context) {
    this(context, null);
  }

  public PaginationWrappedRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * 设置adapter
   * @param adapter
   */
  public void setAdapter(PaginationWrappedAdapter adapter){
    this.mOriginalAdapter = adapter;
    initRefreshConfig();
    initLayoutManager();
    initListener();
    mRecyclerView.addOnScrollListener(new RecyclerScrollListener());
    if (mLoadMoreView == null){
      mLoadMoreView = new LoadMoreView(getContext());
    }
    mOriginalAdapter.setLoadMoreView(mLoadMoreView);
    mOriginalAdapter.setHeaderViews(mHeaderViews);
    mOriginalAdapter.setFooterViews(mFooterViews);
    mOriginalAdapter.setEmpty(mEmpty);
    mOriginalAdapter.setEmptyArea(mEmptyFlag);
    mRecyclerView.setAdapter(mOriginalAdapter);
  }

  /**
   * 自定义LoadMoreView
   * @param loadMoreView
   */
  public void setLoadMoreView(ILoadMoreView loadMoreView) {
    if (loadMoreView instanceof View){
      this.mLoadMoreView = loadMoreView;
    } else {
      throw new IllegalArgumentException("must extends View or ViewGroup");
    }
  }

  /**
   * 获得adapter
   * @return
   */
  public PaginationWrappedAdapter getAdapter(){
    return mOriginalAdapter;
  }

  @Override protected void beforeRefresh() {
    mOriginalAdapter.refresh();
  }

  private void initListener() {
    if (mOnItemClickListener != null){
      mOriginalAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    if (mOnItemLongClickListener != null){
      mOriginalAdapter.setOnItemLongClickListener(mOnItemLongClickListener);
    }
  }

  /**
   * 监听“加载更多”
   */
  public interface OnLoadMoreListener{
    void onLoadMore();
  }

  protected OnLoadMoreListener mOnLoadMoreListener;

  public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
    this.mOnLoadMoreListener = onLoadMoreListener;
  }

  private class RecyclerScrollListener extends RecyclerView.OnScrollListener {

    private int lastVisibleItemPosition;
    private RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
    private ProxyLoadMore loadMoreProxy = new ProxyLoadMore();

    @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);

      if (newState != RecyclerView.SCROLL_STATE_IDLE || !mOriginalAdapter.shouldLoadMore()) return;

      if (layoutManager instanceof GridLayoutManager) {
        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
      } else if (layoutManager instanceof StaggeredGridLayoutManager) {
        int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
        lastVisibleItemPosition = findMax(into);
      } else {
        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
      }

      if (lastVisibleItemPosition + 1 == mOriginalAdapter.getItemCount()) {
        mOriginalAdapter.loadMore();
        if (mOnLoadMoreListener != null){
          loadMoreProxy.setListener(mOnLoadMoreListener);
          loadMoreProxy.onLoadMore();
        }
      }
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);
    }

    private int findMax(int[] lastPositions) {
      int max = lastPositions[0];
      for (int value : lastPositions) {
        if (value > max) {
          max = value;
        }
      }
      return max;
    }
  }
}
