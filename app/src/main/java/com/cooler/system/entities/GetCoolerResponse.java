package com.cooler.system.entities;

import androidx.annotation.NonNull;

import com.cooler.system.network.BaseResponse;

import java.util.List;

/**
 * 描述：GetCoolerResponse
 * 创建者: shichengxiang
 * 创建时间：2022/8/16
 */
public class GetCoolerResponse extends BaseResponse<List<CoolerBean>> {

    public GetCoolerResponse(int code, @NonNull String msg) {
        super(code, msg);
    }

    public GetCoolerResponse(int code, @NonNull String msg, List<CoolerBean> data) {
        super(code, msg, data);
    }
}
