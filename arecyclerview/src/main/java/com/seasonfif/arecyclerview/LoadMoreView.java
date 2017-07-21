package com.seasonfif.arecyclerview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 创建时间：2017年06月30日18:09 <br>
 * 作者：zhangqiang <br>
 * 描述：有状态（正在加载、已经最后一条）的view
 */
public class LoadMoreView extends LinearLayout implements ILoadMoreView{

  public static final int TYPE_HIDE = 0x0001;
  public static final int TYPE_LOADING = 0x0002;
  public static final int TYPE_REACH_END = 0x0003;
  public static final int TYPE_ERROR = 0x0004;
  private RelativeLayout mLoadingLayout;
  private ProgressBar mPbLoading;
  private TextView mTvLoading;
  private TextView mTvEnd;
  private String mEndText;
  private int currentType;

  public LoadMoreView(Context context) {
    super(context);
    initView();
  }

  private void initView() {
    inflate(getContext(), R.layout.lib_load_more, this);
    mLoadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
    mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
    mTvLoading = (TextView) findViewById(R.id.tv_loading);
    mTvEnd = (TextView) findViewById(R.id.tv_end);
  }

  public void setType(int type){
    currentType = type;
    if (type == TYPE_LOADING){
      showLoadingType();
    }else if(type == TYPE_REACH_END){
      showEndType();
    }else if(type == TYPE_ERROR){
      showErrorType();
    }else{
      this.setVisibility(GONE);
    }
  }

  private void showLoadingType() {
    this.setVisibility(VISIBLE);
    mLoadingLayout.setVisibility(VISIBLE);
    mTvEnd.setVisibility(GONE);
  }

  private void showEndType() {
    this.setVisibility(VISIBLE);
    mLoadingLayout.setVisibility(GONE);
    mTvEnd.setVisibility(VISIBLE);
    if (!TextUtils.isEmpty(mEndText)){
      mTvEnd.setText(mEndText);
    }
  }

  private void showErrorType() {
    if (isConnected(getContext())){
      setEndText("获取数据失败，请重试");
    }else{
      setEndText("连接不到网络，请检查网络");
    }
    showEndType();
  }

  public void setEndText(String mEndText){
    this.mEndText = mEndText;
  }

  public int getType(){
    return currentType;
  }

  /**
   * 网络是否已经连接
   */
  private boolean isConnected(Context context) {
    if (context != null) {
      ConnectivityManager cm = null;
      try {
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      } catch (Exception e) {
        // ignore
      }
      if (cm != null) {
        NetworkInfo[] infos = cm.getAllNetworkInfo();
        if (infos != null) {
          for (NetworkInfo ni : infos) {
            if (ni.isConnected()) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  @Override public void onBindView() {
    setEndText("加载更多");
    setType(LoadMoreView.TYPE_REACH_END);
  }

  @Override public void onLoading() {
    setType(LoadMoreView.TYPE_LOADING);
  }

  @Override public void onError() {
    setType(LoadMoreView.TYPE_ERROR);
  }
}
