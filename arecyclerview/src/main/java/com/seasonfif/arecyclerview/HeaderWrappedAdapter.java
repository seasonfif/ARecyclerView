package com.seasonfif.arecyclerview;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2017年07月03日20:51 <br>
 * 作者：zhangqiang <br>
 * 描述：继承自BaseRecyclerAdapter
 *      扩展了Header、Footer与空白页的展示
 */

public abstract class HeaderWrappedAdapter<D> extends SimpleRecyclerAdapter<D> implements IHeaderAdapter {

  /**
   * header列表
   */
  protected ArrayList<View> mHeaderViews = new ArrayList<>();

  /**
   * footer列表
   */
  protected ArrayList<View> mFooterViews = new ArrayList<>();

  /**
   * 空白页
   * 为了使用与header、footer相同逻辑处理空白页，这里用数量为1的列表保存empty
   */
  protected ArrayList<View> mEmptyViews = new ArrayList<>(1);

  /**
   * header缓存
   */
  private ArrayList<View> headerCache = new ArrayList<>();

  /**
   * footer缓存
   */
  private ArrayList<View> footerCache = new ArrayList<>();

  /**
   * 空白页
   */
  protected View mEmpty;

  /**
   * 空白页展示的位置标记
   */
  private int mEmptyFlag;

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (getHeaderFooterByViewType(viewType) == VIEW_TYPE_HEADER){
      int headerIndex = getIndexByViewType(viewType);
      return new HeaderFooterHolder(mHeaderViews.get(headerIndex));
    }else if(getHeaderFooterByViewType(viewType) == VIEW_TYPE_FOOTER){
      int footerIndex = getIndexByViewType(viewType);
      return new HeaderFooterHolder(mFooterViews.get(footerIndex));
    }else if(viewType == VIEW_TYPE_EMPTY){
      return new EmptyViewHolder(mEmpty);
    }
    return onLJCreateViewHolder(parent, viewType);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    int numHeaders = getHeadersCount() + getEmptyCount();
    if (position >= numHeaders){
      int adjPosition = position - numHeaders;
      int adapterCount = mDatas.size();
      if (adjPosition < adapterCount){
        super.onBindViewHolder(holder, adjPosition);
        onLJBindViewHolder(holder, adjPosition);
        return;
      }
    }
  }

  protected abstract RecyclerView.ViewHolder onLJCreateViewHolder(ViewGroup parent, int viewType);

  protected abstract void onLJBindViewHolder(RecyclerView.ViewHolder holder, int position);

  @Override public void setDatas(@Nullable List<D> data) {
    if (data == null || data.size() == 0){
      enableEmpty();
    }else{
      if (mDatas != null && mDatas.size() > 0){
        mDatas.clear();
      }
      disableEmpty();
      super.setDatas(data);
    }
  }

  @Override
  public int getItemViewType(int position) {
    int numHeaders = getHeadersCount();
    if (position < numHeaders){
      return getHeaderViewTypeByIndex(position);
    }

    if (getEmptyCount() > 0 && position == numHeaders){
      return VIEW_TYPE_EMPTY;
    }

    int adjPosition = position - numHeaders - getEmptyCount();
    int adapterCount = 0;
    if (position >= numHeaders+getEmptyCount()){
      adapterCount = mDatas.size();
      if (adjPosition < adapterCount){
        return super.getItemViewType(adjPosition);
      }
    }
    return getFooterViewTypeByIndex(adjPosition - adapterCount);
  }

  @Override
  public int getItemCount() {
    return mDatas.size() + getHeadersCount() + getFootersCount() + getEmptyCount();
  }

  @Override public void updateItem(int position, Object obj) {
    mDatas.set(position, (D)obj);
    notifyItemChanged(position + getHeadersCount());
  }

  @Override public void insertItem(int position, Object obj) {
    int count = getDataCount();
    if (position > count){
      position = count;
    }
    mDatas.add(position, (D)obj);
    notifyItemInserted(position + getHeadersCount());
    if (getHeadersCount() > 0 || getFootersCount() > 0){
      //fixme Java.lang.IllegalArgumentException: Called attach on a child which is not detached
      //notifyItemRangeChanged(position + getHeadersCount(), getItemCount());
      notifyDataSetChanged();
    }else{
      notifyItemRangeChanged(position + getHeadersCount(), getItemCount());
    }
  }

  @Override
  public D removeItem(int position){
    D d = mDatas.remove(position);
    notifyItemRemoved(position + getHeadersCount());
    if (getHeadersCount() > 0 || getFootersCount() > 0){
      //fixme Java.lang.IllegalArgumentException: Called attach on a child which is not detached
      //notifyItemRangeChanged(position + getHeadersCount(), getItemCount());
      notifyDataSetChanged();
    }else{
      notifyItemRangeChanged(position + getHeadersCount(), getItemCount());
    }
    return d;
  }

  @Override
  public int getHeadersCount(){
    return mHeaderViews.size();
  }

  @Override
  public int getFootersCount(){
    return mFooterViews.size();
  }

  public int getEmptyCount(){
    return mEmptyViews.size();
  }

  /**
   * 添加header
   * @param headers
   */
  @Override
  public void setHeaderViews(ArrayList<View> headers){
    this.mHeaderViews = headers;
  }

  /**
   * 添加footer
   * @param footers
   */
  @Override
  public void setFooterViews(ArrayList<View> footers){
    this.mFooterViews = footers;
  }

  /**
   * 添加空白页面
   * @param empty
   */
  @Override public void setEmpty(View empty) {
    this.mEmpty = empty;
  }

  /**
   * 设置空白页区域
   * @param flag
   */
  protected void setEmptyArea(@Empty int flag) {
    this.mEmptyFlag |= flag;
  }

  /**
   * 显示空白页面
   */
  protected void enableEmpty() {
    //设置过empty并且当前没有显示时才会显示空白页面
    if (mEmpty != null && getEmptyCount() == 0){
      //清除已有数据
      mDatas.clear();
      //空白页如果覆盖header，则将header缓存后clear
      if (doseCoverHeader()){
        headerCache.addAll(mHeaderViews);
        mHeaderViews.clear();
      }

      //空白页如果覆盖footer，则将footer缓存后clear
      if (doseCoverFooter()){
        footerCache.addAll(mFooterViews);
        mFooterViews.clear();
      }
      mEmptyViews.add(mEmpty);
      notifyDataSetChanged();
    }
  }

  private boolean doseCoverHeader(){
    return (mEmptyFlag & Empty.HEADER_COVER) > 0;
  }

  private boolean doseCoverFooter(){
    return (mEmptyFlag & Empty.FOOTER_COVER) > 0;
  }

  /**
   * 删掉空白页面
   */
  protected void disableEmpty(){

    if (mEmpty == null) return;

    //删掉空白页同时显示原来缓存的header与footer
    if (headerCache.size() > 0){
      mHeaderViews.addAll(headerCache);
      headerCache.clear();
    }
    if (footerCache.size() > 0){
      mFooterViews.addAll(footerCache);
      footerCache.clear();
    }
    mEmptyViews.remove(mEmpty);
  }

  /**
   * GridLayoutManager设置
   * header、footer、空白页、loadmoreview的布局
   * @param recyclerView
   */
  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager){
      final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
      final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
      gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {
          int viewType = getItemViewType(position);
          if (getHeaderFooterByViewType(viewType) == VIEW_TYPE_HEADER || getHeaderFooterByViewType(viewType) == VIEW_TYPE_FOOTER
              || viewType == VIEW_TYPE_EMPTY || viewType == VIEW_TYPE_LOADMORE){
            return gridLayoutManager.getSpanCount();
          }
          if (spanSizeLookup != null){
            return spanSizeLookup.getSpanSize(position);
          }
          return 0;
        }
      });
    }
  }

  /**
   * StaggeredGridLayoutManager设置header与footer的布局
   * @param holder
   */
  @Override public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
    int position = holder.getLayoutPosition();
    if (isHeaderViewPos(position) || isFooterViewPos(position)) {
      setFullSpan(holder);
    }
  }

  private boolean isHeaderViewPos(int position) {
    return position < getHeadersCount();
  }

  private boolean isFooterViewPos(int position) {
    return position >= getHeadersCount() + getItemCount();
  }

  private void setFullSpan(RecyclerView.ViewHolder holder) {
    ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
    if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
      StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
      p.setFullSpan(true);
    }
  }

  /**
   * 取header、footer类型
   */
  private static final int MASK_HEADER_FOOTER = 0xf0000000;

  /**
   * 取header、footer的list索引
   */
  private static final int MASK_INDEX = 0x000000ff;

  private int getHeaderViewTypeByIndex(int index){
    return index | VIEW_TYPE_HEADER;
  }

  private int getFooterViewTypeByIndex(int index){
    return index | VIEW_TYPE_FOOTER;
  }

  /**
   * 取header、footer类型
   */
  private int getHeaderFooterByViewType(int viewType){
    return viewType & MASK_HEADER_FOOTER;
  }

  /**
   * 取header、footer的list索引
   */
  private int getIndexByViewType(int viewType){
    return viewType & MASK_INDEX;
  }

  private class HeaderFooterHolder extends RecyclerView.ViewHolder {
    public HeaderFooterHolder(View view) {
      super(view);
    }
  }

  private class EmptyViewHolder extends RecyclerView.ViewHolder {
    public EmptyViewHolder(View view) {
      super(view);
    }
  }

}
