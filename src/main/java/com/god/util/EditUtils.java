package com.god.util;

import android.widget.EditText;

import com.god.em.EditType;

/**
 * Created by My on 2016/9/12.
 */
public class EditUtils extends CheckUtils {

    public static void editPhone(EditText edit) {
        edit.addTextChangedListener(new EditTextWatcher(edit, EditType.phone));
    }

    public static void editIdNumber(EditText edit) {
        edit.addTextChangedListener(new EditTextWatcher(edit, EditType.idNumber));
    }

    public static void editNumberDecimal(EditText edit) {
        edit.addTextChangedListener(new EditTextWatcher(edit, EditType.numberDecimal));
    }

    public static void editNumberDecimal(EditText edit, int decimals) {
        edit.addTextChangedListener(new EditTextWatcher(edit, EditType.numberDecimal, decimals));
    }

}
