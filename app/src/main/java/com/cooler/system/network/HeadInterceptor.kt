package com.cooler.system.network

import android.text.TextUtils
import com.cooler.system.util.Util
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset

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
        var response=chain.proceed(request)
        Util.log("url = "+request.url +"  \n body = "+Gson().toJson(request.body) +"result =="+response.code+"\n" +getResponseInfo(response))

//        var newToken=response.header("token")

        return response
    }
    private fun getRequestInfo(request: Request):String{
        return ""
    }
    private fun getResponseInfo(response:Response):String{
        var str = ""
        if (response == null || !response.isSuccessful) {
            return str!!
        }
        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()
        val source = responseBody!!.source()
        try {
            source.request(Long.MAX_VALUE) // Buffer the entire body.
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val buffer = source.buffer()
        val charset = Charset.forName("utf-8")
        if (contentLength != 0L) {
            str = buffer.clone().readString(charset)
        }
        return str
    }
}