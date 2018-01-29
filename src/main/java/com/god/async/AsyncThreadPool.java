package com.god.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;

import com.god.listener.AsyncTaskListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by abook23 on 2016/5/27.
 * 1
 */
public abstract class AsyncThreadPool<Params, Progress, Result> {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;

    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;
    private static final int MESSAGE_POST_CALL_BACK = 0x3;

    protected Map<String, Object> params = new HashMap<>();
    private AsyncTaskListener taskListener;
    private int requestCode;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask#" + mCount.getAndIncrement());
        }
    };

    private static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), sThreadFactory);

    /**
     * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
     *
     * @return
     */
    public synchronized ExecutorService getThreadPool() {
        return THREAD_POOL_EXECUTOR;
    }

    /**
     * 取消正在下载的任务
     */
    public synchronized void cancelTask() {
        THREAD_POOL_EXECUTOR.shutdownNow();
    }


    public void setAsyncTaskListener(AsyncTaskListener taskListener) {
        this.taskListener = taskListener;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_POST_PROGRESS:
                    Result result = (Result) msg.obj;
                    onPostExecute(result);
                    break;
                case MESSAGE_POST_RESULT:
                    Progress[] progresses = (Progress[]) msg.obj;
                    onProgressUpdate(progresses);
                    break;
                case MESSAGE_POST_CALL_BACK:
                    if (taskListener != null) {
                        taskListener.onTaskCallBack(msg.arg1, msg.obj);
                    }
                    break;
            }

        }
    };

    public void execute(final Params... params) {
        onPreExecute();
        getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Result result = doInBackground(params);
                handler.obtainMessage(MESSAGE_POST_PROGRESS, result).sendToTarget();
            }
        });
    }


    protected void setCallAsync(Object... data) {
        if (taskListener != null) {
            taskListener.onTaskCallBack(requestCode, data);
        }
    }

    protected void setCallPost(Object... data) {
        handler.obtainMessage(MESSAGE_POST_CALL_BACK, requestCode, 0, data).sendToTarget();
    }


    /**
     * 线程执行之前
     */
    @MainThread
    protected void onPreExecute() {
    }

    protected abstract Result doInBackground(Params... _params);

    @SuppressWarnings({"UnusedDeclaration"})
    @MainThread
    protected void onProgressUpdate(Progress... values) {
    }

    protected final void publishProgress(Progress... values) {
        handler.obtainMessage(MESSAGE_POST_PROGRESS, values).sendToTarget();
    }

    /**
     * 成功
     */
    protected void onPostExecute(Result result) {
    }

}
