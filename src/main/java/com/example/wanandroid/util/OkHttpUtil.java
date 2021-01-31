package com.example.wanandroid.util;

import android.util.Log;

import java.io.File;
import java.io.IOException;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {
    private static final String TAG = "http_log";
    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 MiB

    private OkHttpClient okHttpClient;

    private static OkHttpUtil okHttpUtil;

    private OkHttpUtil(){
        initOkHttpClient();
    }

    public static OkHttpUtil getInstance(){
        if(okHttpUtil == null){
            synchronized (OkHttpUtil.class){
                if(okHttpUtil == null){
                    okHttpUtil = new OkHttpUtil();
                }
            }
        }
        return okHttpUtil;
    }

    public void okGetAsyFunc(){
        //1.创建okHttpClient对象
        initOkHttpClient();

        //2.创建Request
        Request request = new Request.Builder().url("http://61.135.169.125").build();

        //3.new Call
        Call call = okHttpClient.newCall(request);

        //4.请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.d("zyy","fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功
                Log.d("zyy",response.body().string());
            }
        });
    }

    public void okGetSynFunc(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //1.创建okHttpClient对象
                    OkHttpClient okHttpClient = new OkHttpClient();

                    //2.创建Request
                    Request request = new Request.Builder().url("http://61.135.169.125").build();

                    //3.new Call
                    Call call = okHttpClient.newCall(request);

                    //4.执行call
                    Response response = call.execute();
                    if(response != null && response.isSuccessful()){
                        Log.d("zyy",response.body().string());
                    }
                    response.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();



    }

    public void ex1(){
//        OkHttpClient client = setOkHttpClient();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                Request request = new Request.Builder()
//                        .url("https://47.104.74.169/article/list/1/json")
//                        .build();
//
//                try {
//                    Response response = client.newCall(request).execute();
//                    Log.d("zyy","-----");
//
//                    if (!response.isSuccessful())
//                        throw new IOException("Unexpected code " + response);
//
//                    Headers responseHeaders = response.headers();
//                    for (int i = 0; i < responseHeaders.size(); i++) {
//                        Log.d("zyy","-----");
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//
//                    System.out.println(response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                Request request = new Request.Builder()
                        .url("https://47.104.74.169/article/list/1/json")
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Accept", "application/vnd.github.v3+json")
                        .build();

                try (Response response = okHttpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    System.out.println("Server: " + response.header("Server"));
                    System.out.println("Date: " + response.header("Date"));
                    System.out.println("Vary: " + response.headers("Vary"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public void okPostFunc(){
        //1.创建okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //2.request Body
        //2.1
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("name","value")
                .build();

        RequestBody mutiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("image", "logo-square.png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        //2.2
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "numId");

        //2.3
        FormBody formBody = new FormBody.Builder().add("name","value").build();

        //3.创建Request
        Request request = new Request.Builder()
                .url("http://61.135.169.125")
                .post(requestBody)
                .build();

        //4.new Call
        Call call = okHttpClient.newCall(request);

        //5.加入调度
        //5.1异步
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("zyy","fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("zyy",response.body().string());
            }
        });

        //5.2同步
        try {
            Response response = call.execute();
            if(response.isSuccessful()){
                //
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initOkHttpClient(){

        if(okHttpClient == null){

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //设置超时时间
            builder.readTimeout(30, TimeUnit.SECONDS)    //读
                    .writeTimeout(30,TimeUnit.SECONDS)   //写
                    .connectTimeout(30,TimeUnit.SECONDS);    //连接


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
