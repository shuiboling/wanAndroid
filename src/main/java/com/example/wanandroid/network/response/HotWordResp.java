package com.example.wanandroid.network.response;

import java.io.Serializable;

public class HotWordResp implements Serializable {
    /*"id":6,
    "link":"",
    "name":"面试",
    "order":1,
    "visible":1
    */
    private String name;
    private String order;

    public String getName() {
        return name;
    }

    public String getOrder() {
        return order;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
