package com.cooler.system.network

import androidx.lifecycle.LiveData
import com.cooler.system.App
import com.cooler.system.entities.CoolerBean
import com.cooler.system.entities.GetCoolerResponse
import com.cooler.system.log
import com.cooler.system.util.EncryptionUtil
import com.google.gson.Gson
import com.vce.baselib.network.BaseRetrofit
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

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

    private var baseHost: String = ""
    private var okHttpClient: OkHttpClient? = null
    private var mGson: Gson? = null
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
            okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .addInterceptor {
                    val req = it.request()
                    val response = it.proceed(req)
                    log("http = response = ${response.code} ${response.body}")
                    response
                }
                .build()
        }
    }

    //请求信息
    fun requestInfo(body: RequestBody): LiveData<BaseResponse<List<CoolerBean>>> =
        getServer().requestInfo(body)

    var cacheBody: RequestBody? = null //缓存设备请求体
    fun postInfo(devices: Array<String>) {
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
        okHttpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
//                    try {
//                        if(mGson == null) mGson = Gson()
//                        val json = mGson.fromJson<BaseResponse<List<CoolerBean>>>(mGson.toJson(response.body),)
//                    }
                }
            }
        })
    }
}