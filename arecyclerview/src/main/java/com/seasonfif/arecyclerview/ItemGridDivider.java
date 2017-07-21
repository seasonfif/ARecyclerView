package com.seasonfif.arecyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 创建时间：2017年06月24日17:28 <br>
 * 作者：zhangqiang <br>
 * 描述：
 */

public class ItemGridDivider extends RecyclerView.ItemDecoration{

  private Drawable mDivider;
  private Paint mPaint;
  private int mSpace;

  public ItemGridDivider(Drawable drawable) {
    if (drawable == null) return;
    mDivider = drawable;
    mSpace = mDivider.getIntrinsicHeight();
  }

  public ItemGridDivider(int space, int color) {
    this.mSpace = space;
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(color);
    mPaint.setStyle(Paint.Style.FILL);
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent, State state) {
    int childCount = parent.getChildCount();
    if (childCount > 1){
      drawHorizontal(c, parent);
      drawVertical(c, parent);
    }
  }

  private int getSpanCount(RecyclerView parent) {
    // 列数
    int spanCount = -1;
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
    }
    return spanCount;
  }

  public void drawHorizontal(Canvas c, RecyclerView parent)
  {
    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int left = child.getLeft() - params.leftMargin;
      final int right = child.getRight() + params.rightMargin + mSpace;
      final int top = child.getBottom() + params.bottomMargin;
      final int bottom = top + mSpace;
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }

  public void drawVertical(Canvas c, RecyclerView parent)
  {
    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
          .getLayoutParams();
      final int top = child.getTop() - params.topMargin;
      final int bottom = child.getBottom() + params.bottomMargin;
      final int left = child.getRight() + params.rightMargin;
      final int right = left + mSpace;
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }

  private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
      int childCount) {
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      // 如果是最后一列，则不需要绘制右边
      if ((pos + 1) % spanCount == 0){
        return true;
      }
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      int orientation = ((StaggeredGridLayoutManager) layoutManager)
          .getOrientation();
      if (orientation == StaggeredGridLayoutManager.VERTICAL) {
        // 如果是最后一列，则不需要绘制右边
        if ((pos + 1) % spanCount == 0) {
          return true;
        }
      } else {
        childCount = childCount - childCount % spanCount;
        // 如果是最后一列，则不需要绘制右边
        if (pos >= childCount)
          return true;
      }
    }
    return false;
  }

  private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
      int childCount) {
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      childCount = childCount - childCount % spanCount;
      // 如果是最后一行，则不需要绘制底部
      if (pos >= childCount)
        return true;
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      int orientation = ((StaggeredGridLayoutManager) layoutManager)
          .getOrientation();
      // StaggeredGridLayoutManager 且纵向滚动
      if (orientation == StaggeredGridLayoutManager.VERTICAL) {
        childCount = childCount - childCount % spanCount;
        // 如果是最后一行，则不需要绘制底部
        if (pos >= childCount)
          return true;
      } else
      // StaggeredGridLayoutManager 且横向滚动
      {
        // 如果是最后一行，则不需要绘制底部
        if ((pos + 1) % spanCount == 0)
        {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void getItemOffsets(Rect outRect, int itemPosition,
      RecyclerView parent) {
    int spanCount = getSpanCount(parent);
    int childCount = parent.getAdapter().getItemCount();
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
      GridLayoutManager.SpanSizeLookup sizeLookup = manager.getSpanSizeLookup();
      int spanSize = sizeLookup.getSpanSize(itemPosition);
      if (childCount <= 1 && spanSize == spanCount) return;
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      View view = parent.getChildAt(itemPosition);
      StaggeredGridLayoutManager.LayoutParams lp =
          (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
      if (childCount <= 1 && lp.isFullSpan()) return;
    }

    // 如果是最后一行，则不需要绘制底部
    if (isLastRaw(parent, itemPosition, spanCount, childCount)){
      outRect.set(0, 0, mSpace, 0);
    } else if (isLastColum(parent, itemPosition, spanCount, childCount)) {
      // 如果是最后一列，则不需要绘制右边
      outRect.set(0, 0, 0, mSpace);
    } else {
      outRect.set(0, 0, mSpace, mSpace);
    }
  }
}
