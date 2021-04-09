package com.example.wanandroid.contract;

public interface LoginContract {
    interface View{
        public void loginSuccess();
        public void loginFail();
        void cloudS();
        void cloudE();
    }

    interface Presenter{
        public void login(String name,String password);
        void cloud();
    }
}
