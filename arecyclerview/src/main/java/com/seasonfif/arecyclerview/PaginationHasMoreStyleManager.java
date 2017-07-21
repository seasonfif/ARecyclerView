package com.seasonfif.arecyclerview;

/**
 * 创建时间：2017年06月26日16:57 <br>
 * 作者：zhangqiang <br>
 * 描述：设置“更多数据标识”类型的分页管理器
 */

public class PaginationHasMoreStyleManager implements Paginable{

    private static final int DEFAULT_CUR_PAGE = 1;

    /**
     * 当前页
     * 初始为第一页
     */
    private int mCurPage = DEFAULT_CUR_PAGE;
    private boolean mHasMore;

    public void setHasMore(boolean hasMore) {
        this.mHasMore = hasMore;
    }

    @Override
    public boolean shouldLoadMore(){
        return mHasMore;
    }

    @Override
    public void onLoadFinished(boolean loadmore, boolean successed){
        if (loadmore && successed){
            mCurPage++;
        }

        if (!loadmore && successed){
            mCurPage = DEFAULT_CUR_PAGE;
        }
    }

    @Override
    public int getCurrentPage() {
        return mCurPage;
    }
}
