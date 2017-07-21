package com.seasonfif.arecyclerview;

/**
 * 创建时间：2017年07月03日21:16 <br>
 * 作者：zhangqiang <br>
 * 描述：分页扩展接口
 */

public interface IPaginationAdapter {

  /**
   * 下拉刷新
   */
  void refresh();

  /**
   * 上拉加载
   */
  void loadMore();

  /**
   * 是否满足下拉刷新的条件
   * @return
   */
  boolean shouldLoadMore();
}
