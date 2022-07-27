package com.cooler.system.util

import android.util.Base64
import com.cooler.system.log
import java.security.MessageDigest
import kotlin.experimental.and

object CryptoUtil {
    private const val md5_key = "NWFk@&e1yDG9J2HackY!WS"
    private const val sha1_key = "6@s%\$NWCWZn&*1EWh8S3NPe"
    private val HEX_CHAR = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    )
    fun generateActive(uid: String): String {
        log("android_id == $uid")
        val base64Str = Base64.encodeToString(uid.toByteArray(Charsets.UTF_8), Base64.DEFAULT).trim()
//        log("base64 = $base64Str")
        val md = MessageDigest.getInstance("MD5")
//        Base64.encodeToString(md.digest(base64Str.toByteArray(Charsets.UTF_8)))
        var md5Str =
            byte2String(md.digest((base64Str + md5_key).toByteArray(Charsets.UTF_8)))
//        log("md5 = $md5Str")
        val pre10 = if (md5Str.length >= 10) md5Str.substring(0, 10) else md5Str
//        string2ASCII(pre10)
        val last15 = if (md5Str.length >= 15) md5Str.substring(md5Str.length - 15) else md5Str
//        string2ASCII(last15)
        val shaStr = byte2String(MessageDigest.getInstance("SHA-1")
            .digest((string2ASCII(pre10) + sha1_key + string2ASCII(last15)).toByteArray(Charsets.UTF_8)))
//        log("加密 sha1 =$shaStr")
        val ascii = string2ASCII(shaStr)
//        log("ascii= $ascii")
        val res = ascii.substring(0, 2) + ascii.substring(20, 22) + ascii.substring(
            39,
            41
        ) + ascii.substring(55, 57) + ascii.substring(ascii.length - 2)
//        log("cypto res= $res")
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
//            if ((Integer.parseInt()) < 0x10) buffer.append("0")
//
//            buffer.append(Integer.toHexString(it and  0xFF.toByte()))
            buffer.append(it)
        }
        return buffer.toString()
    }
    fun byte2String(hash: ByteArray): String {
        val hex: StringBuilder = StringBuilder(hash.size * 2)
        for (b in hash) {
            if (b.toInt() and 0xFF < 0x10) {
                hex.append("0")
            }
            hex.append(Integer.toHexString(b.toInt() and 0xFF))
        }
        return hex.toString()
    }
    fun bytes2HexString(bs: ByteArray): String {
        var sb : StringBuffer = StringBuffer()
        for (b in bs) {
            var i :Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0$hexString"//如果是一位的话，补0
            }
            sb.append(hexString)
        }
        return sb.toString()
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