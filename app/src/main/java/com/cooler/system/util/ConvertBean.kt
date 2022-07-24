package com.cooler.system.util

import com.cooler.system.entities.DeviceBean
import com.cooler.system.log
import com.tencent.mmkv.MMKV

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

    fun getString(offset: Int, value: Int) {
        when (offset) {
            address -> {
                var t = value // 温度
            }
            address + 1 -> {
                var has = value //有/无
            }
            address + 2 -> {

            }
            address + 3 -> value
            address + 4 -> value
            address + 5 -> value
            address + 6 -> value
            address + 7 -> value
            address + 8 -> value
            address + 9 -> value
            address + 10 -> value
            address + 11 -> value
            address + 12 -> value
            address + 13 -> value
            address + 14 -> value
            address + 15 -> value
            address + 16 -> value
            address + 17 -> value
            address + 18 -> value
            address + 19 -> value
            address + 20 -> value
            address + 21 -> value
            address + 22 -> value
            address + 23 -> value
            address + 24 -> value
            address + 25 -> value
            address + 26 -> value
            address + 27 -> value
            address + 28 -> value
            address + 29 -> value
        }
    }

    /**
     * 处理数据
     * @param offset Int
     * @param array IntArray
     */
    fun handleOffsetData(offset: Int, array: IntArray): MutableList<DeviceBean> {
        if (mCacheInfo == null) mCacheInfo = MutableList(3) { DeviceBean() }
        var des = 0
        val size = array.size
        array.forEachIndexed { index, a ->
            var o = offset + index
            when (o) {
                address, address + 10, address + 20 -> {
                    var t = a // 温度
                    log("温度 = $a")
                    if (o == address) mCacheInfo.get(0).temperature = t.toString()
                    if (o == address + 10) mCacheInfo.get(1).temperature = t.toString()
                    if (o == address + 10) mCacheInfo.get(2).temperature = t.toString()
                }
                address + 1, address + 11, address + 21 -> {
                    //有/无
                    log("有无 = $a")
                    if (o == address + 1) mCacheInfo.get(0).isHas = a
                    if (o == address + 11) mCacheInfo.get(1).isHas = a
                    if (o == address + 21) mCacheInfo.get(2).isHas = a
                }
                address + 2, address + 12, address + 22 -> {
                    // 状态
                    log("状态 = $a")
                    if (o == address + 2) mCacheInfo.get(0).state = a
                    if (o == address + 12) mCacheInfo.get(1).state = a
                    if (o == address + 22) mCacheInfo.get(2).state = a
                }
                address + 3, address + 13, address + 23 -> {
                    //名字
                    val one = ConvertUtil.int2Unicode(array[index + 1])
                    val two =
                        if (index + 1 < size) ConvertUtil.int2Unicode(array[index + 1]) else ""
                    val three =
                        if (index + 2 < size) ConvertUtil.int2Unicode(array[index + 2]) else ""
                    val four =
                        if (index + 3 < size) ConvertUtil.int2Unicode(array[index + 3]) else ""
                    val name = one + two + three + four
                    log("名字 = $name")
                    if (o == address + 3) mCacheInfo.get(0).name = name
                    if (o == address + 13) mCacheInfo.get(1).name = name
                    if (o == address + 23) mCacheInfo.get(2).name = name
                }
                address + 7, address + 17, address + 27 -> {
                    val one = array.get(index).toChar()
                    val two = if (index + 1 < size) array.get(index + 1).toChar() else Char(0)
                    val three = if (index + 2 < size) array.get(index + 2).toChar() else Char(0)
                    val code = StringBuffer().append(one).append(two).append(three).toString()
                    log("编号 = $code")
                    if (o == address + 7) {
                        mCacheInfo.get(0).code = code
                        MMKV.defaultMMKV().encode("code_first", code)
                    }
                    if (o == address + 17) {
                        mCacheInfo.get(1).code = code
                        MMKV.defaultMMKV().encode("code_second", code)

                    }
                    if (o == address + 27) {
                        mCacheInfo.get(2).code = code
                        MMKV.defaultMMKV().encode("code_third", code)
                    }
                }
            }

        }
        return mCacheInfo
    }

}