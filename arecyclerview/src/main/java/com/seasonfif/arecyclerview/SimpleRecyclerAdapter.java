package com.seasonfif.arecyclerview;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2017年06月26日14:57 <br>
 * 作者：zhangqiang <br>
 * 描述：LJRecyclerView的adapter基类
 *      处理了点击事件的绑定以及item更新
 */

public abstract class SimpleRecyclerAdapter<D> extends RecyclerView.Adapter implements IUpdateAdapter {

  protected List<D> mDatas = new ArrayList<>();

  protected SimpleRecyclerView.OnItemClickListener mOnItemClickListener;

  protected SimpleRecyclerView.OnItemLongClickListener mOnItemLongClickListener;

  public void setDatas(@Nullable List<D> data){
    this.mDatas = data;
    notifyDataSetChanged();
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
    if (mOnItemClickListener != null){
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mOnItemClickListener.onItemClick(holder.itemView, position);
        }
      });
    }
    if (mOnItemLongClickListener != null){
      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override public boolean onLongClick(View v) {
          mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
          return true;
        }
      });
    }
  }

  @Override public int getItemCount() {
    return isEmpty() ? 0 : mDatas.size();
  }

  public boolean isEmpty(){
    return mDatas == null || mDatas.size() == 0;
  }

  public D getItem(int position){
    if (!isEmpty()){
      return mDatas.get(position);
    }
    return null;
  }

  public List<D> getDatas() {
    return mDatas;
  }

  protected int getDataCount(){
    return isEmpty() ? 0 : mDatas.size();
  }

  @Override public void updateItem(int position, Object obj) {
    mDatas.set(position, (D)obj);
    notifyItemChanged(position);
  }

  @Override public void insertItem(int position, Object obj) {
    int count = getDataCount();
    if (position > count){
      position = count;
    }
    mDatas.add(position, (D)obj);
    notifyItemInserted(position);
    notifyItemRangeChanged(position, getItemCount());
  }

  @Override
  public D removeItem(int position){
    D d = mDatas.remove(position);
    notifyItemRemoved(position);
    notifyItemRangeChanged(position, getItemCount());
    return d;
  }

  protected void setOnItemClickListener(SimpleRecyclerView.OnItemClickListener onItemClickListener) {
    this.mOnItemClickListener = onItemClickListener;
  }

  protected void setOnItemLongClickListener(SimpleRecyclerView.OnItemLongClickListener onItemLongClickListener) {
    this.mOnItemLongClickListener = onItemLongClickListener;
  }
}
