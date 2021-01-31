package com.example.wanandroid.contract;

public interface LoginContract {
    interface View{
        public void loginSuccess();
        public void loginFail();
    }

    interface Presenter{
        public void login(String name,String password);
    }
}
