/*
* Copyright 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.god.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
/**
 * Utility class that wraps access to the runtime permissions API in M and provides basic helper
 * methods.
 * 使用方法
 * 1.PermissionUtil.requestPermission
 * 2.PermissionUtil.verifyPermissions
 */
public class PermissionUtil {

    private static final String[] pGroup = {"CALENDAR", "CAMERA", "CONTACTS", "LOCATION", "MICROPHONE", "PHONE", "SENSORS", "SMS", "STORAGE"};
    private static final String[] pGroupCN = {"日历", "相机", "联系人", "定位", "麦克风", "获取手机信息(手机号码/IMEI/IMSI权限)", "传感器", "SMS", "读写手机存储"};
    private static AlertDialog alertDialog;

    /**
     * activity 中使用
     */
    public static boolean requestPermission(Activity activity, String[] permissions, int request_contacts) {
        return _requestPermissions(activity, permissions, request_contacts);
    }

    /**
     * 在fragment 中使用
     */
    public static boolean requestPermission(Fragment fragment, String[] permissions, int request_contacts) {
        return _requestPermissions(fragment, permissions, request_contacts);
    }

    private static boolean _requestPermissions(Object object, String[] permissions, int request_contacts) {
        if (permissions.length < 1) {
            return false;
        }
        if (object instanceof Activity) {
            if (shouldShowRequestPermissionRationale((Activity) object, permissions))//检查用户拒绝的权限
                return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (String p : permissions) {
            if (object instanceof Activity) {
                //PERMISSION_GRANTED if you have the permission, or PERMISSION_DENIED if not.
                if (ActivityCompat.checkSelfPermission((Activity) object, p) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    ActivityCompat.requestPermissions((Activity) object, permissions, request_contacts);
                    return false;
                }
            } else if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                if (ContextCompat.checkSelfPermission(fragment.getActivity(), p) != PackageManager.PERMISSION_GRANTED) {
                    fragment.requestPermissions(permissions, request_contacts);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(Fragment fragment, String[] permissions, int[] grantResults) {
        return verifyPermissions(fragment.getActivity(), permissions, grantResults);
    }

    public static boolean verifyPermissions(Activity activity, String[] permissions, int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        StringBuilder sb = new StringBuilder();
        int m = 0;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                sb.append(++m).append(".").append(getPermissionCN(permissions[i])).append("\n");
            }
        }
        String msg = sb.toString();
        if (msg.length() > 0) {
            msg = "请授予以下权限:\n" + msg;
            alertDialog(activity, "提示", msg);
            return false;
        }
        return true;
    }

    /**
     * 用户拒绝的权限
     */
    private static boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions) {
        StringBuilder msgbff = new StringBuilder();
        if (permissions.length < 1) {
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        int i = 0;
        for (String permission : permissions) {
            //如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
            //如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
            //如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {//禁止的权限
                msgbff.append(++i).append(".").append(getPermissionCN(permission)).append("\n");
            }
        }
        String msg = msgbff.toString();
        if (msg.length() > 0) {
            msg = "请授予以下权限:\n" + msg;
            alertDialog(activity, "提示", msg);
            return true;
        }
        return false;
    }

    private static void alertDialog(final Context context, String title, String message) {
        if (alertDialog != null && alertDialog.isShowing())
            return;
        alertDialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        alertDialog = null;
                    }
                })
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        alertDialog = null;
                        AndroidUtils.showInstalledAppDetails(context, context.getPackageName());
                    }
                }).show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.setCancelable(false);
    }

    private static String getPermissionCN(String p) {
        for (int i = 0; i < pGroup.length; i++) {
            if (p.endsWith(pGroup[i]))
                return pGroupCN[i];
        }
        return p.substring(p.lastIndexOf('.') + 1);
    }

}
