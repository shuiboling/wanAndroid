package com.example.wanandroid.framework.retrofit;

import io.reactivex.functions.Function;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxHelper {
    //统一处理，产生事件（网络请求）作用在io线程，其他事件（如网络请求回调处理）作用在主线程
    public static ObservableTransformer rxScheduleHelper(){
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Function<CommonResponse<T>,CommonResponse<T>> result(){
        return new Function<CommonResponse<T>, CommonResponse<T>>() {
            @Override
            public CommonResponse<T> apply(CommonResponse<T> o) {
                if("0".equals(o.errorCode))
                    return o;
                else
                    throw new RuntimeException(o.getErrorMsg());
            }
        };
    }
}
