package com.cooler.system;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

import com.gyf.immersionbar.ImmersionBar;
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
    private static int sWidth=0;
    private static int sHeight=0;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        MMKV.initialize(this);
        sWidth = Math.max(Resources.getSystem().getDisplayMetrics().widthPixels,Resources.getSystem().getDisplayMetrics().heightPixels);
        sHeight = Math.min(Resources.getSystem().getDisplayMetrics().widthPixels,Resources.getSystem().getDisplayMetrics().heightPixels);
    }
    public static int getScreenWidth(){
        return sWidth;
    }
    public static int getScreenHeight(){
        return sHeight;
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
