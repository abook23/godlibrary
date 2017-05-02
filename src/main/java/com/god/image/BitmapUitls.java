package com.god.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.DisplayMetrics;

import java.io.IOException;

/**
 * Created by abook23 on 2014/9/23.
 *
 * @version 1.0
 */
public class BitmapUitls {

    public static int widthPixels;
    public static int heightPixels;

    /**
     * 主流的图片大小  适用于http 网络图片上传
     * 3M  100k
     * 900k 50k 不失真
     *
     * @param filePath
     * @return
     */
    public static Bitmap getDefaultBitmap(String filePath) {
        return getSmallBitmap(filePath, 960, 640, true);
    }

    /**
     * 压缩图片 屏幕大小压缩
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, Context context) {
        /**
         * 然而为了节约内存的消耗，这里返回的图片是一个121*162的缩略图。
         * 那么如何返回我们需要的大图呢？看上面
         * 然而存储了图片。有了图片的存储位置，能不能直接将图片显示出来呢》
         * 这个问题就设计到对于图片的处理和显示，是非常消耗内存的，对于PC来说可能不算什么，但是对于手机来说
         * 很可能使你的应用因为内存耗尽而死亡。不过还好，Android为我们考虑到了这一点
         * Android中可以使用BitmapFactory类和他的一个内部类BitmapFactory.Options来实现图片的处理和显示
         * BitmapFactory是一个工具类，里面包含了很多种获取Bitmap的方法。BitmapFactory.Options类中有一个inSampleSize，比如设定他的值为8，则加载到内存中的图片的大小将
         * 是原图片的1/8大小。这样就远远降低了内存的消耗。
         * BitmapFactory.Options op = new BitmapFactory.Options();
         * op.inSampleSize = 8;
         * Bitmap pic = BitmapFactory.decodeFile(imageFilePath, op);
         * 这是一种快捷的方式来加载一张大图，因为他不用考虑整个显示屏幕的大小和图片的原始大小
         * 然而有时候，我需要根据我们的屏幕来做相应的缩放，如何操作呢？
         *
         */
        //首先取得屏幕对象

        if (widthPixels == 0 || heightPixels == 0) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            //获取屏幕的宽和高
            widthPixels = mDisplayMetrics.widthPixels;
            heightPixels = mDisplayMetrics.heightPixels;
        }
        // Calculate inSampleSize
        //options.inSampleSize = calculateInSampleSize(options, W, H);
        return getSmallBitmap(filePath, widthPixels, heightPixels, true);
    }


    /**
     * 自定义高宽压缩
     *
     * @param filePath
     * @param Width
     * @param Height
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int Width, int Height) {
        return getSmallBitmap(filePath, Width, Height, false);
    }

    public static Bitmap getSmallBitmap(String filePath, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPurgeable = true;//用于存储Pixel的内存空间在系统内存不足时可以被回收
        options.inSampleSize = inSampleSize;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        return bm;
    }

    /**
     * 自定义高宽压缩
     *
     * @param filePath
     * @param Width
     * @param Height
     * @param rotateBitmap
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int Width, int Height, boolean rotateBitmap) {
        long t1 = System.nanoTime();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置了此属性一定要记得将值设置为false
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, Width, Height);
        long t2 = System.nanoTime();
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        if (rotateBitmap) {
            int degree = readPictureDegree(filePath);
            bm = rotateBitmap(bm, degree);
        }
        return bm;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    /**
     * 旋转
     *
     * @param bitmap
     * @param rotate
     * @return
     */
    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 压缩比例
     *
     * @param inSampleSize
     * @return
     */
    public static BitmapFactory.Options getOptions(int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;// 4 ..... 1/4 压缩
        return options;
    }


}
