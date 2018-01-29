package com.god.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.god.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * xxxxx
 * Created by abook23 on 2015/9/19.
 */
public class ViewPagerTabHost2 implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    private FragmentTabHost fragmentTabHost;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private Context mContext;

    public static ViewPagerTabHost2 instantiate(FragmentActivity activity, FragmentTabHost fth, ViewPager vp) {
        return new ViewPagerTabHost2(activity, fth, vp, activity.getSupportFragmentManager());
    }

    public static ViewPagerTabHost2 instantiate(Fragment fragment, FragmentTabHost fth, ViewPager vp) {
        return new ViewPagerTabHost2(fragment.getContext(), fth, vp, fragment.getFragmentManager());
    }

    private ViewPagerTabHost2(Context context, FragmentTabHost fth, ViewPager vp, FragmentManager fm) {
        this.mContext = context;
        this.fragmentTabHost = fth;
        this.viewPager = vp;
        this.fragmentManager = fm;
        fragmentTabHost.setOnTabChangedListener(this);//选择 table事件
        viewPager.addOnPageChangeListener(this);//滑动Page事件
        // viewPager.setOffscreenPageLimit(1);
    }

    public void initView(Context context, Fragment[] fragments, SimpleAdapter simpleAdapter) {
        fragmentTabHost.setup(context, fragmentManager, android.R.id.tabcontent);//初始化TabHost
        for (int i = 0; i < fragments.length; i++) {
            View view = simpleAdapter.getView(i);
            fragmentTabHost.addTab(
                    fragmentTabHost.newTabSpec(i + "").setIndicator(view),
                    fragments[i].getClass(),
                    null);
        }
        viewPager.setAdapter(new MyViewPagerAdapter(fragmentManager, fragments));
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

    //自定义View
    private View getTableHostView(Context context, @LayoutRes int resource, Map<String, ?> data,
                                  String[] from, @IdRes int[] to) {
        View v = View.inflate(context, resource, null);
        for (int i = 0; i < to.length; i++) {
            View view = v.findViewById(to[i]);
            if (view instanceof TextView)
                ((TextView) view).setText((String) data.get(from[i]));
            if (view instanceof ImageView)
                ((ImageView) view).setImageResource((Integer) data.get(from[i]));
        }
        return v;
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments;

        public MyViewPagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    public abstract class SimpleAdapter<T> {
        private List<T> mData;
        private int mResource;

        public SimpleAdapter(@LayoutRes int resource, List<T> data) {
            mResource = resource;
            mData = data;
        }

        public View getView(int position) {
            return createViewFromResource(position, mResource);
        }

        private View createViewFromResource(int position, int resource) {
            View view = View.inflate(mContext, resource, null);
            BaseViewHolder viewHolder = new BaseViewHolder(view);
            convert(viewHolder, position, mData.get(position));
            return view;
        }

        public abstract void convert(BaseViewHolder holder, int position, T t);
    }
}
