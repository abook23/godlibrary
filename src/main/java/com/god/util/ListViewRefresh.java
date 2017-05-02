package com.god.util;

import android.os.Handler;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.god.listener.OnRefreshListener;

import java.text.SimpleDateFormat;

/**
 * Created by My on 2016/3/18.
 */
public class ListViewRefresh implements AbsListView.OnScrollListener, View.OnTouchListener {
    private static final String TAG = ListViewRefresh.class.toString();

    private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
    private int downY; // 按下时y轴的偏移量
    private int headerViewHeight; // 头布局的高度
    private ViewGroup headerView; // 头布局的对象

    private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
    private final int RELEASE_REFRESH = 1; // 松开刷新
    private final int REFRESHING = 2; // 正在刷新中
    private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

    private Animation upAnimation; // 向上旋转的动画
    private Animation downAnimation; // 向下旋转的动画

    private ImageView ivArrow; // 头布局的剪头
    private ProgressBar mProgressBar; // 头布局的进度条
    private TextView tvState; // 头布局的状态
    private TextView tvLastUpdateTime; // 头布局的最后更新时间

    private boolean isScrollToBottom; // 是否滑动到底部
    private View footerView; // 脚布局的对象
    private int footerViewHeight; // 脚布局的高度
    private boolean isLoadingMore = false; // 是否正在加载更多中


    private OnRefreshListener mOnRefershListener;

    private Handler handler = new Handler();

    private ListView listView;
    private boolean isLoadingData;
    private AbsListView.OnScrollListener onScrollListener;

    public ListViewRefresh(ListView listView) {
        this.listView = listView;
        listView.setOnTouchListener(this);

    }

    public void setRefreshBoot(boolean value) {
        if (value) {
            listView.setOnScrollListener(this);
        } else
            listView.setOnScrollListener(null);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setHeaderView(ViewGroup view) {
        getView(view);
        initHeaderView(view);
    }

    public void setHeaderView(ViewGroup view, @IdRes int StateId) {
        tvState = (TextView) view.findViewById(StateId);
        initHeaderView(view);
    }

    public void setHeaderView(ViewGroup view, @IdRes int StateId, @IdRes int ImageViewId) {
        ivArrow = (ImageView) view.findViewById(ImageViewId);
        tvState = (TextView) view.findViewById(StateId);
        initHeaderView(view);
    }

    public void setHeaderView(ViewGroup view, @IdRes int StateId, @IdRes int ImageViewId, @IdRes int ProgressBarId) {
        mProgressBar = (ProgressBar) view.findViewById(ProgressBarId);
        ivArrow = (ImageView) view.findViewById(ImageViewId);
        tvState = (TextView) view.findViewById(StateId);
        initHeaderView(view);
    }


    public void setFooterView(ViewGroup view) {
        footerView = view;
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        listView.addFooterView(footerView);
        footerView.setPadding(0, -footerViewHeight, 0, 0);
    }

    /**
     * 初始化 headerView
     *
     * @param view
     */
    private void initHeaderView(ViewGroup view) {
        headerView = view;
        headerView.measure(0, 0); // 系统会帮我们测量出headerView的高度
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        //listView.addHeaderView(headerView); // 向ListView的顶部添加一个view对象
        initAnimation();
    }

    private void getView(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = viewGroup.getChildAt(i);
            if (mProgressBar == null && childView instanceof ProgressBar) {
                mProgressBar = (ProgressBar) childView;
            } else if (ivArrow == null && childView instanceof ImageView) {
                ivArrow = (ImageView) childView;
            } else if (tvState == null && childView instanceof TextView) {
                tvState = (TextView) childView;
            } else if (ivArrow == null || tvState == null || mProgressBar == null) {
                if (childView instanceof ViewGroup)
                    getView((ViewGroup) childView);
            }
        }
    }

    /**
     * 获得系统的最新时间
     *
     * @return
     */
    private String getLastUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        upAnimation = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

        downAnimation = new RotateAnimation(-180f, -360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (!isLoadingData)
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveY = (int) ev.getY();
                    // 移动中的y - 按下的y = 间距.
                    int diff = (moveY - downY) / 2;
                    if (diff > 50)
                        if (firstVisibleItemPosition == 0 && listView.getHeaderViewsCount() == 0) {
                            /**
                             * ListViewRefreshUtils 监听在 setadapter 之前
                             */
                            listView.addHeaderView(headerView);
                        }
                    // -头布局的高度 + 间距 = paddingTop
                    int paddingTop = -headerViewHeight + diff;
                    // 如果: -头布局的高度 > paddingTop的值 执行super.onTouchEvent(ev);
                    if (firstVisibleItemPosition == 0 && -headerViewHeight < paddingTop) {
                        if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) { // 完全显示了.
                            Log.i(TAG, "松开刷新");
                            currentState = RELEASE_REFRESH;
                            refreshHeaderView();
                        } else if (paddingTop < 0
                                && currentState == RELEASE_REFRESH) { // 没有显示完全
                            Log.i(TAG, "下拉刷新");
                            currentState = DOWN_PULL_REFRESH;
                            refreshHeaderView();
                        }
                        // 下拉头布局
                        headerView.setPadding(0, paddingTop, 0, 0);
                    } else if (isScrollToBottom) {
                        Log.d(TAG, "拉倒底部啦");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // 判断当前的状态是松开刷新还是下拉刷新
                    if (currentState == RELEASE_REFRESH) {
                        Log.i(TAG, "刷新数据.");
                        // 把头布局设置为完全显示状态
                        headerView.setPadding(0, 0, 0, 0);
                        // 进入到正在刷新中状态
                        currentState = REFRESHING;
                        refreshHeaderView();

                        if (mOnRefershListener != null) {
                            isLoadingData = true;
                            mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
                            handler.post(progressBarRunnable);
                        }
                    } else if (currentState == DOWN_PULL_REFRESH) {
                        // 隐藏头布局
                        headerView.setPadding(0, -headerViewHeight, 0, 0);
                        listView.removeHeaderView(headerView);
                    }
                    downY = 0;
                    break;
                default:
                    break;
            }
        return false;
    }

    public Runnable progressBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (mProgressBar != null) {
                mProgressBar.setMax(240);
                if (mProgressBar.getProgress() < mProgressBar.getMax() * 0.85) {
                    mProgressBar.setProgress(mProgressBar.getProgress() + 1);
                    handler.postDelayed(progressBarRunnable, 60);
                }
            }
        }
    };
    public Runnable progressBarRunnable2 = new Runnable() {
        @Override
        public void run() {
            if (mProgressBar != null) {
                if (mProgressBar.getProgress() < mProgressBar.getMax() * 0.98) {
                    mProgressBar.setProgress(mProgressBar.getProgress() + ((mProgressBar.getMax() - mProgressBar.getProgress()) / 3));
                    handler.postDelayed(progressBarRunnable2, 10);
                } else {
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                    mProgressBar.setProgress(0);
                    if (ivArrow != null)
                        ivArrow.setVisibility(View.VISIBLE);
                    if (mProgressBar != null)
                        mProgressBar.setVisibility(View.GONE);
                    listView.removeHeaderView(headerView);
                }
            } else {
                listView.removeHeaderView(headerView);
            }
        }
    };

    /**
     * 根据currentState刷新头布局的状态
     */
    private void refreshHeaderView() {
        switch (currentState) {
            case DOWN_PULL_REFRESH: // 下拉刷新状态

                setStateText("下拉刷新");
                setArrowStartAnimation(downAnimation); // 执行向下旋转
                break;
            case RELEASE_REFRESH: // 松开刷新状态
                setStateText("松开刷新");
                setArrowStartAnimation(upAnimation); // 执行向上旋转
                break;
            case REFRESHING: // 正在刷新中状态
                if (ivArrow != null) {
                    ivArrow.clearAnimation();
                    ivArrow.setVisibility(View.GONE);
                }
                if (mProgressBar != null)
                    mProgressBar.setVisibility(View.VISIBLE);
                setStateText("正在刷新中...");
                break;
            default:
                break;
        }
    }

    public void setStateText(String value) {
        if (tvState != null)
            tvState.setText(value);
    }

    public void setArrowStartAnimation(Animation animation) {
        if (ivArrow != null)
            ivArrow.startAnimation(animation);
    }


    /**
     * 设置刷新监听事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefershListener = listener;
    }

    public void startHeaderView() {
        if (listView.getHeaderViewsCount() > 0)
            return;
        listView.addHeaderView(headerView);
        currentState = REFRESHING;
        refreshHeaderView();
        headerView.setPadding(0, 0, 0, 0);
        if (mOnRefershListener != null) {
            isLoadingData = true;
            mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
            handler.post(progressBarRunnable);
        }
    }

    /**
     * 隐藏头布局
     */
    public void hideHeaderView() {
        isLoadingData = false;

        if (mProgressBar != null) {
            handler.removeCallbacks(progressBarRunnable);
            handler.post(progressBarRunnable2);
        } else {
            if (ivArrow != null)
                ivArrow.setVisibility(View.VISIBLE);
            setStateText("下拉刷新");
            if (tvLastUpdateTime != null)
                tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());
            listView.removeHeaderView(headerView);
        }
        currentState = DOWN_PULL_REFRESH;
    }

    /**
     * 隐藏脚布局
     */
    public void hideFooterView() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        isLoadingMore = false;
    }


    /**
     * 当滚动状态改变时回调
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(view, scrollState);
        }

        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            // 判断当前是否已经到了底部
            if (footerView != null)
                if (isScrollToBottom && !isLoadingMore) {
                    isLoadingMore = true;
                    // 当前到底部
                    Log.i(TAG, "加载更多数据");
                    footerView.setPadding(0, 0, 0, 0);
                    listView.setSelection(listView.getCount());

                    if (mOnRefershListener != null) {
                        mOnRefershListener.onLoadingMore();
                    }
                }
        }
    }

    /**
     * 当滚动时调用
     *
     * @param firstVisibleItem 当前屏幕显示在顶部的item的position
     * @param visibleItemCount 当前屏幕显示了多少个条目的总数
     * @param totalItemCount   ListView的总条目的总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        firstVisibleItemPosition = firstVisibleItem;

        if (listView.getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }
    }

}
