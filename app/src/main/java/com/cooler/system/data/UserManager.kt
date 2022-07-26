package com.cooler.system.data

import android.content.Context
import android.util.Base64
import com.cooler.system.util.CryptoUtil
import com.cooler.system.util.Util
import com.tencent.mmkv.MMKV

object UserManager {
    /**
     * 检查是否激活
     * @return Boolean
     */
    fun isActived(context: Context):Boolean{
        val ac = getActiveCode()
        return  !ac.isNullOrEmpty() || isValidCode(Util.getUniqueID(context),ac) //!ac.isNullOrEmpty() &&  5371850252
    }

    /**
     * 检测激活码是否有效
     * @param des String
     * @param checkString String
     * @return Boolean
     */
    fun isValidCode(des: String?,checkString: String?):Boolean{
        val ac = CryptoUtil.generateActive(des?:"")
        return !checkString.isNullOrEmpty() && ac == checkString
    }

    /**
     * 保存active
     */
    fun saveActiveCode(active:String){
        MMKV.defaultMMKV().encode("isActived",active)
    }

    /**
     * 获取保存的active
     */
    fun getActiveCode():String{
        return MMKV.defaultMMKV().decodeString("isActived","")?:""
    }

}