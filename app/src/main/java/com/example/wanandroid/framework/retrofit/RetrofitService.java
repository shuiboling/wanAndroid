package com.example.wanandroid.framework.retrofit;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static final String BASE_URL = "https://www.wanandroid.com/";
    public static RetrofitService instance;
    public Retrofit retrofit;

    private static final String TAG = "http_log";
    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 MiB

    private OkHttpClient okHttpClient;

    private RetrofitService(){

    }

    public static RetrofitService getInstance(){
        if(instance == null){
            synchronized (RetrofitService.class){
                if(instance == null)
                    instance = new RetrofitService();
            }
        }
        return instance;
    }

    public Retrofit createRetrofit(){
        if(retrofit == null){
            synchronized (RetrofitService.class){
                if(retrofit == null) {
                    initOkHttpClient();
                    retrofit = new Retrofit.Builder()
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .baseUrl(BASE_URL)
                            .build();
                }
            }
        }
        return retrofit;
    }

    public void initOkHttpClient(){

        if(okHttpClient == null){

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //设置超时时间
            builder.readTimeout(60, TimeUnit.SECONDS)    //读
                    .writeTimeout(60,TimeUnit.SECONDS)   //写
                    .connectTimeout(60,TimeUnit.SECONDS);    //连接


            //log拦截器
            Interceptor logInterceptor = new Interceptor() {

                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request request = chain.request();

                    long t1 = System.nanoTime();
                    Log.e(TAG,String.format("Sending request %s on %s%n%s",
                            request.url(),chain.connection(),request.headers()));


                    Response response = chain.proceed(request);
                    long t2 = System.nanoTime();
                    Log.e(TAG,String.format("Receive response for %s in %.1fms%n%s",
                            response.request().url(),(t2-t1)/1e6d,response.headers()));

                    Log.e(TAG,"response code is "+response.code());

                    //获得返回的body，注意此处不要使用responseBody.string()获取返回数据，原因在于这个方法会消耗返回结果的数据(buffer)
                    ResponseBody responseBody = response.body();
                    Log.e("TAG", "code-----" + response.code());
                    //为了不消耗buffer，我们这里使用source先获得buffer对象，然后clone()后使用
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    //获得返回的数据
//                    if (BuildConfig.CONFIG_DEBUG) {// debug 版本控制
                    Buffer buffer = source.buffer();
                        //使用前clone()下，避免直接消耗
                    Log.e("TAG", "response:" + buffer.clone().readString(Charset.forName("UTF-8")));
//                    }
                    return response;
                }


            };
            builder.addInterceptor(logInterceptor);

            //Cache
            Cache cache = new Cache(new File("cache_path"),CACHE_SIZE);
            builder.cache(cache);

            //
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    //强行返回true 即验证成功
                    return true;
                }
            }).sslSocketFactory(createSSLSocketFactory());


            okHttpClient = builder.build();
        }

    }

    SSLSocketFactory TRUSTED_FACTORY;
    private SSLSocketFactory getTrustedFactory() {
        if (TRUSTED_FACTORY == null) {
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

            } };
            try {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, trustAllCerts, new SecureRandom());
                TRUSTED_FACTORY = context.getSocketFactory();
            } catch (GeneralSecurityException e) {
                IOException ioException = new IOException(
                        "Security exception configuring SSL context");
                ioException.initCause(e);
            }
        }

        return TRUSTED_FACTORY;
    }





    public SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

//        // 取到证书的输入流
//        InputStream is = null;
//        try {
//            is = MainApp.getInstance().getApplicationContext().getResources().getAssets().open("abchinacom.crt");
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            Certificate ca = cf.generateCertificate(is);
//            // 创建 Keystore 包含我们的证书
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null);
//            keyStore.setCertificateEntry("abchinacom", ca);
//
//            // 创建一个 TrustManager 仅把 Keystore 中的证书 作为信任的锚点
//            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
//            trustManagerFactory.init(keyStore);
//            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//            // 用 TrustManager 初始化一个 SSLContext
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null, trustManagers, null);
//
//            ssfFactory = sc.getSocketFactory();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } finally {
//            if (is != null)
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        }

        return ssfFactory;
    }

    public class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
