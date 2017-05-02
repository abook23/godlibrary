package com.god.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abook23 on 2016/1/4.
 */
public class ContactsUtils {

    public static List<String[]> getPhonteContacts(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<String[]> listPhone = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                CallLog callLog = new CallLog();
                String phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (TextUtils.isEmpty(phone_number))
                    continue;
                String phone_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String[] mPhone = new String[2];
                mPhone[0] = phone_number;
                mPhone[1] = phone_name;
                listPhone.add(mPhone);
            }
            cursor.close();
        }
        return listPhone;
    }
}
