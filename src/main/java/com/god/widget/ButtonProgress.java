package com.god.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;

import com.god.R;


/**
 * Created by abook23 on 2016/11/30.
 */

public class ButtonProgress extends Button {
    private int mMax, mProgress;
    private Paint mPaint;
    private int mWidth;
    private float mRadius;
    private int mProgressColor;

    public ButtonProgress(Context context) {
        super(context);
        init();
    }

    public ButtonProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        getTypedArray(context, attrs);
        init();
    }

    public ButtonProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getTypedArray(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
    }

    public void getTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonProgress);
        mRadius = typedArray.getFloat(R.styleable.ButtonProgress_radius, 8);
        mProgressColor = typedArray.getColor(R.styleable.ButtonProgress_progressColor, getResources().getColor(R.color.colorAccent));
        mProgress = typedArray.getInt(R.styleable.ButtonProgress_progress, 0);
        mMax = typedArray.getInt(R.styleable.ButtonProgress_max, 100);
        typedArray.recycle();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(1.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDraw(canvas);
        super.onDraw(canvas);
    }

    private void mDraw(Canvas canvas) {
        Rect rect = new Rect();      //先获取Button的边框
        canvas.getClipBounds(rect);
        rect.left += getPaddingLeft() / 3;      //填充条的右边界根据当前进度来计算
        rect.top += getPaddingTop() / 2;
        rect.right = rect.left + (int) ((float) mProgress / mMax * (mWidth - (getPaddingRight() / 2)));
        //rect.right += (rect.left - getPaddingLeft()) + (mProgress * getWidth() / 100) - getPaddingRight();
        rect.bottom -= getPaddingBottom() / 2;
        canvas.drawRoundRect(new RectF(rect), mRadius, mRadius, mPaint);
    }

    public void setMax(int max) {
        mMax = max;
    }

    public void setProgress(int progress) {
        if (progress > mMax)
            mProgress = mMax;
        if (mProgress != progress) {
            mProgress = progress;
            postInvalidate();
        }
    }
}
