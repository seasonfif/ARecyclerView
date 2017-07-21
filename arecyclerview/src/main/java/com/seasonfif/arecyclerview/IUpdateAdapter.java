package com.seasonfif.arecyclerview;

/**
 * 创建时间：2017年07月03日21:13 <br>
 * 作者：zhangqiang <br>
 * 描述：item更新扩展接口
 */

public interface IUpdateAdapter {

  void updateItem(int position, Object obj);

  void insertItem(int position, Object obj);

  Object removeItem(int position);
}
