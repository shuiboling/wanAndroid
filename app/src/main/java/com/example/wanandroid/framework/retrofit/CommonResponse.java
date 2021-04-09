package com.example.wanandroid.framework.retrofit;

import java.io.Serializable;

public class CommonResponse<T> implements Serializable {
    public T data;
    public String errorCode;
    public String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
