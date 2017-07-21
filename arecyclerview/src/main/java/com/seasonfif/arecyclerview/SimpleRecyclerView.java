package com.seasonfif.arecyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建时间：2017年06月24日17:08 <br>
 * 作者：zhangqiang <br>
 * 描述：封装基础LJSimpleRecyclerView
 *       1、下拉刷新；
 *       2、点击、长按事件监听；
 *       3、item更新
 */
public class SimpleRecyclerView extends SwipeRefreshLayout {

  private static final String TAG = "LJSimpleRecyclerView";

  protected RecyclerView mRecyclerView;

  /**
   * 标识RecyclerView的布局种类
   * 见{@link RecyclerType}
   * 默认为纵向的listview
   */
  protected int mRecyclerType = RecyclerType.LINEARLAYOUT_VERTICAL;

  /**
   * RecyclerView的LayoutManager
   */
  protected RecyclerView.LayoutManager mLayoutManager;

  /**
   * 标识gridlayout与staggeredGridLayout显示的行数或列数
   * 纵向时为列数，横向时为行数
   * 默认为-1
   */
  protected int mSpanCount = GridLayoutManager.DEFAULT_SPAN_COUNT;

  /**
   * 标识横向RecyclerView内容显示的方向
   * 为true表示右→左
   * 默认为false即表示从左至右显示
   */
  protected boolean mReverseLayout = false;

  /**
   * 标识禁止下拉刷新
   */
  protected boolean mDisablePullRefresh = false;

  private SimpleRecyclerAdapter mOriginalAdapter;
  private ProxyPullRefresh refreshProxy;

  public SimpleRecyclerView(Context context) {
    this(context, null);
  }

  public SimpleRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mRecyclerView = new RecyclerView(context, attrs);
    addView(mRecyclerView);
  }

  /**
   * 设置RecyclerView的布局种类
   * 默认为纵向的listview
   * @param recyclerType
   */
  public void setRecyclerType(@RecyclerType int recyclerType){
    this.mRecyclerType = recyclerType;
  }

  /**
   * 标识gridlayout与staggeredGridLayout显示的行数或列数
   * 纵向时为列数，横向时为行数
   * 默认为-1
   */
  public void setSpanCount(int spanCount) {
    this.mSpanCount = spanCount;
  }

  /**
   * RecyclerView内容显示的方向
   * 只有横向布局才需要设置
   * 默认从左到右显示
   * @param reverseLayout
   */
  public void setReverseLayout(boolean reverseLayout){
    this.mReverseLayout = reverseLayout;
  }

  /**
   * 设置adapter
   * @param adapter
   */
  public void setAdapter(SimpleRecyclerAdapter adapter){
    this.mOriginalAdapter = adapter;
    initRefreshConfig();
    initLayoutManager();
    initListener();
    mRecyclerView.setAdapter(mOriginalAdapter);
  }

  /**
   * 添加分割线
   * @param decoration
   */
  public void addItemDecoration(ItemDecoration decoration){
    mRecyclerView.addItemDecoration(decoration);
  }

  /**
   * 添加item动画
   * @param animator
   */
  public void setItemAnimator(ItemAnimator animator){
    mRecyclerView.setItemAnimator(animator);
  }

  public RecyclerView getRecyclerView() {
    return mRecyclerView;
  }

  /**
   * 获得adapter
   * @return
   */
  public SimpleRecyclerAdapter getAdapter(){
    return mOriginalAdapter;
  }

  protected void initLayoutManager() {

    switch (mRecyclerType){
      case RecyclerType.LINEARLAYOUT_VERTICAL:
        mLayoutManager = new LinearLayoutManager(getContext());
        break;
      case RecyclerType.LINEARLAYOUT_HORIZONTAL:
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, mReverseLayout);
        setEnabled(false);
        break;
      case RecyclerType.GRIDLAYOUT_VERTICAL:
        mLayoutManager = new GridLayoutManager(getContext(), mSpanCount);
        break;
      case RecyclerType.GRIDLAYOUT_HORIZONTAL:
        mLayoutManager = new GridLayoutManager(getContext(), mSpanCount, GridLayoutManager.HORIZONTAL, mReverseLayout);
        setEnabled(false);
        break;
      case RecyclerType.STAGGEREDGRIDLAYOUT_VERTICAL:
        mLayoutManager = new StaggeredGridLayoutManager(mSpanCount, StaggeredGridLayoutManager.VERTICAL);
        break;
      case RecyclerType.STAGGEREDGRIDLAYOUT_HORIZONTAL:
        mLayoutManager = new StaggeredGridLayoutManager(mSpanCount, StaggeredGridLayoutManager.HORIZONTAL);
        setEnabled(false);
        break;
      default:
        mLayoutManager = new LinearLayoutManager(getContext());
        break;
    }
    mRecyclerView.setLayoutManager(mLayoutManager);
  }

  protected void initRefreshConfig(){
    refreshProxy = new ProxyPullRefresh();
    if (mDisablePullRefresh){
      this.setEnabled(false);
    }else{
      this.setOnRefreshListener(new OnRefreshListener() {
        @Override
        public void onRefresh() {
          doRefresh();
        }
      });
    }
  }

  protected void beforeRefresh() {}

  /**
   * 刷新事件执行
   */
  public void doRefresh() {
    if (mOnPullRefreshListener != null){
      beforeRefresh();
      refreshProxy.setListener(mOnPullRefreshListener);
      refreshProxy.onPullRefresh();
    }
  }

  /**
   * 设置是否禁止下拉刷新
   * 默认不禁止
   * @param disablePullRefresh
   */
  public void setDisablePullRefresh(boolean disablePullRefresh) {
    this.mDisablePullRefresh = disablePullRefresh;
  }

  /**
   * 监听“下拉刷新”
   */
  public interface OnPullRefreshListener {
    void onPullRefresh();
  }

  protected OnPullRefreshListener mOnPullRefreshListener;

  public void setOnPullRefreshListener(OnPullRefreshListener onPullRefreshListener) {
    this.mOnPullRefreshListener = onPullRefreshListener;
  }

  /**
   * item短按事件监听
   */
  public interface OnItemClickListener {

    void onItemClick(View view, int position);
  }

  /**
   * item长按事件监听
   */
  public interface OnItemLongClickListener {

    void onItemLongClick(View view, int position);
  }

  protected OnItemClickListener mOnItemClickListener;

  protected OnItemLongClickListener mOnItemLongClickListener;

  public void setOnItemClickListener(SimpleRecyclerView.OnItemClickListener onItemClickListener) {
    this.mOnItemClickListener = onItemClickListener;
  }

  public void setOnItemLongClickListener(SimpleRecyclerView.OnItemLongClickListener onItemLongClickListener) {
    this.mOnItemLongClickListener = onItemLongClickListener;
  }

  private void initListener() {
    if (mOnItemClickListener != null){
      mOriginalAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    if (mOnItemLongClickListener != null){
      mOriginalAdapter.setOnItemLongClickListener(mOnItemLongClickListener);
    }
  }
}
