package com.cooler.system.network

import androidx.lifecycle.LiveData
import com.cooler.system.App
import com.cooler.system.entities.CoolerBean
import com.cooler.system.entities.GetCoolerResponse
import com.cooler.system.log
import com.cooler.system.util.EncryptionUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.vce.baselib.network.BaseRetrofit
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * 描述：Client
 * 创建者: shichengxiang
 * 创建时间：2022/3/22
 */
class Client {

    companion object {
        @JvmStatic
        val instance: Client by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Client() }
//        @JvmStatic
//        fun get()= lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Client }
    }

    private val DEFAULT_TIME = 10L
    private var baseHost: String = ""
    private var okHttpClient: OkHttpClient? = null
    fun setNewHost(host: String) {
        baseHost = host
        retrofit = BaseRetrofit(Api::class.java, baseHost)
    }

    private var retrofit: BaseRetrofit<Api>? = null
    private fun getServer(): Api {
        if (retrofit == null) {
            retrofit = BaseRetrofit(Api::class.java, baseHost)
        }
        return retrofit!!.getServer()
    }

    private fun initHttpClient() {
        if (okHttpClient == null) {
            //调度器
            var dispatcher = Dispatcher()
            dispatcher.maxRequests = 10
            dispatcher.maxRequestsPerHost = 10
            val headInterceptor = HeadInterceptor()
            okHttpClient = OkHttpClient().newBuilder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
                .callTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory(),trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier{_,_ ->true}
                .addInterceptor(headInterceptor)
                .dispatcher(dispatcher)
                .build()
        }
    }

    //请求信息
    fun requestInfo(body: RequestBody): LiveData<BaseResponse<List<CoolerBean>>> =
        getServer().requestInfo(body)








    var cacheBody: RequestBody? = null //缓存设备请求体
    fun postInfo(devices: Array<String>,callback:Callback) {
        if (okHttpClient == null) initHttpClient()
        if (cacheBody == null) {
            val map = hashMapOf<String, Any>().apply {
                put("codes", devices)
            }
            val encrypStr = EncryptionUtil.encrypToBase64Str(Gson().toJson(map))
            val obj = hashMapOf<String, String>().apply {
                put("content", encrypStr)
            }
            cacheBody = Gson().toJson(obj)
                .toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
        }
        val request =
            Request.Builder().post(cacheBody!!).url("${baseHost}app-api/tv/load-cold-equipment")
                .build()
        okHttpClient?.newCall(request)?.enqueue(callback)
    }
    fun clearCache(){
        cacheBody =null
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
    private fun createSSLSocketFactory(): SSLSocketFactory {
        var ssfFactory: SSLSocketFactory?=null
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