package com.cooler.system.network;

import androidx.lifecycle.LiveData;

import com.cooler.system.entities.CoolerBean;
import com.cooler.system.entities.GetCoolerResponse;

import java.util.List;

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
    @FormUrlEncoded
    @POST("tv/load-cold-equipment")
    LiveData<BaseResponse<List<CoolerBean>>> requestInfo(@Field("code") String code);

}
