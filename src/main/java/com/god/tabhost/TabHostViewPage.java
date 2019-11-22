/*
 * xxxxx
 * Created by abook23 on 2015/9/19.
 *
 *version 2.0 2019年6月21日14:37:37
 *
 *     <android.support.constraint.ConstraintLayout
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent">
 *
 *         <android.support.v4.view.ViewPager
 *             android:id="@+id/view_pager"
 *             android:layout_width="match_parent"
 *             android:layout_height="0dp"
 *             app:layout_behavior="@string/appbar_scrolling_view_behavior"
 *             app:layout_constraintTop_toTopOf="parent"
 *             app:layout_constraintBottom_toTopOf="@id/tabs" />
 *
 *         <android.support.v4.app.FragmentTabHost
 *             android:id="@+id/tabs"
 *             android:layout_width="match_parent"
 *             android:layout_height="wrap_content"
 *             android:background="?attr/colorPrimary"
 *             app:layout_constraintBottom_toBottomOf="parent">
 *             <TabWidget
 *                 android:id="@android:id/tabs"
 *                 android:layout_width="fill_parent"
 *                 android:layout_height="wrap_content" />
 *             <FrameLayout
 *                 android:id="@android:id/tabcontent"
 *                 android:layout_width="0dp"
 *                 android:layout_height="0dp"
 *                 android:layout_weight="0"/>
 *         </android.support.v4.app.FragmentTabHost>
 *     </android.support.constraint.ConstraintLayout>
 */
package com.god.tabhost;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;

import java.util.List;

public class TabHostViewPage<T extends TabHostViewPage.TabHostBean> implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    private Context mContext;
    private ViewPager viewPager;
    private FragmentTabHost fragmentTabHost;
    private FragmentManager fragmentManager;
    private List<T> tabHostBeanList;

    public TabHostViewPage(Fragment fragment, FragmentTabHost fth, ViewPager vp, List<T> data) {
        init(fragment.getContext(), fragment.getFragmentManager(),
                fth, vp, data);
    }

    public TabHostViewPage(FragmentActivity fragmentActivity, FragmentTabHost fth, ViewPager vp, List<T> data) {
        init(fragmentActivity.getApplicationContext(), fragmentActivity.getSupportFragmentManager(),
                fth, vp, data);
    }

    private void init(Context context, FragmentManager fragmentManager, FragmentTabHost fth, ViewPager vp, List<T> data) {
        this.mContext = context;
        this.fragmentManager = fragmentManager;
        this.viewPager = vp;
        this.fragmentTabHost = fth;
        this.tabHostBeanList = data;
        fragmentTabHost.setOnTabChangedListener(this);//选择 table事件
        viewPager.addOnPageChangeListener(this);//滑动Page事件
        // viewPager.setOffscreenPageLimit(1);
    }

    public void setAdapter(TabHostViewPagerAdapter<T> adapter) {
        fragmentTabHost.setup(mContext, fragmentManager, android.R.id.tabcontent);//初始化TabHost
        for (int i = 0; i < tabHostBeanList.size(); i++) {
            View view = adapter.getView(i, tabHostBeanList.get(i));
            fragmentTabHost.addTab(
                    fragmentTabHost.newTabSpec(i + "").setIndicator(view),
                    tabHostBeanList.get(i).fragment.getClass(),
                    null);
        }
        viewPager.setAdapter(new MyViewPagerAdapter(fragmentManager, tabHostBeanList));
        fragmentTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    /**
     * 显示 position Page
     *
     * @param position
     */
    public void setCurrentPage(int position) {
        fragmentTabHost.setCurrentTab(position);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabChanged(String tabId) {
        int position = fragmentTabHost.getCurrentTab();
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        fragmentTabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        private List<T> tabHostBeanList;

        MyViewPagerAdapter(FragmentManager fm, List<T> tabHostBeanList) {
            super(fm);
            this.tabHostBeanList = tabHostBeanList;
        }

        @Override
        public Fragment getItem(int position) {
            return tabHostBeanList.get(position).fragment;
        }

        @Override
        public int getCount() {
            return tabHostBeanList.size();
        }
    }

    public static class TabHostBean {
        public String title;
        public int resId;
        public Fragment fragment;

        public TabHostBean(String title, int resId,@NonNull Fragment fragment) {
            this.title = title;
            this.resId = resId;
            this.fragment = fragment;
        }
    }
}
