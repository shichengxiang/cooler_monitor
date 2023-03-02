package com.cooler.system.util;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述：MyCrashHandler
 * 创建者: shichengxiang
 * 创建时间：2023/3/2
 */
public class MyCrashHandler implements Thread.UncaughtExceptionHandler{
    //系统默认UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private static volatile MyCrashHandler myCrashHandler;
    private MyCrashHandler(){}
    public static MyCrashHandler getInstance(){
        if(myCrashHandler == null){
            synchronized (MyCrashHandler.class){
                if(myCrashHandler == null){
                    myCrashHandler = new MyCrashHandler();
                }
            }
        }
        return myCrashHandler;
    }
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        if (!handleExample(e) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理 目的是判断异常是否已经被处理
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {//Sleep 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                Log.d("MyCrashHandler", "uncaughtException: "+e1.getMessage());
            }catch (Exception e2){
                e2.printStackTrace();
                Log.d("MyCrashHandler", "uncaughtException: "+e2.getMessage());
            }
/** 关闭App 与下面的restartApp重启App保留一个就行 看你需求 **/
            // 如果不关闭程序,会导致程序无法启动,需要完全结束进程才能重新启动
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(1);
//            restartApp();
        }
    }
    public void init(Context ctx){
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    /**
     * 自定义错误处理,收集错误信息 将异常信息保存 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleExample(Throwable ex) {
        // 如果已经处理过这个Exception,则让系统处理器进行后续关闭处理
        if (ex == null)
            return false;

//        new Thread(() -> {
//            // Toast 显示需要出现在一个线程的消息队列中
//            Looper.prepare();
//            Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出", Toast.LENGTH_SHORT).show();
//            Looper.loop();
//        }).start();

        //将异常记录到本地的文件中
        saveCrashInfoToFile(ex);
        return true;
    }
    /**
     * 重启应用
     */
    public void restartApp() {

        // 重启应用
        mContext.startActivity(mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName()));
        //干掉当前的程序
        android.os.Process.killProcess(android.os.Process.myPid());

    }
    /**
     * 保存错误信息到文件中
     *
     * @param ex
     */
    private void saveCrashInfoToFile(Throwable ex) {
        //获取错误原因
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable exCause = ex.getCause();
        while (exCause != null) {
            exCause.printStackTrace(printWriter);
            exCause = exCause.getCause();
        }
        printWriter.close();
        // 错误日志文件名称
        String fileName = timeStampDate()+ ".log";
//        fileutil
        // 判断sd卡可正常使用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件存储位置
            String path = Environment.getExternalStorageDirectory().getPath() + "/cooler/";
            File fl = new File(path);
            //创建文件夹
            if (!fl.exists()) {
                fl.mkdirs();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
                fileOutputStream.write(writer.toString().getBytes());
                fileOutputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }catch (Exception e2){
                e2.printStackTrace();
                Log.d("MyCrashHandler", "saveCrashInfoToFile: "+e2.getMessage());
            }
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     * 格式 - 2021-08-05 13:59:05
     */
    public  String timeStampDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd ");
        return sdFormatter.format(nowTime);
    }
}
