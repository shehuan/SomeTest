package com.shh.sometest;

import android.Manifest;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shh.sometest.net.OkHttpManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class CacheActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);
        context = this;
        findViewById(R.id.btn_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();
            }
        });
    }

    @AfterPermissionGranted(100)
    private void requestStoragePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            OkHttpManager.getInstance().asyncGet(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("failure", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (response.networkResponse() != null) {
                            Log.e("network", response.body().string().length() + "");
                        } else if (response.cacheResponse() != null) {
                            if (Utils.isNetworkAvailable(context)) {
                                Log.e("cache", response.body().string().length() + "");
                            } else {
                                Log.e("cache(no network)", response.body().string().length() + "");
                            }
                        }
                    }
                }
            });
        } else {
            EasyPermissions.requestPermissions(this, "NEED_WRITE_READ_EXTERNAL_STORAGE", 100, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (100 == requestCode) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this)
                        .setTitle("TIP")
                        .setRationale("NEED_WRITE_READ_EXTERNAL_STORAGE")
                        .build()
                        .show();
            }
        }
    }
}
