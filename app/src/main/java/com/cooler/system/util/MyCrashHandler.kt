package com.cooler.system.util

import android.content.Context
import android.os.Environment
import com.cooler.system.log
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

/**
 * 描述：MyCrashHandler
 * 创建者: shichengxiang
 * 创建时间：2023/12/8
 */
class MyCrashHandler :Thread.UncaughtExceptionHandler {

    companion object{
        private var instance:MyCrashHandler?=null
        fun getInstance():MyCrashHandler{
            if(instance == null) instance = MyCrashHandler()
            return instance!!
        }
    }
    private var mDefaultHandler:Thread.UncaughtExceptionHandler?=null
    private var error =""
    fun init(context: Context){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(t: Thread, e: Throwable) {
        e.printStackTrace()
        log("异常捕获")
        if(handleException(e)){
            //捕获到异常了
        }else{
            mDefaultHandler?.uncaughtException(t,e)
        }
        var  logPath = ""
        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            //外部存储可用
            logPath = Environment.getExternalStorageDirectory().absolutePath+File.separator+"cooler_log"
            val file = File(logPath)
            if(!file.exists()){
                file.mkdirs()
            }
            try {
                val writer = FileWriter(logPath+File.separator+"error.log",false)
                writer.write(error)
                writer.close()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }


    }
    private fun handleException(e:Throwable?):Boolean{
        if(e == null) return false
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        e.printStackTrace(printWriter)
        printWriter.close()
        error = writer.toString()
        return true
    }

}