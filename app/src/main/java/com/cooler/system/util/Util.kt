package com.cooler.system.util

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.cooler.system.App
import com.cooler.system.BuildConfig
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
        if (BuildConfig.DEBUG)
            Log.d("cooler == ", msg)
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
                if (mainHandler == null)
                    mainHandler = Handler(Looper.getMainLooper())
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

    fun getUniqueID(context: Context): String? {
        var id: String? = null
        val androidId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        if (androidId != null && "9774d56d682e549c" != androidId) {
            try {
                val uuid = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
                id = uuid.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return if (id.isNullOrEmpty()) UUID.randomUUID().toString() else id
    }
}