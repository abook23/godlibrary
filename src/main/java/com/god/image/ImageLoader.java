package com.god.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.god.util.Encode;
import com.god.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by abook23 on 2016/5/16.
 */
public class ImageLoader {

    private final static String TAG = ImageLoader.class.toString();
    private final static String LOCAL_CACHE_DIR_NAME = "/cache/image";//
    private final static String HTTP_CACHE_DIR_NAME = "/download";//
//    /**
//     * sd卡的根目录
//     */
//    private String mSdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    private int POOL_SIZE = 2;
    // 获取当前系统的CPU 数目
    private int cpuNums = Runtime.getRuntime().availableProcessors();
    private int nThreads = POOL_SIZE * cpuNums + 1;
    // ExecutorService通常根据系统资源情况灵活定义线程池大小
    private ExecutorService executorService;

    private LruCache<String, Bitmap> mMemoryCache;
    private Context mContext;
    private FileUtils fileUtils;
    private ViewGroup viewGroup;
    private static ImageLoader imageLoader;

    private ImageLoader(Context mContext) {
        this(mContext, null);
    }

    private ImageLoader(Context mContext, String dirNameByRoot) {
        this.mContext = mContext;
        if (dirNameByRoot != null && dirNameByRoot.length() > 0)
            fileUtils = new FileUtils(mContext, dirNameByRoot);
        else
            fileUtils = new FileUtils(mContext);
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 给LruCache分配1/8 4M
        int mCacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
            // 必须重写此方法，来测量Bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public static ImageLoader build(Context context) {
        if (imageLoader == null)
            imageLoader = new ImageLoader(context);
        return imageLoader;
    }

    public static ImageLoader build(Context context, String rootDirName) {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(context, rootDirName);
        }
        return imageLoader;
    }

    public static void bindBitmap(ImageView imageView, String uri, int width, int height) {
        if (imageLoader == null)
            imageLoader = new ImageLoader(imageView.getContext());
        imageLoader.bindBitmap(uri, imageView, width, height, 0, 0, 0, false);
    }


    public static void bindWithTag(ViewGroup parent, ImageView imageView, String uri, int width, int height) {
        if (imageLoader == null)
            imageLoader = new ImageLoader(parent.getContext());
        imageLoader.bindBitmap(uri, null, width, height, 0, 0, 0, false);
    }

    public static void bindBitmap(ImageView imageView, String uri, DisplayImageOptions options) {
        if (options == null)
            options = DisplayImageOptions.builder();
        if (options.cacheDirName != null && options.cacheDirName.length() > 0) {
            if (imageLoader == null)
                imageLoader = new ImageLoader(imageView.getContext(), options.cacheDirName);
        } else {
            if (imageLoader == null)
                imageLoader = new ImageLoader(imageView.getContext());
        }
        imageLoader.bindBitmap(uri, imageView, options.width, options.height, 0, options.loadImage, options.errorImage, options.isRound);
    }

    /**
     * @param parent
     * @param imageView
     * @param uri
     * @param options
     */
    public static void bindWithTag(ViewGroup parent, ImageView imageView, String uri, DisplayImageOptions options) {
        if (options == null)
            options = DisplayImageOptions.builder();
        if (options.cacheDirName != null && options.cacheDirName.length() > 0) {
            if (imageLoader == null)
                imageLoader = new ImageLoader(parent.getContext(), options.cacheDirName);
        } else {
            if (imageLoader == null)
                imageLoader = new ImageLoader(parent.getContext());
        }
        imageLoader.viewGroup = parent;
        imageLoader.bindBitmap(uri, imageView, options.width, options.height, 0, options.loadImage, options.errorImage, options.isRound);
    }

    public static void clearAllCache() {
        if (imageLoader != null)
            imageLoader.clearCache();
    }


    public static void cancelAllTask() {
        if (imageLoader != null)
            imageLoader.cancelTask();
    }

    public void clearCache() {
        if (mMemoryCache != null) {
            if (mMemoryCache.size() > 0) {
                Log.d("CacheUtils", "mMemoryCache.size() " + mMemoryCache.size());
                mMemoryCache.evictAll();
            }
            //mMemoryCache = null;
        }
    }

    /**
     * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
     *
     * @return
     */
    private ExecutorService getThreadPool() {
        if (executorService == null) {
            synchronized (ExecutorService.class) {
                if (executorService == null) {
                    executorService = Executors.newFixedThreadPool(nThreads);
                }
            }
        }
        return executorService;
    }

    /**
     * 取消正在下载的任务
     */
    public synchronized void cancelTask() {
        Log.d(TAG, "取消下载");
        mHandler.removeCallbacksAndMessages(null);
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
            getThreadPool();
        }
    }

    /**
     * 添加到硬件缓存
     *
     * @param key
     * @param bitmap
     * @return 成功 true
     */
    private boolean setLruCache(String key, Bitmap bitmap) {
        if (getLruCache(key) == null && bitmap != null) {
            mMemoryCache.put(key, bitmap);
            return true;
        }
        return false;
    }

    /**
     * 获取硬件缓存图片
     *
     * @param key
     * @return
     */
    private Bitmap getLruCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     *
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    private File setSDCache(String fileName, Bitmap bitmap) throws IOException {
        if (bitmap == null) {
            return null;
        }
        setLruCache(fileName, bitmap);//保存到硬件缓存
        return fileUtils.saveBitmap(LOCAL_CACHE_DIR_NAME, fileName, bitmap);
    }

    /**
     * 获取SD 缓存图片
     *
     * @return
     */
    private Bitmap getSDCache(String fileName) {
        if (fileUtils.isFileExist(LOCAL_CACHE_DIR_NAME, fileName) && fileUtils.getFileSize(LOCAL_CACHE_DIR_NAME, fileName) != 0) {
            Bitmap bitmap = fileUtils.getBitmap(LOCAL_CACHE_DIR_NAME, fileName);
            return bitmap;
        }
        return null;
    }


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageBean imageBean = (ImageBean) msg.obj;
            if (imageBean.iv != null) {
                if (imageBean.bitmap != null) {
                    imageBean.iv.setImageBitmap(imageBean.bitmap);
                } else {
                    if (imageBean.errorImage > 0)
                        imageBean.iv.setImageResource(imageBean.errorImage);
                }
            } else {
                ImageView imageView = (ImageView) viewGroup.findViewWithTag(imageBean.uri);
                if (imageView != null) {
                    if (imageBean.bitmap != null) {
                        imageView.setImageBitmap(imageBean.bitmap);
                    } else {
                        if (imageBean.errorImage > 0)
                            imageView.setImageResource(imageBean.errorImage);
                    }
                }
            }
        }
    };

    /**
     * @param uri          图片地址
     * @param imageView    imageView
     * @param inSampleSize 缩比 exp:2 缩小 1/2
     * @param isRound      圆形
     */
    public void bindBitmap(String uri, ImageView imageView, int inSampleSize, boolean isRound) {
        bindBitmap(uri, imageView, 0, 0, inSampleSize, 0, 0, isRound);
    }

    /**
     * @param uri       图片地址
     * @param imageView imageView
     * @param width     width
     * @param height    height
     * @param isRound   圆形
     */
    public void bindBitmap(String uri, ImageView imageView, int width, int height, boolean isRound) {
        bindBitmap(uri, imageView, width, height, 0, 0, 0, isRound);
    }



    /**
     * 根据屏幕分辨率 压缩
     *
     * @param uri       图片地址
     * @param imageView imageView
     */
    public void bindBitmap(String uri, ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0, 0, 0, 0, false);
    }

    /**
     * @param uri       图片地址
     * @param imageView imageView
     * @param width     width
     * @param height    height
     */
    public void bindBitmap(String uri, ImageView imageView, int width, int height) {
        bindBitmap(uri, imageView, width, height, 0, 0, 0, false);
    }

    /**
     * @param uri       地址
     * @param imageView view
     * @param isRound   圆角
     */
    public void bindBitmap(String uri, ImageView imageView, boolean isRound) {
        bindBitmap(uri, imageView, 0, 0, 0, 0, 0, isRound);
    }

    public void bindBitmap(String uri, ImageView imageView, int loadImage,
                           int errorImage, int inSampleSize, boolean isRound) {
        bindBitmap(uri, imageView, 0, 0, inSampleSize, loadImage, errorImage, isRound);
    }

    public void bindBitmap(String uri, ImageView imageView, int loadImage, int errorImage, int width,
                           int height, boolean isRound) {
        bindBitmap(uri, imageView, width, height, 0, loadImage, errorImage, isRound);
    }

    private void bindBitmap(final String uri, final ImageView imageView, final int width,
                            final int height, final int inSampleSize, int loadImage,
                            final int errorImage, final boolean isRound) {
        if (imageView != null) {
            Bitmap bitmap = getCacheBitmap(uri, width, height, inSampleSize, isRound);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                return;
            } else {
                imageView.setImageResource(loadImage);
            }
        }
        getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri, width, height, inSampleSize, isRound);
                ImageBean imageBean = new ImageBean();
                Message msg = Message.obtain();
                imageBean.iv = imageView;
                imageBean.uri = uri;
                imageBean.bitmap = bitmap;
                imageBean.errorImage = errorImage;
                msg.obj = imageBean;
                mHandler.sendMessage(msg);
            }
        });
    }

    private Bitmap loadBitmap(String uri, int width, int height, int inSampleSize, boolean isRound) {
        String fileType = uri.substring(uri.lastIndexOf("."));
        String key = getUriToKey(uri, width, height, inSampleSize, isRound) + fileType;
        Bitmap bitmap = getLruCache(key);//硬件缓存图片

        if (bitmap == null) {
            bitmap = getSDCache(key);//获取sdk 缓存图片
            setLruCache(key, bitmap);
        }
        if (bitmap == null) {
            if (uri.startsWith("http://") || uri.startsWith("https://")) {
                if (width > 0 && height > 0) {
                    bitmap = loadBitmapForHttp(uri, width, height);//网络图片
                } else {
                    bitmap = loadBitmapForHttp(uri, inSampleSize);//网络图片
                }
            } else {
                if (width > 0 && height > 0) {
                    bitmap = BitmapUitls.getSmallBitmap(uri, width, height);
                } else if (inSampleSize == 0) {
                    bitmap = BitmapUitls.getSmallBitmap(uri, mContext);
                } else if (inSampleSize > 1) {
                    bitmap = BitmapUitls.getSmallBitmap(uri, inSampleSize);
                }

            }
            if (bitmap != null) {
                try {
                    if (isRound) {
                        Bitmap bm_r = BitmapUitls.toRoundBitmap(bitmap);
                        bitmap = bm_r;
                        bm_r.recycle();
                    }
                    setSDCache(key, bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    private Bitmap getCacheBitmap(String uri, int width, int height, int inSampleSize, boolean isRound) {
        String fileType = uri.substring(uri.lastIndexOf("."));
        String key = getUriToKey(uri, width, height, inSampleSize, isRound) + fileType;
        return getLruCache(key);//硬件缓存图片
    }

    private Bitmap loadBitmapForHttp(String url, int width, int height) {
        File file = downloadFileForUrl(url);
        if (file == null)
            return null;
        return BitmapUitls.getSmallBitmap(file.getPath(), width, height);
    }

    private Bitmap loadBitmapForHttp(String url, int inSampleSize) {
        File file = downloadFileForUrl(url);
        if (file == null)
            return null;
        return BitmapUitls.getSmallBitmap(file.getPath(), inSampleSize);
    }

    public File downloadFileForUrl(String url) {
        try {
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            File file = fileUtils.createSDFile(HTTP_CACHE_DIR_NAME, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            downloadFileForUrl(url, fileOutputStream);
            fileUtils.scannerFile(file);
            Log.d(TAG, file.getPath());
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void downloadFileForUrl(String urlString, FileOutputStream fileOutputStream) {
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            in = urlConnection.getInputStream();
            urlConnection.getContentLength();

            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private String getUriToKey(String uri, int width, int height, int inSampleSize, boolean isRound) {
        StringBuilder sb = new StringBuilder();
        sb.append(Encode.getEncode("md5", uri));
        sb.append("-");
        if (width > 0 && height > 0) {
            sb.append(width).append("x").append(height);
        } else {
            sb.append(inSampleSize);
        }
        if (isRound)
            sb.append("_r");//圆形的图片
        return sb.toString();
    }

    private class ImageBean {
        private ImageView iv;
        private Bitmap bitmap;
        private int errorImage;
        private String uri;
    }
}
