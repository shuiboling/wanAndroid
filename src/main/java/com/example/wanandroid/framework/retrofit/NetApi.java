package com.example.wanandroid.framework.retrofit;


import com.example.wanandroid.network.response.HotWordResp;
import com.example.wanandroid.network.response.LoginResp;
import com.example.wanandroid.network.response.SearchListData;
import com.example.wanandroid.network.response.SearchResp;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetApi {

    @POST("user/login_activity")
    Observable<CommonResponse<LoginResp>> login(@Query("username")String username, @Query("password")String password);

    @FormUrlEncoded
    @POST("user/register")
    Observable<ResponseBody> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    Observable<CommonResponse<SearchListData>> search(@Path("page") int page, @Field("k") String k);

    @GET("hotkey/json")
    Observable<CommonResponse<List<HotWordResp>>> getHotWords();
}
