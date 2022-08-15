package com.cooler.system.network;

import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class){
            return null;
        }
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Type rawType = getRawType(observableType);
        if (rawType != BaseResponse.class){
            throw new IllegalArgumentException("type must be ApiResponse");
        }
        if (!ParameterizedType.class.isInstance(observableType)){
            throw new IllegalArgumentException("resource must be Parameterized");
        }
        return new LiveDataCallAdapter<Type>(observableType);
    }
}