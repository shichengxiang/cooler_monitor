package com.cooler.system.util

import android.util.Base64
import com.cooler.system.log
import java.security.MessageDigest

object CryptoUtil {
    private const val md5_key = "NWFk@&e1yDG9J2HackY!WS"
    private const val sha1_key = "6@s%\$NWCWZn&*1EWh8S3NPe"
    fun generateActive(uid: String): String {
        log("android_id == $uid")
        val e = Base64.encodeToString(uid.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        log("base64 = $e")
        var md5Str =
            byteArr2String(MessageDigest.getInstance("MD5").digest((e + md5_key).toByteArray()))
        val pre10 = if (md5Str.length >= 10) md5Str.substring(0, 10) else md5Str
//        string2ASCII(pre10)
        val last15 = if (md5Str.length >= 15) md5Str.substring(md5Str.length - 15) else md5Str
//        string2ASCII(last15)
        val shaStr =byteArr2String(MessageDigest.getInstance("SHA-1")
            .digest((string2ASCII(pre10) + sha1_key + string2ASCII(last15)).toByteArray(Charsets.UTF_8)))
        log("加密 sha1 =$shaStr")
        val ascii = string2ASCII(shaStr)
        log("ascii= $ascii")
        val res = ascii.substring(0, 2) + ascii.substring(20, 22) + ascii.substring(
            39,
            41
        ) + ascii.substring(55, 57) + ascii.substring(ascii.length - 2)
        log("cypto res= $res")
        return res
    }

    fun string2Hex(str: String): String {
        return str.map {
            it.digitToInt(16).toByte()
        }.toByteArray().toString()
    }
    fun byteArr2String(bs: ByteArray):String{
        val buffer=StringBuffer()
        bs.forEach {
            buffer.append(it)
        }
        return buffer.toString()
    }

    //    fun string2ASCII(str:String):String{
//        return str.toByteArray(Charsets.UTF_8).map {
//            it.toInt().toByte()
//        }.toByteArray().contentToString()
//    }
    fun string2ASCII(str: String): String {
        val buffer = StringBuffer()
        str.toByteArray(Charsets.UTF_8).forEach {
            buffer.append(it)
        }
        return buffer.toString()
    }
}