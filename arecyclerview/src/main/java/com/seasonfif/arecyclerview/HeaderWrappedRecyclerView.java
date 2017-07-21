package com.seasonfif.arecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;

/**
 * 创建时间：2017年06月24日17:08 <br>
 * 作者：zhangqiang <br>
 * 描述：继承自基础SimpleRecyclerView
 *       扩展功能： 1、支持header、footer
 *                  2、支持空白页
 */
public class HeaderWrappedRecyclerView extends SimpleRecyclerView {

  private static final String TAG = "LJHeaderWrappedRecyclerView";

  private HeaderWrappedAdapter mOriginalAdapter;

  protected ArrayList<View> mHeaderViews = new ArrayList<>();

  protected ArrayList<View> mFooterViews = new ArrayList<>();

  protected View mEmpty;

  protected int mEmptyFlag;

  public HeaderWrappedRecyclerView(Context context) {
    this(context, null);
  }

  public HeaderWrappedRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * 设置adapter
   * @param adapter
   */
  public void setAdapter(HeaderWrappedAdapter adapter){
    this.mOriginalAdapter = adapter;
    initRefreshConfig();
    initLayoutManager();
    initListener();
    mOriginalAdapter.setHeaderViews(mHeaderViews);
    mOriginalAdapter.setFooterViews(mFooterViews);
    mOriginalAdapter.setEmpty(mEmpty);
    mOriginalAdapter.setEmptyArea(mEmptyFlag);
    mRecyclerView.setAdapter(mOriginalAdapter);
  }

  /**
   * 添加header
   * @param header
   */
  public void addHeaderView(View header){
    mHeaderViews.add(header);
  }

  /**
   * 添加footer
   * @param footer
   */
  public void addFooterView(View footer){
    mFooterViews.add(footer);
  }

  /**
   * 设置空白页
   * @param empty
   */
  public void setEmptyView(View empty){
    this.mEmpty = empty;
  }

  /**
   * 设置空白页位置
   * @param flag
   */
  public void setEmptyArea(@Empty int flag){
    this.mEmptyFlag = flag;
  }


  /**
   * 获得adapter
   * @return
   */
  public HeaderWrappedAdapter getAdapter(){
    return mOriginalAdapter;
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
