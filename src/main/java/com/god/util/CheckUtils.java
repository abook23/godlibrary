package com.god.util;

import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 校验
 *
 * @author Comsys-anook23
 * <p>
 * 2015-5-28 下午2:38:09
 */
public class CheckUtils extends StringUtils {

    /**
     * 密码校验
     * 如果两个密码不相等  清楚第二个密码  并且在第二个密码输入框 setHint("两次密码不一样")
     *
     * @param password1
     * @param password2
     * @return boolean    返回类型
     */
    public static boolean isPasswordSame(EditText password1, EditText password2) {
        boolean b = true;
        String pas1 = password1.getText().toString();
        String pas2 = password2.getText().toString();
        if (!pas1.equals(pas2)) {
            b = false;
            password2.setText("");
            password2.setHint("两次密码不一样");
            setHintTextColor(password2);
        }
        return b;
    }

    /**
     * 判断textView 是否有空  ( "" || null)
     */
    public static boolean isNullTextView(int Color, String msg, TextView... textViews) {
        boolean b = false;
        for (TextView textView : textViews) {
            if (isEmpty(textView.getText().toString())) {
                if (!isEmpty(msg)) {
                    textView.setHint(msg);
                }
                if (Color != -1)
                    textView.setHintTextColor(Color);
                return true;
            }
        }
        return b;
    }

    /**
     * 判断textView 是否有空  ( "" || null)
     */
    public static boolean isNullEditView(int Color, String msg, EditText... editTexts) {
        boolean b = false;
        for (EditText editText : editTexts) {
            if (isEmpty(editText.getText().toString())) {
                if (!isEmpty(msg)) {
                    editText.setHint(msg);
                }
                if (Color != -1)
                    editText.setHintTextColor(Color);
                editText.setSelection(0);
                editText.requestFocus();
                return true;
            }
        }
        return b;
    }

    public static boolean isNullEditView(EditText... edt) {
        return isNullEditView(-1, "", edt);
    }

    /**
     * 手机号码
     *
     * @param Color
     * @param edt
     * @param errMsg
     * @return boolean    返回类型
     */
    public static boolean isPhone(int Color, EditText edt, String errMsg) {
        boolean b = true;
        if (!isMobileNO(edt.getText().toString())) {
            if (isEmpty(errMsg))
                errMsg = "手机号有误";
            edt.setHint(errMsg);
            edt.setHintTextColor(Color);
            b = false;
        }
        return b;
    }

    @Deprecated
    public static boolean isNull(String value) {
        if ("".equals(value) || null == value || value.length() < 1) {
            return true;
        }
        return false;
    }

    /**
     * 不可编辑
     *
     * @param editTexts
     */
    public static void setEnabled(boolean enabled, EditText... editTexts) {
        for (EditText e : editTexts) {
            e.setEnabled(enabled);
        }
    }

    /**
     * 不可编辑
     *
     * @param textViews
     */
    public static void setEnabled(boolean enabled, TextView... textViews) {
        for (TextView e : textViews) {
            e.setEnabled(enabled);
        }
    }

    /**
     * 不可编辑
     *
     * @param radioButtons
     */
    public static void setEnabled(boolean enabled, RadioButton... radioButtons) {
        for (RadioButton e : radioButtons) {
            e.setEnabled(enabled);
        }
    }

    /**
     * 默认红色
     *
     * @return void    返回类型
     * setHintTextColor
     * @paramedt 设定文件
     */
    private static void setHintTextColor(EditText edt) {
        setHintTextColor(Color.RED, edt);
    }

    private static void setHintTextColor(int Color, EditText edt) {
        edt.setHintTextColor(Color);
    }
}
