package com.example.wanandroid.network.response;

import java.io.Serializable;
import java.util.List;

public class SearchListData implements Serializable {
    private List<SearchResp> datas;

    public List<SearchResp> getDatas() {
        return datas;
    }

    public void setDatas(List<SearchResp> datas) {
        this.datas = datas;
    }
}
