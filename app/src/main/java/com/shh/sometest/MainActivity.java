package com.shh.sometest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView title;

    @OnClick(R.id.btn_submit)
    public void submit() {
        startActivity(new Intent(this, TestActivity.class));
    }

    @OnClick(R.id.btn_cache)
    public void cacheTest() {
        startActivity(new Intent(MainActivity.this, CacheActivity.class));
    }

    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
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
