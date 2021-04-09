package com.example.wanandroid.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.wanandroid.R;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.ui.MyContract;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.tb_web)
    Toolbar toolbar;
    @BindView(R.id.pb_web)
    ProgressBar progressBar;

    private String url;
    private WebSettings webSettings;
    private Unbinder unbinder;
    private ActivityResultLauncher activityResultLauncher;

    @Override
    public void initEventAndView() {
        unbinder = ButterKnife.bind(this);
        initWebSettings();
        initWebView();
        initToolBar();

    }

    @Override
    public RxPresenter setPresenter() {
        return null;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if(menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void initToolBar() {
//        setSupportActionBar(toolbar);
        if(!TextUtils.isEmpty(getIntent().getStringExtra("title"))){
            toolbar.setTitle(getIntent().getStringExtra("title"));
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.heart_web_menu:
//                        activityResultLauncher.launch("this is input");

                        Intent intent1 = new Intent(WebActivity.this,AActivity.class);
                        intent1.putExtra("input","from web");
                        myActivityLauncher.launch(intent1);
                        break;
                    case R.id.web_web:
                        Uri uri;
                        if(!TextUtils.isEmpty(url)) {
                           uri  = Uri.parse(url);
                        }else {
                           uri = Uri.parse("https://www.baidu.com");
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                        break;
                    case R.id.share_web:
                        break;
                }
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityResultLauncher activityResultLauncher = registerForActivityResult(new MyContract(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String result) {
                Toast.makeText(WebActivity.this,result,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ActivityResultLauncher myActivityLauncher
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            int resultCode = result.getResultCode();
            switch (resultCode){
                case Activity.RESULT_OK:
                    String a = result.getData().getStringExtra("result");
                    Toast.makeText(WebActivity.this,a,Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    });

    private void initWebView() {
        url = getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initWebSettings() {
        webSettings = webView.getSettings();
        //支持js
        webSettings.setJavaScriptEnabled(true);
        //支持缩放
        webSettings.setSupportZoom(true);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public int getLayout() {
        return R.layout.web_activity;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
