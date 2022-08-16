package com.cooler.system.network

import android.text.TextUtils
import com.cooler.system.util.Util
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 描述：HeadInterceptor
 * 创建者: shichengxiang
 * 创建时间：2022/3/23
 */
class HeadInterceptor :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
//            .header("server", "nginx/1.15.11")
            .header("Content-Type", "application/json; charset=utf-8")
            .addHeader("Connection", "close")
            .addHeader("Accept-Encoding", "chunked")
            .build()
        Util.log("url = "+request.url +"  \n body = "+Gson().toJson(request.body))
        var response=chain.proceed(request)
//        var newToken=response.header("token")

        return response
    }
    private fun getRequestInfo(request: Request):String{
        return ""
    }
    private fun getResponseInfo(response:Response):String{
        return ""
    }
}