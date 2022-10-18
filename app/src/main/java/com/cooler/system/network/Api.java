package com.cooler.system.network;

import androidx.lifecycle.LiveData;

import com.cooler.system.entities.CoolerBean;
import com.cooler.system.entities.GetCoolerResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 描述：Api
 * 创建者: shichengxiang
 * 创建时间：2022/3/23
 */
interface Api {
    @POST("app-api/tv/load-cold-equipment")
    LiveData<BaseResponse<List<CoolerBean>>> requestInfo(@Body RequestBody body);
//    @POST("tv/load-cold-equipment")
//    LiveData<BaseResponse<List<CoolerBean>>> requestInfo(@Body RequestBody body);
}
