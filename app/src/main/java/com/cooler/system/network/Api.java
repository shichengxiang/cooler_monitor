package com.cooler.system.network;

import com.cooler.system.entities.CoolerBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * 描述：Api
 * 创建者: shichengxiang
 * 创建时间：2022/3/23
 */
interface Api {
    @POST("/app-api/tv/load-cold-equipment")
    MyLiveData<BaseResponse<List<CoolerBean>>> requestInfo(@Field("code") String code);

}
