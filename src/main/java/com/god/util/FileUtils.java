package com.god.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by abook23 on 2014/9/22. 目录名为项目名
 * /cache_data/[packageName]
 */
public class FileUtils {

    private Context context;
    /**
     * 手机硬缓缓存根目录
     */
    private String mDataRootPath = null;
    /**
     * 硬缓存目录名
     */
    private String CC_FOLDER_NAME = null;

    /**
     * sd卡的根目录
     */
    public String mSdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * 软缓存目录名
     */
    // private  String SD_FOLDER_NAME = "/Android/data";
    private String SD_FOLDER_NAME = "/Android/data";
    public boolean isWrite = true;


    /**
     * @param context context
     */
    public FileUtils(Context context) {
        this.context = context;
        mDataRootPath = context.getCacheDir().getPath();
        CC_FOLDER_NAME = File.separator + context.getPackageName();
        SD_FOLDER_NAME += File.separator + context.getPackageName();
    }

    /**
     * @param mDriName 在跟目录中的名称
     * @param context  context
     */
    public FileUtils(Context context, String mDriName) {
        this.context = context;
        mDataRootPath = context.getCacheDir().getPath();
        if (mDriName != null && mDriName.length() > 0) {
            if (mDriName.indexOf("/") != 0) {
                mDriName = "/" + mDriName;
            }
            SD_FOLDER_NAME = mDriName;
        }
        CC_FOLDER_NAME = mDriName;
        SD_FOLDER_NAME = mDriName;
    }

    /**
     * 获取储存的目录
     *
     * @return
     */
    public String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? mSdRootPath + SD_FOLDER_NAME
                : mDataRootPath + CC_FOLDER_NAME;
    }

    /**
     * 获取储存的根目录
     *
     * @return
     */
    public String getRootPath() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? mSdRootPath
                : mDataRootPath;
    }

    private OnOutputStreamListener mOutputStreamListener;

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     *
     * @param fileName
     * @param bitmap
     */
    public File saveBitmap(String dirsName, String fileName, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            File file = createSDFile(dirsName, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            String Type = fileName.substring(fileName.lastIndexOf(".") + 1)
                    .toUpperCase();
            if ("PNG".equals(Type) || "png".equals(Type)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            }

            fos.flush();
            fos.close();
            scannerFile(file.getPath());
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从手机或者sd卡获取Bitmap 大图片用 BitmapUitls.getSmallBitmap 防止OOM
     *
     * @param fileName
     * @return
     */
    public Bitmap getBitmap(String dirsName, String fileName) {
        return BitmapFactory.decodeFile(getStorageDirectory() + dirsName + File.separator
                + fileName);
    }

    /**
     * 获取文件的大小
     *
     * @param fileName
     * @return
     */
    public long getFileSize(String dirsName, String fileName) {
        return new File(getStorageDirectory() + dirsName + File.separator + fileName)
                .length();
    }

    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    public boolean delete(File file) {
        if (!file.exists()) {
            return false;
        }
        boolean d;
        if ((d = file.delete()))
            scannerFile(file);
        return d;
    }

    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    public boolean deleteDirs(String dirsName) {
        File dirFile = new File(getStorageDirectory() + dirsName);
        if (!dirFile.exists()) {
            return false;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }
        scannerDir(dirFile);
        return dirFile.delete();
    }

    /**
     * 创建目录
     *
     * @param dirsName 目录
     * @return
     */
    public File createDir(String dirsName) throws IOException {
        File file = new File(getStorageDirectory() + dirsName);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file;
            } else {
                throw new IOException("创建文件夹失败");
            }
        }
        scannerDir(file);
        return file;
    }

    /**
     * 在ＳＤ卡上 项目根目录 创建文件
     *
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public File createSDFile(String dirsName, String fileName) throws IOException {
        File dir = createDir(dirsName);//创建文件夹
        if (dir.exists()) {
            File file = new File(dir.getPath() + File.separator + fileName);
            if (!file.exists()) {
                if (file.createNewFile()) {//创建文件
                    scannerFile(file);
                    return file;
                } else {
                    throw new IOException("创建文件失败");
                }
            } else {
                return file;
            }
        }
        return null;
    }

    /**
     * 判断SD 卡上的文件夹内的 文件是否存在
     *
     * @param dirsName
     * @param fileName
     * @return
     */
    public boolean isFileExist(String dirsName, String fileName) {
        File file = new File(getStorageDirectory() + dirsName + File.separator + fileName);
        return file.exists();
    }

    /**
     * 获取文件
     *
     * @param dirsName
     * @param fileName
     * @return
     */
    public File getFile(String dirsName, String fileName) {
        if (isFileExist(dirsName, fileName)) {
            return new File(getStorageDirectory() + dirsName + File.separator + fileName);
        }
        return null;
    }

    /**
     * 将 InputStream保存到 SD卡中
     * 写入监听 mOutputStreamListener
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @param input    输入流
     */
    public File writeSDFormInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream outputStream = null;
        try {
            file = createSDFile(path, fileName);
            outputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int length;
            while ((length = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                if (mOutputStreamListener != null) {
                    mOutputStreamListener.onReadLength(length);
                }
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close(); // 清除缓存
            } catch (IOException e) {
                e.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
            }
        }
        return file;
    }

    /**
     * 更新sd文件列表信息
     * 告诉傻缺Android， 我放文件了，请及时显示 扫描文件 防止文件放入磁盘中，未能及时查看
     *
     * @param filePath
     */
    public void scannerFile(String... filePath) {
        MediaScannerConnection.scanFile(context, filePath, null, null);
    }

    /**
     * 程序通过发送下面的Intent启动MediaScanner服务扫描指定的文件
     * 不兼容 7.0
     */
    public void scannerFile(File file) {

//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri uri = Uri.fromFile(file);
//        intent.setData(uri);
//        context.sendBroadcast(intent);
        scannerFile(file.getPath());
    }

    /**
     * 程序通过发送下面的Intent启动MediaScanner服务扫描指定的目录：
     */
    public void scannerDir(File file) {
//        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
//        Uri uri = Uri.fromFile(file);
//        intent.setData(uri);
//        context.sendBroadcast(intent);
        scannerFile(file.getPath());
    }


    public void setOutputStreamListener(OnOutputStreamListener listener) {
        this.mOutputStreamListener = listener;
    }

    public File getResourceFile(String dirsName, String fileName, int resourcesId, int inSampleSize) {
        boolean b = isFileExist(dirsName, fileName);
        if (!b) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourcesId, options);
            File file = saveBitmap(dirsName, fileName, bitmap);
            scannerFile(file.getPath());
        }
        return getFile(dirsName, fileName);
    }


    /**
     * 写入本地速度监听
     *
     * @author abook23
     */
    public interface OnOutputStreamListener {
        /**
         * @param size 写入量
         */
        void onReadLength(int size);
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public boolean deleteAllFile(File file) {
        if (!file.exists()) {
            if (file.isFile()) {
                return file.delete();
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    return file.delete();
                }
                for (File f : childFile) {
                    deleteAllFile(f);
                }
                return file.delete();
            }
        } else {
            return file.delete();
        }
        return false;
    }
}
