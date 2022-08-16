package com.cooler.system.network;

import androidx.lifecycle.LiveData;

import com.cooler.system.util.Util;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Util.log("1");
        if (getRawType(returnType) != LiveData.class){
            return null;
        }
        Util.log("2");
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Type rawType = getRawType(observableType);
        if (rawType != BaseResponse.class){
            throw new IllegalArgumentException("type must be ApiResponse");
        }
        Util.log("3");
        if (!ParameterizedType.class.isInstance(observableType)){
            throw new IllegalArgumentException("resource must be Parameterized");
        }
        Util.log("4");
        return new LiveDataCallAdapter<Type>(observableType);
    }
}