package com.cooler.system.util

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.cooler.system.App
import com.cooler.system.BuildConfig
import com.tencent.mmkv.MMKV
import java.lang.NumberFormatException
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object Util {

    private var mToast: Toast? = null
    private var mainHandler: Handler? = null
    private var toastMsg: String? = null

    @JvmStatic
    fun log(msg: String) {
        if (BuildConfig.DEBUG) Log.i("cooler== ", msg)
    }

    /**
     * 时间格式
     * @param time Long
     * @param format String
     * @return String
     */
    fun formatTime(time: Long?, format: String = "MM-dd HH:mm:ss"): String {
        if (time == null) return ""
        var simple = ""
        try {
            simple = SimpleDateFormat(format).format(Date(time))
        } catch (e: Exception) {
        }
        return simple
    }

    @JvmStatic
    fun toast(msg: String, time: Int = Toast.LENGTH_SHORT) {
        synchronized(Any::class.java) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                if (mainHandler == null) mainHandler = Handler(Looper.getMainLooper())
                mainHandler?.post {
                    show(msg, time)
                }
            } else {
                show(msg, time)
            }
        }
    }

    private fun show(msg: String, time: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(App.getContext(), msg, time)
        } else {
            mToast?.setText(msg)
        }
        toastMsg = msg
        mToast?.show()
    }

    /**
     * 获取ip
     */
    fun getLocalIp(context:Context):String?{
        val wm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val connectionInfo = wm.connectionInfo
        val ad = connectionInfo.ipAddress
        return "${ad and 0xff}.${ad shr 8 and 0xff}.${ad shr 16 and 0xff}.${ad shr 24 and 0xff}"
    }

    fun getUniqueID(context: Context): String? {
        val uuid = MMKV.defaultMMKV().decodeString("uuid", "")
        if (!uuid.isNullOrEmpty()) {
            return uuid
        }
        var bf = StringBuffer()
        try {
            bf.append(
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            )
                .append(Build.ID)
                .append(Build.MANUFACTURER)
                .append(Build.DEVICE)
                .append(Build.MODEL)
                .append(Build.HARDWARE)
        } catch (exception: Exception) {
            exception.printStackTrace();
        }
        var str = bf.toString().trim()
        var b = Base64.encodeToString(str.toByteArray(Charsets.UTF_8), Base64.DEFAULT).trim()
        var res = if (b.length > 10) b.substring(0, 10) else b
        res = res.uppercase(Locale.ROOT)
        MMKV.defaultMMKV().encode("uuid", res)
        return res
    }

    /**
     * 保存host
     */
    fun saveHost(ip:String,port:String){
        MMKV.defaultMMKV().encode("ip", ip)
        MMKV.defaultMMKV().encode("port",port)
    }

    /**
     * 保存间隔时间
     */
    fun savePerTime(time:Int){
        MMKV.defaultMMKV().encode("pertime",time)
    }
    fun saveDeviceCodes(vararg codes:String){
        val buffer = StringBuffer()
        codes.forEach {
            if(!it.isNullOrEmpty()) buffer.append(it).append(",")
        }
        if(buffer.length>1) buffer.deleteCharAt(buffer.length-1)
        MMKV.defaultMMKV().encode("codes",buffer.toString())
    }
    fun getHost():Pair<String?,String?>{
        val first= MMKV.defaultMMKV().decodeString("ip")
        val second = MMKV.defaultMMKV().decodeString("port")
        return Pair(first,second)
    }
    fun getHostStr():String{
        val first= MMKV.defaultMMKV().decodeString("ip")?:""
        val second = MMKV.defaultMMKV().decodeString("port")
        var url = if(first.startsWith("http://") || first.startsWith("https://")){
            if(!second.isNullOrEmpty()){
                "$first:$second"
            }else{
                first
            }
        }else{
            if(!second.isNullOrEmpty()){
                "http://$first:$second"
            }else{
                "http://$first"
            }
        }
        if(!url.endsWith("\\/")){
            url= "$url/"
        }
        return url
    }
    fun getPerTime():Int{
        return MMKV.defaultMMKV().decodeInt("pertime",3)
    }
    fun getDeviceCodes():Array<String>?{
        val s = MMKV.defaultMMKV().decodeString("codes", "")
        val split = s?.split(",")?.toTypedArray()
        return split
    }

}