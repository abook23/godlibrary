package com.god.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abook23 on 2015/12/28.
 */
public class CheckLayout extends LinearLayout implements Checkable {
    // checked状态
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private boolean mChecked;

    public CheckLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked != mChecked) {
            mChecked = checked;
            refreshDrawableState();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof CompoundButton) {
                    if (child.getVisibility() == VISIBLE && child instanceof CheckBox) {
                        ((CheckBox) child).setChecked(checked);
                        break;
                    }
                    if (child.getVisibility() == VISIBLE && child instanceof RadioButton) {
                        ((RadioButton) child).setChecked(checked);
                        break;
                    }
                }
            }
            setBackgroundColor(checked ? Color.GRAY : -1);//当选中时呈现蓝色
//             setBackgroundDrawable(checked ? new ColorDrawable(0xff0000a0) : null);//当选中时呈现蓝色

        }

    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if (isChecked()) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
            return drawableState;
        }
        return super.onCreateDrawableState(extraSpace);
    }

    public static long[] getCheckedItemIds(ListView listview) {
        List<Integer> list = new ArrayList<>();
        //listview.getCheckedItemPositions(); 为触发过选择的item ,选中 为true 取消false
        SparseBooleanArray sp = listview.getCheckedItemPositions();
        for (int i = 0; i < sp.size(); i++) {
            if (sp.valueAt(i)) {
                list.add(sp.keyAt(i));
            }
        }
        long[] ids = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i);
        }
        return ids;
    }
}
