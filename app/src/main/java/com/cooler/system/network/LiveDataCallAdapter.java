package com.cooler.system.network;

import androidx.lifecycle.LiveData;

import com.cooler.system.util.Util;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {

    private Type mResponseType;

    LiveDataCallAdapter(Type mResponseType) {
        this.mResponseType = mResponseType;
    }

    @NotNull
    @Override
    public Type responseType() {
        return mResponseType;
    }

    @NotNull
    @Override
    public LiveData<T> adapt(@NotNull final Call<T> call) {
        return new com.cooler.system.network.MyLiveData<>(call);
    }

    private static class MyLiveData<T> extends LiveData<T> {

        private final Call<T> call;
        private AtomicBoolean stared = new AtomicBoolean(false);

        MyLiveData(Call<T> call) {
            this.call = call;
        }

        @Override
        protected void onActive() {
            super.onActive();
            //确保执行一次
            if (stared.compareAndSet(false, true)) {

                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                        T body = response.body();
//                        LogUtil.d("返回数据=" + JSON.toJSONString(body));

                        if (200 == response.code()) {
                            Util.log(new Gson().toJson(response.body()));
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
                                postValue(body);
//                            }

                        } else {
                            BaseResponse apiResponse = new BaseResponse(response.code(), response.message(), null);
                            postValue((T) apiResponse);
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                        postValue((T) new BaseResponse<>(-1, t.getMessage()));

                    }
                });
            }
        }
        void cancel(){
            call.cancel();
        }
    }
}