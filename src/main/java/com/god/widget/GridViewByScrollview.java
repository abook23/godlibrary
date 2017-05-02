package com.god.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by abook23 on 2015/9/9.
 *
 * @author abook23 abook23@163.com
 * @version 1.0
 */
public class GridViewByScrollview extends GridView {
    public GridViewByScrollview(Context context) {
        super(context);
    }

    public GridViewByScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewByScrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);//测量所以儿子的高度
        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec);
    }
}
