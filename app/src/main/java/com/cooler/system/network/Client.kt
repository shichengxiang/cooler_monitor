package com.cooler.system.network

import androidx.lifecycle.LiveData
import com.cooler.system.entities.CoolerBean
import com.cooler.system.entities.GetCoolerResponse
import com.vce.baselib.network.BaseRetrofit

/**
 * 描述：Client
 * 创建者: shichengxiang
 * 创建时间：2022/3/22
 */
class Client {

    companion object{
        @JvmStatic
        val instance: Client by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Client() }
//        @JvmStatic
//        fun get()= lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Client }
    }
    private var baseHost:String = ""
    fun setNewHost(host:String){
        baseHost = host
        retrofit= BaseRetrofit(Api::class.java,baseHost)
    }

    private var retrofit: BaseRetrofit<Api>?=null
    private fun getServer():Api{
        if(retrofit==null){
            retrofit= BaseRetrofit(Api::class.java,baseHost)
        }
        return retrofit!!.getServer()
    }
    //请求信息
    fun requestInfo(code:String): LiveData<BaseResponse<List<CoolerBean>>> = getServer().requestInfo(code)
}