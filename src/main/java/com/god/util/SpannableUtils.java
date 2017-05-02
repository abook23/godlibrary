
package com.god.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.god.listener.OnItemViewListener;

public class SpannableUtils {
    public SpannableUtils() {
    }

    public static ImageSpan getImageSpan(Context context, @DrawableRes int res) {
        return new SpannableUtils.MyIm(context, res);
    }

    public static SpannableString setTextSpannable(TextView textView, String[] strings, int color, OnItemViewListener listener) {
        return setTextSpannable(textView, strings, new int[]{color}, listener);
    }

    public static SpannableString setTextSpannable(TextView textView, String[] strings, int[] color, final OnItemViewListener listener) {
        int start = 0;
        int end = 0;
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            if (str != null) {
                sb.append(str);
            }
        }
        SpannableString spannableString = new SpannableString(sb.toString());
        for (int i = 0; i < strings.length; ++i) {
            if (strings[i] != null) {
                end += strings[i].length();
                if (color.length == 1) {
                    spannableString.setSpan(new ForegroundColorSpan(textView.getResources().getColor(color[0])), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                } else if (color[i] > 0) {
                    spannableString.setSpan(new ForegroundColorSpan(textView.getResources().getColor(color[i])), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                }
                if (listener != null) {
                    final int finalI = i;
                    ClickableSpan cbs = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemViewClick(null, view, finalI);
                        }
                        @Override
                        public void updateDrawState(TextPaint textPaint) {
                            textPaint.setUnderlineText(false);
                        }
                    };
                    spannableString.setSpan(cbs, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                }
                start += strings[i].length();
            }
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
        return spannableString;
    }

    /**
     * 图片居中
     */
    public static class MyIm extends ImageSpan {
        public MyIm(Context context, @DrawableRes int resourceId) {
            super(context, resourceId);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable drawable = this.getDrawable();
            canvas.save();
            canvas.translate(x, (float) ((bottom - top - drawable.getBounds().bottom) / 2 + top));
            drawable.draw(canvas);
            canvas.restore();
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
            Rect rect = this.getDrawable().getBounds();
            if (fm != null) {
                FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
                end = fontMetricsInt.bottom - fontMetricsInt.top;
                int var = rect.bottom - rect.top;
                start = var / 2 - end / 4;
                end = var / 2 + end / 4;
                fm.ascent = -end;
                fm.top = -end;
                fm.bottom = start;
                fm.descent = start;
            }
            return rect.right;
        }
    }
}
