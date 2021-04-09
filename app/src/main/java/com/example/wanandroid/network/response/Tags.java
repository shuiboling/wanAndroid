package com.example.wanandroid.network.response;

import java.io.Serializable;

public class Tags implements Serializable {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
