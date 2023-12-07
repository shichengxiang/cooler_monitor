package com.vce.baselib.network

import com.cooler.system.network.HeadInterceptor
import com.cooler.system.network.LiveDataCallAdapterFactory
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

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
        val headInterceptor = HeadInterceptor()
        val okHttpClient = OkHttpClient.Builder()
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .callTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .sslSocketFactory(createSSLSocketFactory(),trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier{_,_ ->true}
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

    private var trustAllCerts = arrayOf<TrustManager>(object :X509TrustManager{
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    })
    private fun createSSLSocketFactory():SSLSocketFactory{
        var ssfFactory:SSLSocketFactory?=null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null,trustAllCerts, SecureRandom())
            ssfFactory = sc.socketFactory
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ssfFactory!!
    }
}