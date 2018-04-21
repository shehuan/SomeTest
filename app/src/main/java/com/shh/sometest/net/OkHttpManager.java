package com.shh.sometest.net;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpManager {
    private static String path = Environment.getExternalStorageDirectory() + "/aaa_some_test/";

    private OkHttpClient.Builder builder;

    private OkHttpManager() {

        int cacheSize = 10 * 1024 * 1024; // 10M
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        Cache cache = new Cache(file, cacheSize);

        builder = new OkHttpClient.Builder()
                .cache(cache)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(new CacheInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
    }

    public static OkHttpManager getInstance() {
        return OkHttpHolder.instance;
    }

    private static class OkHttpHolder {
        private static final OkHttpManager instance = new OkHttpManager();
    }

    public void asyncGet(Callback callback) {
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(60, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .cacheControl(cacheControl)
                .build();

        builder.build().newCall(request).enqueue(callback);
    }
}
