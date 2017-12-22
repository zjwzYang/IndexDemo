package com.example.yangjie.indexdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements QuickIndexBar.OnLetterChangeListener {

    private String[] string = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };
    private List<IndexBean> dataList;
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private QuickIndexBar mQuickIndexBar;
    private TextView mTextView;
    private TextView mTopLetterV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据
        initDate();

        // 初始化View
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mMyAdapter = new MyAdapter(this, dataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mMyAdapter);

        mQuickIndexBar = (QuickIndexBar) findViewById(R.id.index_bar);
        mQuickIndexBar.setLetters(string);
        mQuickIndexBar.setOnLetterChangeListener(this);

        mTextView = (TextView) findViewById(R.id.center_text);
        mTopLetterV = (TextView) findViewById(R.id.top_letter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private float mTopHeigh;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mTopHeigh = mTopLetterV.getMeasuredHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 获得当前显示的第一个item
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int firstPosition = layoutManager.findFirstVisibleItemPosition();
                IndexBean item = mMyAdapter.getItem(firstPosition);
                mQuickIndexBar.setCurrentIndex(item.getFirstLetter());
                mTopLetterV.setText(item.getFirstLetter());

                View firstView = layoutManager.findViewByPosition(firstPosition);
                View secondView = layoutManager.findViewByPosition(firstPosition + 1);
                if (firstView != null && secondView != null) {
                    if (mTopHeigh > secondView.getTop() && mMyAdapter.isDiffIndex(firstPosition)) {
                        mTopLetterV.setY(secondView.getTop() - mTopHeigh);
                    } else {
                        mTopLetterV.setY(0);
                    }
                }
            }
        });
    }

    private void initDate() {
        dataList = new ArrayList<>();
        for (int i = 0; i < string.length; i++) {
            String firstLetter = string[i];
            for (int j = 0; j < 5; j++) {
                IndexBean indexBean = new IndexBean();
                indexBean.setFirstLetter(firstLetter);
                indexBean.setName(firstLetter + (j + 1));
                dataList.add(indexBean);
            }
        }
    }

    @Override
    public void onLetterChange(String letter) {
        if (letter == null) {
            return;
        }
        mTextView.setText(letter);
        if (mTextView.getVisibility() == View.GONE) {
            mTextView.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < mMyAdapter.getItemCount(); i++) {
            IndexBean item = mMyAdapter.getItem(i);
            if (letter.equals(item.getFirstLetter())) {
                moveToPosition(i);
                break;
            }
        }
    }

    @Override
    public void onReset() {
        if (mTextView.getVisibility() == View.VISIBLE) {
            mTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 移动到某个位置。
     *
     * @param index 位置
     */
    private void moveToPosition(int index) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        if (index <= firstItem) {
            mRecyclerView.scrollToPosition(index);
        } else if (index <= lastItem) {
            int top = mRecyclerView.getChildAt(index - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(index);
        }
    }
}
