package com.shh.sometest.net;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpManager {
    private String path = Environment.getExternalStorageDirectory() + "/aaa_some_test/";

    private OkHttpClient.Builder builder;
    Request request;

    private OkHttpManager() {

        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        Cache cache = new Cache(file, cacheSize);

        builder = new OkHttpClient.Builder()
                .cache(cache)
                .retryOnConnectionFailure(true)
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void run(Callback callback) {
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(60, TimeUnit.SECONDS)
                .build();

        request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .cacheControl(cacheControl)
                .build();

        builder.build().newCall(request).enqueue(callback);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                CacheControl cacheControl = new CacheControl.Builder()
//                        .maxAge(60, TimeUnit.SECONDS)
//                        .build();
//
//                Request request = new Request.Builder()
//                        .url("http://publicobject.com/helloworld.txt")
//                        .cacheControl(cacheControl)
//                        .build();
//
//                try (Response response1 = builder.build().newCall(request).execute()) {
//                    if (!response1.isSuccessful())
//                        throw new IOException("Unexpected code " + response1);
//
//                    System.out.println("Response 1 response:          " + response1);
//                    System.out.println("Response 1 cache response:    " + response1.cacheResponse());
//                    System.out.println("Response 1 network response:  " + response1.networkResponse());
//
//                    if (response1.cacheResponse().body() != null){
//                        System.out.println("Response 1 cache response string:    " + response1.cacheResponse().body().string());
//                        response1.cacheResponse().body().close();
//                    }
//                } catch (IOException e) {
//
//                }
//
//                try (Response response2 = builder.build().newCall(request).execute()) {
//                    if (!response2.isSuccessful())
//                        throw new IOException("Unexpected code " + response2);
//                    System.out.println("Response 2 response:          " + response2);
//                    System.out.println("Response 2 cache response:    " + response2.cacheResponse());
//                    System.out.println("Response 2 network response:  " + response2.networkResponse());
//                } catch (Exception e) {
//
//                }
//            }
//        }).start();

    }
}
