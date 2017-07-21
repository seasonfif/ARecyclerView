package com.seasonfif.arecyclerview;

/**
 * 创建时间：2017年06月26日16:57 <br>
 * 作者：zhangqiang <br>
 * 描述：设置数据总数类型的分页管理器
 */

public class PaginationTotalStyleManager implements Paginable{

    private static final int DEFAULT_PER_SIZE = 20;
    private static final int DEFAULT_CUR_PAGE = 1;

    private int mTotal;
    private int mPerSize = DEFAULT_PER_SIZE;
    private int mCurPage = DEFAULT_CUR_PAGE;

    public PaginationTotalStyleManager(int perSize) {
        if (perSize > 0){
            this.mPerSize = perSize;
        }
    }

    public void setTotal(int total) {
        this.mTotal = total;
    }

    public int getPages(){
        return mTotal % mPerSize == 0 ? mTotal/mPerSize : mTotal/mPerSize + 1;
    }

    @Override
    public boolean shouldLoadMore(){
        return getPages() > 1 && mCurPage != getPages();
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
