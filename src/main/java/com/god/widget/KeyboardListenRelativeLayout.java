package com.god.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 自定义布局解决键盘弹出挡住输入框的问题
 * Created by abook23 on 2015/7/8.
 * 在使用 本控件的Activity 中调用 键盘刷新见面
 * getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
 */
public class KeyboardListenRelativeLayout extends RelativeLayout {

    private static final String TAG = KeyboardListenRelativeLayout.class.getSimpleName();

    private int width, height;
    public static final byte KEYBOARD_STATE_SHOW = -3;//显示
    public static final byte KEYBOARD_STATE_HIDE = -2;//隐藏
    public static final byte KEYBOARD_STATE_INIT = -1;//初始化

    private OnKeyboardStateChangedListener onKeyboardStateChangedListener;

    public KeyboardListenRelativeLayout(Context context) {
        super(context);
    }

    public KeyboardListenRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnKeyboardStateChangedListener(OnKeyboardStateChangedListener onKeyboardStateChangedListener) {
        this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.width = widthMeasureSpec;
        this.height = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (onKeyboardStateChangedListener != null) {
            if (oldh > 0 && oldh > h) {
                onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_SHOW);
            } else {
                onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_HIDE);
            }
            measure(this.width - w + getWidth(), this.height - h + getHeight());
        }

    }

    public interface OnKeyboardStateChangedListener {
        void onKeyboardStateChanged(int state);
     //   void onKeyboardStateChanged(int state, int old_h, int new_h);
    }
}
