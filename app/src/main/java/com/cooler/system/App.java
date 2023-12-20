package com.cooler.system;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

import com.cooler.system.util.MyCrashHandler;
import com.cooler.system.util.Util;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xcrash.XCrash;

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
        CrashReport.initCrashReport(getApplicationContext());
        MMKV.initialize(this);
        sWidth = Math.max(Resources.getSystem().getDisplayMetrics().widthPixels,Resources.getSystem().getDisplayMetrics().heightPixels);
        sHeight = Math.min(Resources.getSystem().getDisplayMetrics().widthPixels,Resources.getSystem().getDisplayMetrics().heightPixels);
        MyCrashHandler.Companion.getInstance().init(context);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        XCrash.InitParameters parameters = new XCrash.InitParameters();
        parameters.setLogDir(base.getFilesDir()+"/cooler");
        String path = base.getFilesDir()+"/cooler";
        int result = XCrash.init(this, parameters);
        Util.log("log == "+path + " code = "+result);
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
