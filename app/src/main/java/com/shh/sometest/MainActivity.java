package com.shh.sometest;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.shh.sometest.net.OkHttpManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tv_title)
    TextView title;

    @OnClick(R.id.bt_submit)
    public void submit() {
        startActivity(new Intent(this, TestActivity.class));
    }

    @OnClick(R.id.bt_net)
    public void net() {
        requestStoragePermission();
    }

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @AfterPermissionGranted(100)
    private void requestStoragePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            OkHttpManager.getInstance().asyncGet(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("response", "failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Log.e("response", response.body().string());
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

    @Subscribe
    public void changeText(String content) {
        title.setText(content);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
