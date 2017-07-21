package com.seasonfif.arecyclerview;

import android.view.View;
import java.util.ArrayList;

/**
 * 创建时间：2017年07月03日21:07 <br>
 * 作者：zhangqiang <br>
 * 描述：Header、Footer、空白页扩展接口
 */

public interface IHeaderAdapter {

  /**
   * header
   */
  int VIEW_TYPE_HEADER = 0x10000000;

  /**
   * footer
   */
  int VIEW_TYPE_FOOTER = 0x20000000;

  /**
   * “加载更多”的viewtype
   */
  int VIEW_TYPE_LOADMORE = 0x30000000;

  /**
   * mEmpty
   */
  int VIEW_TYPE_EMPTY = 0x40000000;

  /**
   * 添加header
   * @param headers
   */
  void setHeaderViews(ArrayList<View> headers);

  /**
   * 添加footer
   * @param footers
   */
  void setFooterViews(ArrayList<View> footers);

  /**
   * 获取header数量
   * @return
   */
  int getHeadersCount();

  /**
   * 获取footer数量
   * @return
   */
  int getFootersCount();

  /**设置空白页
   * @param empty
   */
  void setEmpty(View empty);
}
