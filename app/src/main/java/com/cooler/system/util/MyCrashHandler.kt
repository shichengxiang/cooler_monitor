package com.cooler.system.util

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.storage.StorageManager
import com.cooler.system.ErrorActivity
import com.cooler.system.Main2Activity
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
    private var CTX:Context?=null
    private var mDefaultHandler:Thread.UncaughtExceptionHandler?=null
    private var error =""
    fun init(context: Context){
        CTX=context
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

    }
    private fun handleException(e:Throwable?):Boolean{
        if(e == null) return false
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        e.printStackTrace(printWriter)
        printWriter.close()
        error = writer.toString()

        var  logPath = ""
        var srcFileName:String="cooler.txt"
        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            //外部存储可用
            logPath = Environment.getExternalStorageDirectory().absolutePath+File.separator+"cooler_log"
            val file = File(logPath)
            if(!file.exists()){
                file.mkdirs()
            }
            val srcFile = File(logPath+File.separator+srcFileName)
            if(!srcFile.exists()){
                srcFile.mkdir()
            }
            try {
//                val fw :FileWriter? = FileWriter(logPath+File.separator+srcFileName,false)
//                fw?.write(error)
//                fw?.close()
                val intent = Intent(CTX,Main2Activity::class.java)
                intent.putExtra("error",error)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                CTX?.startActivity(intent)
                System.exit(0)

            }catch (e:Exception){
                e.printStackTrace()
            }finally {
//                if(CTX!=null) copyToUPlate(CTX!!,logPath+File.separator+srcFileName)
            }
        }
        return true
    }
    private fun getExtreranelUsbPaths(context: Context):Array<String>?{
        try {
            val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            val paths = StorageManager::class.java.getMethod("getVolumePaths",null).invoke(sm,null) as? Array<String>
            val result = arrayListOf<String>()
            if(!paths.isNullOrEmpty()){
                val size = paths.size
                for (i in 0 until size){
                    val p = paths[i]
                    val f = File(p)
                    if(p != getSDPath() && f.exists()){
                        if(f.freeSpace>100){
                            result.add(p)
                            log(p)
                        }
                    }
                }
            }
            return result.toTypedArray()
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null

    }
    private fun getSDPath():String?{
        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            return Environment.getExternalStorageDirectory().toString()
        }
        return null
    }

    private fun copyToUPlate(context: Context,srcPath:String){
        try {
            val u = getExtreranelUsbPaths(context)
            if(!u.isNullOrEmpty()){
                val desPath = u[0]+"/cooler/"
                val desFile = File(desPath)
                if(!desFile.exists()){
                    desFile.mkdirs()
                }
                //复制到U盘
                Runtime.getRuntime().exec("cp $srcPath $desPath")
                Runtime.getRuntime().exec("ls -l $desPath")
            }
        }catch (e:Exception){}
    }

}