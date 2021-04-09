package com.example.wanandroid.presenter;

import com.example.wanandroid.contract.SearchContract;
import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.framework.retrofit.CommonResponse;
import com.example.wanandroid.framework.retrofit.OnNetworkListener;
import com.example.wanandroid.network.SearchRequest;
import com.example.wanandroid.network.response.HotWordResp;
import com.example.wanandroid.network.response.SearchListData;
import com.example.wanandroid.network.response.SearchResp;

import java.util.List;

public class SearchPresenter extends RxPresenter<SearchContract.View> implements SearchContract.Presenter {

    SearchRequest searchRequest = new SearchRequest(this);

    @Override
    public void search(int page,String key) {
        searchRequest.search(page, key, new OnNetworkListener<CommonResponse<SearchListData>>() {

            @Override
            public void onSuccess(CommonResponse<SearchListData> data) {
                mView.searchResult(data.getData());
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void getHotWord() {
        searchRequest.getHotWords(new OnNetworkListener<CommonResponse<List<HotWordResp>>>() {
            @Override
            public void onSuccess(CommonResponse<List<HotWordResp>> data) {
                if(data.getData() != null){
                    mView.getHotWordSuccess(data.getData());
                }

            }

            @Override
            public void onFail() {

            }
        });
    }
}
