package com.cooler.system.util

import com.cooler.system.entities.DeviceBean
import com.cooler.system.log
import com.tencent.mmkv.MMKV
import java.math.BigInteger

object ConvertBean {
    const val address = 0
    var mCacheInfo = MutableList<DeviceBean>(3) {
        DeviceBean().apply {
            code = when (it) {
                0 -> MMKV.defaultMMKV().decodeString("code_first", "")
                1 -> MMKV.defaultMMKV().decodeString("code_second", "")
                else -> {
                    MMKV.defaultMMKV().decodeString("code_third", "")
                }
            }
        }
    }

    /**
     * 处理写入单寄存器
     */
    fun handleOffsetData(offset: Int, value: Int,onCallBack: OnCallBack?) {
        var desIndex = when (offset - address) {
            in 0..9 -> 0
            in 10..19 -> 1
            else -> 2
        }
        when (offset - address) {
            0, 10, 20 -> {
                //温度
                val toBinaryString = Integer.toBinaryString(value)
                val toInt = BigInteger(toBinaryString, 2).toShort()
                log("index = $desIndex 当前温度是  $toInt")
                onCallBack?.onReceived(desIndex,2,toInt)
            }
            1, 11, 21 -> {
                //有无
                log("index = $desIndex  有无=$value")
                onCallBack?.onReceived(desIndex,3,value)
            }
            2, 12, 22 -> {
                //状态
                log("index = $desIndex  状态 = $value")
                onCallBack?.onReceived(desIndex,4,value)
            }
        }
    }

    /**
     * 处理数据
     * @param offset Int
     * @param array IntArray
     */
    fun handleOffsetData(offset: Int, array: IntArray,onCallBack: OnCallBack?){
        var desIndex = when (offset - address) {
            in 0..9 -> 0
            in 10..19 -> 1
            else -> 2
        }
        when (offset - address) {
            7, 17, 27 -> {
                //编号
                val code = ConvertUtil.toAscii(array)
                log("index =$desIndex 编号  $code")
                when (desIndex) {
                    0 -> MMKV.defaultMMKV().encode("code_first", code)
                    1 -> MMKV.defaultMMKV().encode("code_second", code)
                    else -> MMKV.defaultMMKV().encode("code_third", code)
                }
                onCallBack?.onReceived(desIndex,0,code)
            }
            3, 13, 23 -> {
                //姓名
                val unicode = ConvertUtil.toUnicode(array)
                val cn = ConvertUtil.unicodeToCN(unicode)
                log("index =$desIndex  姓名 = $cn")
                onCallBack?.onReceived(desIndex,1,cn)
            }
        }
    }
    interface OnCallBack{
        fun onReceived(index:Int,tag:Int,value:Any)
    }

}