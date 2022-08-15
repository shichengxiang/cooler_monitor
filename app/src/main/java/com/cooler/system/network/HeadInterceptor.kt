package com.cooler.system.network

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
        var request=chain.request().newBuilder()
//            .header()
            .addHeader("token","")
            .build()
        var response=chain.proceed(request)
        var newToken=response.header("token")

        return response
    }
    private fun getRequestInfo(request: Request):String{
        return ""
    }
    private fun getResponseInfo(response:Response):String{
        return ""
    }
}