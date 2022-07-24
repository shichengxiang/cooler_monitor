package com.cooler.system;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.tencent.mmkv.MMKV;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {

    private static Context context;
    public static Context getContext(){
        return context;
    }
    private static ExecutorService service;
    private static Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        MMKV.initialize(this);
    }
    public static ExecutorService getExecutor(){
        if(service == null){
            service= Executors.newFixedThreadPool(3);
        }
        return service;
    }
    public static void post(Runnable runnable){
        if(mainHandler == null)
            mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(runnable);
    }
    public static void postDelay(Runnable runnable, int delay){
        if(mainHandler == null)
            mainHandler= new Handler(Looper.getMainLooper());
        mainHandler.postDelayed(runnable,delay);
    }
}
