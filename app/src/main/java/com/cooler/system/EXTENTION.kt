package com.cooler.system

import android.content.res.Resources
import android.util.Log
import com.cooler.system.util.Util
import java.lang.Exception

fun log(msg: String, tag: String? = "") {
    Log.i("cooler== $tag", msg)
}
fun toast(msg:String){
    Util.toast(msg)
}
fun toPx(dp:Float): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()
fun String?.float():Float{
    var res= 0f
    try {
        res = this?.toFloat()?:0f
    }catch (e:Exception){
    }
    return res
}