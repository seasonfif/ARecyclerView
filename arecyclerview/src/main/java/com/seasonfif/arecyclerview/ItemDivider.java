package com.seasonfif.arecyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/**
 * 创建时间：2017年06月24日15:22 <br>
 * 作者：zhangqiang <br>
 * 描述：线性布局的RecyclerView item分割线
 */

public class ItemDivider extends RecyclerView.ItemDecoration{

  public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

  public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

  private Drawable mDivider;

  private int mOrientation;

  private Paint mPaint;
  private int mSpace;

  /**
   * 支持drawable的构造方法
   * @param drawable
   */
  public ItemDivider(Drawable drawable) {
    if (drawable == null) return;
    mDivider = drawable;
    mSpace = mDivider.getIntrinsicHeight();
  }

  /**
   * 支持高度以及颜色设置的构造方法
   * @param space
   * @param color
   */
  public ItemDivider(int space, int color) {
    this.mSpace = space;
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(color);
    mPaint.setStyle(Paint.Style.FILL);
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent, State state) {
    super.onDraw(c, parent, state);
    LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
    if (layoutManager.getOrientation() == VERTICAL_LIST) {
      drawVertical(c, parent);
    } else {
      drawHorizontal(c, parent);
    }
  }

  public void drawVertical(Canvas c, RecyclerView parent) {
    final int left = parent.getPaddingLeft();
    final int right = parent.getMeasuredWidth() - parent.getPaddingRight();

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      int position = parent.getChildAdapterPosition(child);
      int childSize = parent.getAdapter().getItemCount();
      //最后一个item不显示分割线
      if (position == childSize - 1) return;
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
          .getLayoutParams();
      final int top = child.getBottom() + params.bottomMargin;
      int bottom = top + mSpace;
      if (mDivider != null){
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
      }
      if (mPaint != null){
        c.drawRect(left, top, right, bottom, mPaint);
      }
    }
  }

  public void drawHorizontal(Canvas c, RecyclerView parent) {
    final int top = parent.getPaddingTop();
    final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      int position = parent.getChildAdapterPosition(child);
      int childSize = parent.getAdapter().getItemCount();
      //最后一个item不显示分割线
      if (position == childSize - 1) return;

      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
          .getLayoutParams();
      final int left = child.getRight() + params.rightMargin;
      int right = left + mSpace;
      if (mDivider != null){
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
      }
      if (mPaint != null){
        c.drawRect(left, top, right, bottom, mPaint);
      }
    }
  }

  @Override public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
    super.getItemOffsets(outRect, itemPosition, parent);
    int childCount = parent.getAdapter().getItemCount();
    //最后一个item不显示分割线
    if (itemPosition == childCount - 1) return;

    LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
    mOrientation = layoutManager.getOrientation();
    if (mOrientation == VERTICAL_LIST) {
      outRect.set(0, 0, 0, mSpace);
    } else {
      outRect.set(0, 0, mSpace, 0);
    }
  }
}
