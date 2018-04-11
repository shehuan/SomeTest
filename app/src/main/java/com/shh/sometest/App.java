package com.shh.sometest;

import android.app.Application;

import org.greenrobot.eventbus.EventBus;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
    }
}
