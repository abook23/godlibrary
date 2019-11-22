package com.god.widget;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.god.em.EditType;
import com.god.util.StringUtils;

/**
 * Created by abook23 on 2016/9/12.
 */
public class EditTextWatcher implements TextWatcher, View.OnTouchListener {
    private boolean isDel;
    private int start, count, mDecimal;
    private EditText mContent;
    private EditType mEditType;
    private boolean isSelection;

    public EditTextWatcher(EditText editText, EditType type) {
        this.mContent = editText;
        this.mEditType = type;
        mContent.setOnTouchListener(this);
        mContent.setInputType(getType(type));
    }


    public EditTextWatcher(EditText editText, EditType type, int decimal) {
        this.mContent = editText;
        this.mEditType = type;
        this.mDecimal = decimal;
        mContent.setOnTouchListener(this);
        mContent.setInputType(getType(type));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        isDel = count > 0;
        if (isDel && s.length() == 1) {
            isSelection = false;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.start = start;
        this.count = count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isDel)
            switch (mEditType) {
                case phone:
                    phone(mContent);
                    break;
                case idNumber:
                    idNumber(mContent, start);
                    break;
                case numberDecimal:
                    numberDecimal(mContent, start, count, mDecimal);
                    break;

            }
    }

    /**
     * 自动补齐 0 或者 小数点
     *
     * @param mContent EditText
     * @param mDecimal 小数位数
     */
    private void numberDecimal(EditText mContent, int start, int count, int mDecimal) {
        String val = mContent.getText().toString();
        if (StringUtils.isEmpty(val))
            return;
        int index;
        if ((index = val.indexOf(".")) != -1) {
            if (index == 0) {
                val = "0" + val;
                mContent.setText(val);
                mContent.setSelection(val.length());
                return;
            }
            if (val.substring(index + 1).length() > mDecimal) {
                mContent.setText(val.substring(0, start));
                mContent.setSelection(val.length() - count);
            }
        } else {
            if ("0".equals(val)) {
                val += ".";
                mContent.setText(val);
                mContent.setSelection(val.length());
            }
        }
    }


    private void phone(EditText ed) {
        addSpace(ed, 11, 3, 7);
    }

    private void idNumber(EditText ed, int start) {
        String id = ed.getText().toString();
        if (StringUtils.isEmpty(id))
            return;
        String regEx_id;
        if (id.replaceAll(" ", "").length() == 18) {
            regEx_id = "\\d{0,18}|\\d{0,17}[x|X]";
        } else {
            regEx_id = "\\d{0,17}";
        }
        if (id.replaceAll(" ", "").matches(regEx_id))
            addSpace(ed, 18, 3, 6, 10, 14);
        else {
            ed.getText().delete(start, start + 1);
        }
    }

    /**
     * 添加间隔
     *
     * @param ed        EditText
     * @param maxLength 有效位数 比如 电话11位
     * @param location  要打间隔的位置
     */
    private void addSpace(EditText ed, int maxLength, int... location) {
        Editable s = ed.getText();
        String val = s.toString();
        String valRep = val.replace(" ", "");
        StringBuilder sb = new StringBuilder(valRep);
        for (int m = 0; m < location.length; m++) {
            if (sb.length() > location[m] + m) {
                sb.insert(location[m] + m, " ");
            }
        }
        if (sb.length() > maxLength + location.length) {
            String _s = sb.toString().substring(0, maxLength + location.length);
            ed.setText(_s);
            ed.setSelection(_s.length());
        } else {
            int k = mContent.getSelectionEnd();
            ed.setText(sb);
            ed.setSelection(isSelection ? k : sb.length());
        }
    }

    private int getType(EditType editType) {
        int type;
        switch (editType) {
            case phone:
                type = InputType.TYPE_CLASS_PHONE;
                break;
            case number:
                type = InputType.TYPE_CLASS_NUMBER;
                break;
            case numberDecimal:
                type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                break;
            case textPassword:
                type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;
            case numberPassword:
                type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
                break;
            default:
                type = InputType.TYPE_CLASS_TEXT;
        }
        return type;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isSelection = mContent.length() > 0;
        return false;
    }
}
