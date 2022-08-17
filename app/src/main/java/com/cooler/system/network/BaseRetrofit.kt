package com.vce.baselib.network

import com.cooler.system.network.HeadInterceptor
import com.cooler.system.network.LiveDataCallAdapterFactory
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 描述：BaseRetrofit
 * 创建者: shichengxiang
 * 创建时间：2022/3/23
 */
class BaseRetrofit<T> {
    private val DEFAULT_TIME = 10L
    var retrofit: Retrofit? = null
    private var server: T? = null
    private var tClass: Class<T>? = null
    private var baseHost :String = ""
    constructor(tClass: Class<T>,host:String) {
        this.tClass = tClass
        init(tClass,host)
    }

    private fun init(tClass: Class<T>,host: String) {
        baseHost = host
        //调度器
        var dispatcher = Dispatcher()
        dispatcher.maxRequests = 10
        dispatcher.maxRequestsPerHost = 10
        //拦截器
        var headInterceptor = HeadInterceptor()
        var okHttpClient = OkHttpClient.Builder()
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .callTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .addInterceptor(headInterceptor)
            .dispatcher(dispatcher)
            .build()

        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .baseUrl(host)
            .build()
        server = retrofit?.create(tClass)
    }

    public fun getServer(): T {
        if (server == null) {
            server = retrofit?.create(tClass)
        }
        return server!!
    }
}