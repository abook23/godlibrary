package com.god.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by abook23 on 2015/10/20.
 */
public class CameraUtil {

    private static final String TAG = "CameraUtil";
    private static CameraUtil cameraUtil;
    public String path;

    public static CameraUtil newInstance() {
        if (cameraUtil == null)
            cameraUtil = new CameraUtil();
        return cameraUtil;
    }

    private CameraUtil() {}

    public void startCamera(Activity activity, int request_camera) {
        startCamera(activity,request_camera,createImageFile());
    }

    public void startCamera(Fragment fragment, int request_camera) {
        startCamera(fragment,request_camera, createImageFile());
    }

    public void startCamera(Fragment fragment,int request_camera, Uri uri) {
        Intent intent = startCamera(uri);
        fragment.startActivityForResult(intent, request_camera);
    }

    public void startCamera(Activity activity,int request_camera, Uri uri) {

        Intent intent = startCamera(uri);
        try {
            activity.startActivityForResult(intent, request_camera);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity,"缺少相机权限:android.permission.CAMERA",Toast.LENGTH_SHORT).show();
            Log.e(TAG,"缺少相机权限:android.permission.CAMERA");
        }
    }

    private Uri createImageFile(){
        String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";

        //2016年5月6日14:11:55
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
        path += "/" + fileName;
        File file = new File(path);

        //通知资源文件库
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, fileName);
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        values.put(MediaStore.Images.Media.DATA, file.getPath());
//        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return Uri.fromFile(file);
    }

    private Intent startCamera(Uri uri){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);// 旋转
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);// 这样就将文件的存储方式和uri指定到了Camera应用中
        return intent;
    }

    public void delete() {
        File file = new File(path);
        if (!file.isDirectory())
            file.delete();
    }

}
