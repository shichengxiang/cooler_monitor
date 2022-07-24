package com.cooler.system.data

import android.content.Context
import android.util.Base64
import com.cooler.system.util.Util
import com.tencent.mmkv.MMKV

object UserManager {
    /**
     * 检查是否激活
     * @return Boolean
     */
    fun isActived(context: Context):Boolean{
        val ac = MMKV.defaultMMKV().decodeString("isActived","")
        return  isValidCode(Util.getUniqueID(context),ac)
    }

    /**
     * 检测激活码是否有效
     * @param des String
     * @param checkString String
     * @return Boolean
     */
    fun isValidCode(des: String?,checkString: String?):Boolean{
        val ac = genarateActiveCode(des?:"")
        return !checkString.isNullOrEmpty() && ac == checkString
    }

    /**
     * 生成激活码
     * @param uid String 唯一标识
     * @return String 转化为10位数字
     */
    fun genarateActiveCode(uid:String):String{
        Base64.encode(uid.toByteArray(Charsets.UTF_8),Base64.DEFAULT)
        return uid?:""
    }

}