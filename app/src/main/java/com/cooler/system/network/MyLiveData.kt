package com.cooler.system.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 描述：MyLiveData
 * 创建者: shichengxiang
 * 创建时间：2022/3/25
 */
class MyLiveData<T> constructor(var call: Call<T>) : LiveData<T?>() {
//    private val call: Call<T>
    var success:((T)->Unit)?=null
    var error:((T)->Unit)?=null
    var completed:((T)->Unit)?=null
    private val stared = AtomicBoolean(false)
    override fun onActive() {
        super.onActive()
        //确保执行一次
        if (stared.compareAndSet(false, true)) {
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    //                        LogUtil.d("返回数据=" + JSON.toJSONString(body));
                    if (200 == response.code()) {
//                            LogUtil.d("网络请求成功");

//                            ApiResponse t = JSON.parseObject(JSON.toJSONString(body), ApiResponse.class);

//                            if (t.code == ApiResponse.CODE_INVALID) {
//                                Log.e("ApiResponse", ApiResponse.CODE_INVALID + "");
////                                LoginInvalidActivity.forward("登录失效");
//                                ApiResponse apiResponse = new ApiResponse(ApiResponse.CODE_INVALID, t.msg, null);
//                                postValue((T) apiResponse);
//
//                            } else if (t.code == ApiResponse.CODE_SUCCESS) {
//                                postValue(body);
//                            } else {
//                                ToastUtil.show(t.msg);
//                                LogUtil.d( "onResponse="+response.message());
//                                ApiResponse apiResponse = new ApiResponse(ApiResponse.CODE_ERROR,, null);
//                                postValue((T) t);
                        postValue(body)
                        //                            }
                    } else {
                        val apiResponse: BaseResponse<*> = BaseResponse<Any?>(response.code(), response.message(), null)
                        postValue(apiResponse as T)
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    postValue(BaseResponse<Any>(-1, t.message!!) as T)
                }
            })
        }
    }

    fun cancel() {
        call.cancel()
    }
    fun success(s:(T)->Unit): MyLiveData<T> {
        this.success=s
        return this
    }
    fun error(e:(T)->Unit): MyLiveData<T> {
        this.error=e
        return this
    }
    fun completed(c:(T)->Unit): MyLiveData<T> {
        this.completed=c
        return this
    }
    fun call(owner: LifecycleOwner){
        observe(owner){
            if(it is BaseResponse<*>){
                if(it.isSuccess()){
                    success?.invoke(it)
                }else{
                    error?.invoke(it)
                }
                completed?.invoke(it)
            }
        }
    }
}