package com.god.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by abook23 on 2016/5/4.
 */

public class LinearLayoutScroll extends LinearLayout implements AbsListView.OnScrollListener, View.OnTouchListener {
    private static final String TAG = "LinearLayoutScroll";
    private ListView listView;

    private LinearLayout linearLayoutHeader;
    private int firstVisibleItemPosition;
    private int downY;
    private int moveY;
    private SrcollTyep srcollTyep;

    public LinearLayoutScroll(Context context) {
        super(context);
        init();
    }

    public LinearLayoutScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearLayoutScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        findView(null);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void findView(ViewGroup view) {
        if (view == null) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = getChildAt(i);
                isView(childView);
            }
        } else {
            int count = view.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = view.getChildAt(i);
                isView(childView);
            }
        }
    }

    public void isView(View childView) {
        if (linearLayoutHeader == null && childView instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) childView;
            if (linearLayout.getId() == android.R.id.tabcontent)
                linearLayoutHeader = linearLayout;
        } else if (listView == null && childView instanceof ListView) {
            listView = (ListView) childView;
            listView.setOnTouchListener(this);
            listView.setOnScrollListener(this);
        }
    }

    int countMouey;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItemPosition = firstVisibleItem;
        Log.i(TAG, firstVisibleItemPosition + "");
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) ev.getY();
                int diff = (int) ((moveY - downY) / 1.5);
              //  Log.i(TAG, diff + "");
                countMouey += diff;
                if (moveY - downY > 0) {
                    srcollTyep = SrcollTyep.down;
                    if (firstVisibleItemPosition == 0)
                        setPadding(0, diff, 0, 0);
                } else {
                    srcollTyep = SrcollTyep.top;
                    setPadding(0, diff, 0, 0);
                }
                refreshHeaderView(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (srcollTyep == SrcollTyep.down)
                    setPadding(0, 0, 0, 0);
                countMouey = 0;
                break;
            default:
                break;
        }
        return false;
    }

    public void refreshHeaderView(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    public enum SrcollTyep {
        top, down
    }
}
