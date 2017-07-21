package com.seasonfif.arecyclerviewdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.seasonfif.arecyclerview.Empty;
import com.seasonfif.arecyclerview.ItemDivider;
import com.seasonfif.arecyclerview.PaginationWrappedRecyclerView;
import com.seasonfif.arecyclerview.SimpleRecyclerView;
import com.seasonfif.arecyclerview.LoadMoreView;
import com.seasonfif.arecyclerview.PaginationTotalStyleManager;
import com.seasonfif.arecyclerview.PaginationWrappedAdapter;
import com.seasonfif.arecyclerview.RecyclerType;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2017年06月24日10:51 <br>
 * 作者：zhangqiang <br>
 * 描述：
 */
public class PaginationRecyclerViewActivity extends Activity implements
    SimpleRecyclerView.OnItemClickListener, SimpleRecyclerView.OnItemLongClickListener,
    SimpleRecyclerView.OnPullRefreshListener, PaginationWrappedRecyclerView.OnLoadMoreListener{

    private PaginationWrappedRecyclerView paginationWrappedRecyclerView;
    private List<String> datas;
    private PaginationWrappedAdapter paginationWrappedAdapter;
    private PaginationTotalStyleManager paginationTotalStyleManager;
    private final static int PER_PAGE = 10;
    private final static int TOTAL = 56;

    /**
     * @param savedInstanceState
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lj_pagerv);

        paginationWrappedRecyclerView = (PaginationWrappedRecyclerView) findViewById(R.id.lJRecyclerView);
        paginationWrappedRecyclerView.setRecyclerType(RecyclerType.LINEARLAYOUT_VERTICAL);
        paginationWrappedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        paginationWrappedRecyclerView.addItemDecoration(new ItemDivider(getResources().getDrawable(R.drawable.div)));
        paginationWrappedRecyclerView.setOnItemClickListener(this);
        paginationWrappedRecyclerView.setOnItemLongClickListener(this);
        paginationWrappedRecyclerView.setOnPullRefreshListener(this);
        paginationWrappedRecyclerView.setOnLoadMoreListener(this);

        //TODO 自定义的LoadMoreView必须在setAdapter之前设置
        paginationWrappedRecyclerView.setLoadMoreView(new LoadMoreView(this));

        //TODO 添加header、footer必须在setAdapter之前
        initHeader();
        //TODO 空白页添加必须在setAdapter之前
        initEmptyView();

        paginationTotalStyleManager = new PaginationTotalStyleManager(PER_PAGE);
        //TODO 这样构造adapter没有分页功能
        //paginationWrappedAdapter = new MyAdapter();
        //TODO 这样构造adapter有分页功能
        paginationWrappedAdapter = new MyAdapter(paginationTotalStyleManager);
        paginationWrappedRecyclerView.setAdapter(paginationWrappedAdapter);

        //TODO setDatas必须在setAdapter之后
        datas = initData(1);
        if (datas.size() > 0){
            //设置分页参数
            paginationTotalStyleManager.setTotal(TOTAL);
        }
        paginationWrappedAdapter.setDatas(datas);
    }

    private TextView tv1;
    private TextView f2;

    private void initHeader() {
        tv1 = new TextView(this);
        tv1.setBackgroundColor(Color.RED);
        tv1.setTextColor(Color.WHITE);
        tv1.setText("header1");
        tv1.setGravity(Gravity.CENTER);
        tv1.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_50)));
        paginationWrappedRecyclerView.addHeaderView(tv1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                paginationWrappedAdapter.insertItem(2, "inserted");
            }
        });

        TextView tv2 = new TextView(this);
        tv2.setBackgroundColor(Color.GREEN);
        tv2.setTextColor(Color.WHITE);
        tv2.setText("header2");
        tv2.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_70)));
        paginationWrappedRecyclerView.addHeaderView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setText("header3");
        tv3.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_90)));
        paginationWrappedRecyclerView.addHeaderView(tv3);

        TextView f1 = new TextView(this);
        f1.setBackgroundColor(Color.BLUE);
        f1.setTextColor(Color.WHITE);
        f1.setText("footer1");
        paginationWrappedRecyclerView.addFooterView(f1);

        f2 = new TextView(this);
        f2.setBackgroundColor(Color.BLUE);
        f2.setTextColor(Color.WHITE);
        f2.setText("footer2");
        f2.setGravity(Gravity.CENTER);
        f2.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_50)));
        paginationWrappedRecyclerView.addFooterView(f2);
    }

    private void initEmptyView() {
        TextView empty = new TextView(this);
        empty.setBackgroundColor(Color.GRAY);
        empty.setTextColor(Color.WHITE);
        empty.setText("Empty");
        empty.setGravity(Gravity.CENTER);
        //mEmpty.setPadding(100,100,100,100);
        int height = Utils.getScreenSize(this)[1] * 3 / 4;
        LinearLayoutCompat.LayoutParams lp =
                new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        /*lp.topMargin = 100;
        lp.bottomMargin = 100;
        lp.leftMargin = 100;
        lp.rightMargin = 100;*/
        empty.setLayoutParams(lp);
        paginationWrappedRecyclerView.setEmptyView(empty);
        paginationWrappedRecyclerView.setEmptyArea(Empty.HEADER_COVER);
    }

    private ArrayList initData(int start) {
        ArrayList list = new ArrayList<>();
        int end = start + PER_PAGE;
        if (end > TOTAL){
            end = TOTAL;
        }

        if (Utils.isConnected(PaginationRecyclerViewActivity.this)){
            for (int i = start; i < end; i++){
                list.add("" + i);
            }
        }
        return list;
    }

    @Override public void onItemClick(View view, int position) {
        Toast.makeText(PaginationRecyclerViewActivity.this, "itemClick" + paginationWrappedAdapter.getItem(position), Toast.LENGTH_SHORT).show();
        paginationWrappedAdapter.updateItem(position, "str" + (position+1));
    }

    @Override public void onItemLongClick(View view, int position) {
        Toast.makeText(PaginationRecyclerViewActivity.this, "itemLongClick" + paginationWrappedAdapter
            .getItem(position), Toast.LENGTH_SHORT).show();
        paginationWrappedAdapter.removeItem(position);
    }

    @Override public void onPullRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                paginationWrappedRecyclerView.setRefreshing(false);
                ArrayList list = initData(1);
                if (list.size() > 0){
                    //设置分页参数
                    paginationTotalStyleManager.setTotal(TOTAL);
                }else{
                    paginationTotalStyleManager.setTotal(0);
                }
                paginationWrappedAdapter.setDatas(list);
            }
        }, 3000);
    }

    @Override public void onLoadMore() {
        paginationWrappedRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                paginationWrappedAdapter.setDatas(initData(paginationTotalStyleManager.getCurrentPage() * PER_PAGE + 1));
            }
        }, 3000);
    }

    private class MyAdapter extends PaginationWrappedAdapter<String> {

        public MyAdapter(){
            super();
        }

        public MyAdapter(PaginationTotalStyleManager manager){
            super(manager);
        }

        @Override
        public MyHolder onLJCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.recycler_item, parent, false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onLJBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
}
