package com.example.wanandroid.contract;

import com.example.wanandroid.framework.retrofit.OnNetworkListener;
import com.example.wanandroid.network.response.HotWordResp;
import com.example.wanandroid.network.response.SearchListData;
import com.example.wanandroid.network.response.SearchResp;

import java.util.List;

public interface SearchContract {
    interface View{
        public void searchResult(SearchListData data);
        public void getHotWordSuccess(List<HotWordResp> data);
    }

    interface Presenter{
        public void search(int page, String key);
        public void getHotWord();
    }
}
