package com.shh.sometest.net;

import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpManager {
    private OkHttpClient client;

    private OkHttpManager() {
        // 缓存目录
        File file = new File(Environment.getExternalStorageDirectory(), "a_cache");
        // 缓存大小
        int cacheSize = 10 * 1024 * 1024;

        client = new OkHttpClient.Builder()
                .cache(new Cache(file, cacheSize))
                .addNetworkInterceptor(new NetCacheInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpManager getInstance() {
        return OkHttpHolder.instance;
    }

    private static class OkHttpHolder {
        private static final OkHttpManager instance = new OkHttpManager();
    }

    public void asyncGet(Callback callback) {
        CacheControl cacheControl = new CacheControl.Builder()
                .maxStale(90, TimeUnit.SECONDS)
                .maxAge(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
//                .url("http://www.wanandroid.com/banner/json")
                .url("http://publicobject.com/helloworld.txt")
                .cacheControl(cacheControl)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
