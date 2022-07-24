package com.cooler.system.modbus

interface TcpListener {
    /**
     * 写入多次
     * @param offset Int
     * @param arr IntArray
     */
    fun onDataReceived(offset:Int,arr:IntArray)

    /**
     * 写入单次
     * @param offset Int
     * @param value Int
     */
    fun onDataReceived(offset: Int,value:Int)
}