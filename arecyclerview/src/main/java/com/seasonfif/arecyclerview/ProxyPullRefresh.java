package com.seasonfif.arecyclerview;

/**
 * 创建时间：2017年07月09日16:45 <br>
 * 作者：zhangqiang <br>
 * 描述：下拉刷新代理
 *      下拉刷新发生时对业务的逻辑应该在此处统一进行处理，以免污染view中的刷新
 */
public class ProxyPullRefresh implements SimpleRecyclerView.OnPullRefreshListener {

    private SimpleRecyclerView.OnPullRefreshListener mListener;

    public void setListener(SimpleRecyclerView.OnPullRefreshListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onPullRefresh() {
        mListener.onPullRefresh();
    }
}
