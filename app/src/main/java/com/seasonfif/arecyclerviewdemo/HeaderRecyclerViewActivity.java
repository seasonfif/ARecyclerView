package com.seasonfif.arecyclerviewdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.seasonfif.arecyclerview.Empty;
import com.seasonfif.arecyclerview.HeaderWrappedAdapter;
import com.seasonfif.arecyclerview.ItemDivider;
import com.seasonfif.arecyclerview.HeaderWrappedRecyclerView;
import com.seasonfif.arecyclerview.SimpleRecyclerView;
import com.seasonfif.arecyclerview.RecyclerType;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2017年06月24日10:51 <br>
 * 作者：zhangqiang <br>
 * 描述：
 */
public class HeaderRecyclerViewActivity extends Activity implements SimpleRecyclerView.OnItemClickListener,
        SimpleRecyclerView.OnItemLongClickListener, SimpleRecyclerView.OnPullRefreshListener {

    private HeaderWrappedRecyclerView headerWrappedRecyclerView;
    private HeaderWrappedAdapter headerWrappedAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lj_headerrv);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);

        headerWrappedRecyclerView = (HeaderWrappedRecyclerView) findViewById(R.id.recycler);
        headerWrappedRecyclerView.setRecyclerType(RecyclerType.LINEARLAYOUT_VERTICAL);
        headerWrappedRecyclerView.addItemDecoration(new ItemDivider(20, Color.GRAY));
        headerWrappedRecyclerView.setDisablePullRefresh(false);
        headerWrappedRecyclerView.setOnPullRefreshListener(this);
        headerWrappedRecyclerView.setOnItemClickListener(this);
        headerWrappedRecyclerView.setOnItemLongClickListener(this);

        //TODO 添加header、footer必须在setAdapter之前
        initHeader();
        //TODO 空白页添加必须在setAdapter之前
        initEmptyView();

        headerWrappedAdapter = new MyAdapter();
        headerWrappedRecyclerView.setAdapter(headerWrappedAdapter);

        //TODO 如果添加了header、footer、空白页，那么setDatas必须在setAdapter之后
        headerWrappedAdapter.setDatas(initData());
    }

    private List initData() {
        List data = new ArrayList();
        if (Utils.isConnected(HeaderRecyclerViewActivity.this)){
            for (int i = 0; i <= 20; i++){
                data.add(i+"");
            }
        }
        return data;
    }

    @Override
    public void onItemClick(View view, int position) {
        headerWrappedAdapter.updateItem(position, "update " + headerWrappedAdapter.getItem(position));
        Toast.makeText(HeaderRecyclerViewActivity.this, headerWrappedAdapter.getItem(position) + " has been updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Toast.makeText(HeaderRecyclerViewActivity.this, headerWrappedAdapter.getItem(position) + " has been removed!", Toast.LENGTH_SHORT).show();
        headerWrappedAdapter.removeItem(position);
    }

    @Override
    public void onPullRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                headerWrappedRecyclerView.setRefreshing(false);
                headerWrappedAdapter.setDatas(initData());
                Toast.makeText(HeaderRecyclerViewActivity.this, " refresh successed!", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }


    private class MyAdapter extends HeaderWrappedAdapter<String> {
        @Override
        protected RecyclerView.ViewHolder onLJCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.recycler_item, parent, false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @Override
        protected void onLJBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String s = getItem(position);
            MyHolder myHolder = (MyHolder) holder;
            myHolder.tv.setText(s);
        }
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.title);
        }
    }

    private void initHeader() {
        TextView tv1 = new TextView(this);
        tv1.setBackgroundColor(Color.RED);
        tv1.setTextColor(Color.WHITE);
        tv1.setText("header1");
        tv1.setGravity(Gravity.CENTER);
        tv1.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_50)));
        headerWrappedRecyclerView.addHeaderView(tv1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                headerWrappedRecyclerView.getAdapter().insertItem(2, "inserted");
            }
        });

        TextView tv2 = new TextView(this);
        tv2.setBackgroundColor(Color.GREEN);
        tv2.setTextColor(Color.WHITE);
        tv2.setText("header2");
        tv2.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_70)));
        headerWrappedRecyclerView.addHeaderView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setText("header3");
        tv3.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_90)));
        headerWrappedRecyclerView.addHeaderView(tv3);

        TextView f1 = new TextView(this);
        f1.setBackgroundColor(Color.BLUE);
        f1.setTextColor(Color.WHITE);
        f1.setText("footer1");
        headerWrappedRecyclerView.addFooterView(f1);

        TextView f2 = new TextView(this);
        f2.setBackgroundColor(Color.BLUE);
        f2.setTextColor(Color.WHITE);
        f2.setText("footer2");
        f2.setGravity(Gravity.CENTER);
        f2.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_50)));
        headerWrappedRecyclerView.addFooterView(f2);
    }

    private void initEmptyView() {
        TextView empty = new TextView(this);
        empty.setBackgroundColor(Color.GRAY);
        empty.setTextColor(Color.WHITE);
        empty.setText("Empty");
        empty.setGravity(Gravity.CENTER);
        //TODO 空白页高度设置为MATCH_PARENT并不会达到预期效果，会被RecyclerView转为WRAP_CONTENT
        int height = Utils.getScreenSize(this)[1] * 3 / 4;
        LinearLayoutCompat.LayoutParams lp =
                new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        /*lp.topMargin = 100;
        lp.bottomMargin = 100;
        lp.leftMargin = 100;
        lp.rightMargin = 100;*/
        empty.setLayoutParams(lp);
        headerWrappedRecyclerView.setEmptyView(empty);
        //TODO empty默认会覆盖数据部分，通过setEmptyArea设置额外需要覆盖的部分header、footer或者全部覆盖
        headerWrappedRecyclerView.setEmptyArea(Empty.HEADER_COVER);
        empty.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //TODO 此处可以显示progressdialog，但记得在onPullRefresh刷新完成时dismiss，原因在下面。
                progressDialog.show();
                //TODO 如果空白页的点击事件是刷新数据，那么请调用doRefresh()，该方法最后调用的是onPullRefresh()
                headerWrappedRecyclerView.doRefresh();
            }
        });
    }

    @Override protected void onDestroy() {
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        super.onDestroy();
    }
}
