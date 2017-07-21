package com.seasonfif.arecyclerview;

/**
 * 创建时间：2017年07月01日14:57 <br>
 * 作者：zhangqiang <br>
 * 描述：分页接口
 */
public interface Paginable {

    boolean shouldLoadMore();

    void onLoadFinished(boolean loadmore, boolean successed);

    int getCurrentPage();
}
