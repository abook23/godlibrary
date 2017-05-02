package com.god.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * xxxxx
 * Created by abook23 on 2015/9/19.
 */
public class ViewPagerTabHost implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    private FragmentTabHost fragmentTabHost;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;

    public static ViewPagerTabHost instantiate(FragmentTabHost fth, ViewPager vp, FragmentManager fm) {
        ViewPagerTabHost viewPagerTabHost = new ViewPagerTabHost(fth, vp, fm);
        return viewPagerTabHost;
    }

    private ViewPagerTabHost(FragmentTabHost fth, ViewPager vp, FragmentManager fm) {
        this.fragmentTabHost = fth;
        this.viewPager = vp;
        this.fragmentManager = fm;

        fragmentTabHost.setOnTabChangedListener(this);//选择 table事件
        viewPager.addOnPageChangeListener(this);//滑动Page事件
       // viewPager.setOffscreenPageLimit(1);
    }

    public void initView(Context contexts, String[] pagerNames, Fragment[] fragments) {
        List<Fragment> list_fragment = new ArrayList<>();
        fragmentTabHost.setup(contexts, fragmentManager, android.R.id.tabcontent);//初始化TabHost
        for (int i = 0; i < pagerNames.length; i++) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec(pagerNames[i]).
                    setIndicator(pagerNames[i]), fragments[i].getClass(), null);
            list_fragment.add(fragments[i]);
        }
        //ViewPage adapter is extends FragmentPagerAdapter
        viewPager.setAdapter(new MyViewPagerAdapter(fragmentManager, list_fragment));
    }

    /**
     * 参考 SimpleAdapter的参数说明
     * @param context
     * @param resource
     * @param data
     * @param from
     * @param to
     * @param fragments
     *
     * /*SimpleAdapter的参数说明
     * 第一个参数 表示访问整个android应用程序接口，基本上所有的组件都需要
     * 第二个参数表示生成一个Map(String ,Object)列表选项
     * 第三个参数表示界面布局的id  表示该文件作为列表项的组件
     * 第四个参数表示该Map对象的哪些key对应value来生成列表项
     * 第五个参数表示来填充的组件 Map对象key对应的资源一依次填充组件 顺序有对应关系
     * 注意的是map对象可以key可以找不到 但组件的必须要有资源填充  因为 找不到key也会返回null 其实就相当于给了一个null资源
     * 下面的程序中如果 new String[] { "name", "head", "desc","name" } new int[] {R.id.name,R.id.head,R.id.desc,R.id.head}
     * 这个head的组件会被name资源覆盖
     * */
    public void initView(Context context,@LayoutRes int resource, List<? extends Map<String, ?>> data,
                          String[] from, @IdRes int[] to,
                         Fragment[] fragments) {
        List<Fragment> list_fragment = new ArrayList<>();
        fragmentTabHost.setup(context, fragmentManager, android.R.id.tabcontent);//初始化TabHost
        for (int i = 0; i < data.size(); i++) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec(i + "").
                    setIndicator(getTableHostView(context, resource, data.get(i), from, to)),
                    fragments[i].getClass(), null);
            list_fragment.add(fragments[i]);
        }
        viewPager.setAdapter(new MyViewPagerAdapter(fragmentManager, list_fragment));
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

        private List<Fragment> fragments;

        public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
