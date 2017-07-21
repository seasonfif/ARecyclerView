package com.seasonfif.arecyclerview;

/**
 * 创建时间：2017年07月14日16:01 <br>
 * 作者：zhangqiang <br>
 * 描述：“上拉加载”view的交互接口
 */

public interface ILoadMoreView {

  /**
   * “上拉加载”view刚显示到RecyclerView时显示的样式
   */
  void onBindView();

  /**
   * “上拉加载”正在loading时显示的样式
   */
  void onLoading();

  /**
   * “上拉加载”数据无效时显示的样式
   */
  void onError();
}
