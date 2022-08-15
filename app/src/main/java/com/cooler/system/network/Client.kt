package com.cooler.system.network

import com.cooler.system.entities.CoolerBean
import com.vce.baselib.network.BaseRetrofit

/**
 * 描述：Client
 * 创建者: shichengxiang
 * 创建时间：2022/3/22
 */
class Client {

    companion object{

        private var baseHost:String = ""
        fun setHost(host: String){
            baseHost = host
        }
        @JvmStatic
        val instance: Client by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Client() }
//        @JvmStatic
//        fun get()= lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Client }
    }

    private var retrofit: BaseRetrofit<Api>?=null
    private fun getServer():Api{
        if(retrofit==null){
            retrofit= BaseRetrofit(Api::class.java,baseHost)
        }
        return retrofit!!.getServer()
    }
    //请求信息
    fun requestInfo(code:String): MyLiveData<BaseResponse<List<CoolerBean>>> = getServer().requestInfo(code)
}