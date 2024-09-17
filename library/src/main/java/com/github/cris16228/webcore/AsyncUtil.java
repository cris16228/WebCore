package com.github.cris16228.webcore;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncUtil {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    private onExecuteListener onExecuteListener;
    private onUIUpdate onUIUpdate;

    protected AsyncUtil(ExecutorService executor, Handler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    protected AsyncUtil() {
    }

    protected static AsyncUtil get() {
        return new AsyncUtil();
    }

    protected void updateUIBackground(Activity activity) {
        activity.runOnUiThread(() -> {
            onUIUpdate.updateUI();
        });
    }

    protected void onExecuteListener(onExecuteListener _onExecuteListener) {
        onExecuteListener = _onExecuteListener;
    }

    protected void onExecuteListener(onExecuteListener _onExecuteListener, onUIUpdate _onUIUpdate) {
        onExecuteListener = _onExecuteListener;
        onUIUpdate = _onUIUpdate;
    }

    protected void onUIUpdate(onUIUpdate _onUIUpdate) {
        onUIUpdate = _onUIUpdate;
    }

    protected <T> void execute(onExecuteListener<T> _onExecuteListener, OnResultListener<T> onResultListener) throws InterruptedException {
        _onExecuteListener.preExecute();
        executor.execute(() -> {
            T result = _onExecuteListener.doInBackground();
            handler.post(() -> {
                _onExecuteListener.postDelayed();
                if (onResultListener != null) {
                    onResultListener.onResult(result);
                }
                if (onUIUpdate != null) {
                    onUIUpdate.updateUI();
                }
            });
        });
    }

    protected interface OnResultListener<T> {
        void onResult(T result);
    }

    protected interface onExecuteListener<T> {

        void preExecute();

        T doInBackground();

        void postDelayed();
    }

    protected interface onUIUpdate {

        void updateUI();
    }
}

