package com.example.wanandroid.network;

import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.framework.retrofit.BaseRequest;
import com.example.wanandroid.framework.retrofit.CommonResponse;
import com.example.wanandroid.framework.retrofit.OnNetworkListener;
import com.example.wanandroid.framework.retrofit.RxHelper;
import com.example.wanandroid.framework.retrofit.SubscribeHelper;
import com.example.wanandroid.network.response.HotWordResp;
import com.example.wanandroid.network.response.SearchListData;
import com.example.wanandroid.network.response.SearchResp;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class SearchRequest extends BaseRequest {

    public SearchRequest(RxPresenter rxPresenter) {
        super(rxPresenter);
    }

    public void search(int page, String key, final OnNetworkListener listener){
        Observable<CommonResponse<SearchListData>> observable = webRequest.search(page,key);
        observable.compose(RxHelper.rxScheduleHelper())
                .map(RxHelper.<SearchListData>result())
                .subscribe(new SubscribeHelper<CommonResponse<SearchListData>>(rxPresenter) {
                    @Override
                    public void onNext(CommonResponse<SearchListData> value) {
                        listener.onSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.onFail();
                    }

                });
    }

    public void getHotWords(final OnNetworkListener listener){
        Observable<CommonResponse<List<HotWordResp>>> observable = webRequest.getHotWords();
        observable.compose(RxHelper.rxScheduleHelper())
                .map(RxHelper.<List<HotWordResp>>result())
                .subscribe(new SubscribeHelper<CommonResponse<List<HotWordResp>>>(rxPresenter) {
                    @Override
                    public void onNext(CommonResponse<List<HotWordResp>> value) {
                        listener.onSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.onFail();
                    }
                });
    }
}
