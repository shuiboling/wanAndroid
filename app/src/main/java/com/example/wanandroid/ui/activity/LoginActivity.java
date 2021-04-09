package com.example.wanandroid.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import androidx.databinding.BindingMethod;

import com.example.wanandroid.R;
import com.example.wanandroid.contract.LoginContract;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.presenter.LoginPresenter;
import com.example.wanandroid.util.CryptologyUtil;
import com.example.wanandroid.widget.VerCodeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.et_login_name)
    EditText etName;
    @BindView(R.id.et_login_pw)
    EditText etPw;
    @BindView(R.id.et_login_code)
    EditText etCode;
    @BindView(R.id.code)
    ImageView imageView;

    VerCodeUtil verCodeUtil;

    @Override
    public int getLayout() {
        return R.layout.login_activity;
    }

    @Override
    public void initEventAndView() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    0);
        }
        verCodeUtil = new VerCodeUtil();
        Bitmap code = verCodeUtil.getCodePic();
        if (code != null) {
            imageView.setImageBitmap(code);
        }
    }

    @Override
    public LoginPresenter setPresenter() {
        return new LoginPresenter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick({R.id.bt_login_login, R.id.bt_login_register, R.id.code, R.id.bt_login_ver})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_ver:
                Bitmap bitmap = verCodeUtil.isEqualCode(etCode.getText().toString());
                if (bitmap != null) {
                    Toast.makeText(mContext, "不一样", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(mContext, "一样", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.bt_login_login:
//
//                Log.d("zyy","is vo:"+fun1());
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "18519508757"));
//                intent.putExtra(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, VideoProfile.STATE_BIDIRECTIONAL);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);


//                Log.d("zyy",String.format("%032x",123));
//
//                startActivity(new Intent(mContext,HomeActivity.class));
//                if(TextUtils.isEmpty(etName.getText())){
//                    Toast.makeText(mContext,"用户名不能为空",Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                if(TextUtils.isEmpty(etPw.getText())){
//                    Toast.makeText(mContext,"密码不能为空",Toast.LENGTH_SHORT).show();
//                    break;
//                }
                mPresenter.login(etName.getText().toString(),etPw.getText().toString());
//                mPresenter.cloud();
//                new Thread() {
//                    @Override
//                    public void run() {
//                        super.run();
//                        String s = sendPost("http://47.104.74.169/user/login_activity","");
//                        Log.d("zyy",s);
//                    }
//                }.start();


                break;
            case R.id.bt_login_register:
                startActivity(new Intent(mContext, AActivity.class));
                break;
            case R.id.code:
                Bitmap code = verCodeUtil.getCodePic();
                if (code != null) {
                    imageView.setImageBitmap(code);
                }
                break;
        }
    }

    private boolean fun1() {
        if (Build.VERSION.SDK_INT >= 23) {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            Class<? extends TelephonyManager> teleclass = telephonyManager.getClass();

            try {
                Method method = teleclass.getDeclaredMethod("isVolteEnabled");
                method.setAccessible(true);
                return (boolean) method.invoke(telephonyManager);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    private void fun() {
        int num = Integer.valueOf(etName.getText().toString());
        String moveCustListDataSqlAdd = "insert into cust_list select *";
        for (int i = 0; i < num; i++) {
            moveCustListDataSqlAdd += ",''";
        }
        moveCustListDataSqlAdd += " from cust_list_temp";
        Log.d("zyy", moveCustListDataSqlAdd);
    }


    @Override
    public void loginSuccess() {
        Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(mContext, HomeActivity.class));
    }

    @Override
    public void loginFail() {
        Toast.makeText(mContext, "账号密码不匹配", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void cloudS() {

    }

    @Override
    public void cloudE() {

    }

    public static boolean isNumberAndLetter(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[a-z0-9A-Z]+");
        return pattern.matcher(str).matches();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sort() {
        List<String> list = Arrays.asList("key13", "key10", "key1", "key", "key2");
        MyComparator mc = new MyComparator();
        Collections.sort(list, mc);
        Log.d("zyyl", String.join(",", list));
    }

    private class MyComparator implements Comparator<String> {
        public int compare(String o1, String o2) {
            String e1 = (String) o1;
            String e2 = (String) o2;

            int n1 = 0, n2 = 0;
            if (!TextUtils.isEmpty(e1) && e1.contains("y")) {
                String s1 = e1.substring(e1.indexOf("y") + 1);
                if (!TextUtils.isEmpty(s1)) {
                    n1 = Integer.valueOf(s1);
                }
            }
            if (!TextUtils.isEmpty(e2) && e2.contains("y")) {
                String s2 = e2.substring(e2.indexOf("y") + 1);
                if (!TextUtils.isEmpty(s2)) {
                    n2 = Integer.valueOf(s2);
                }
            }

            if (n1 == n2) {
                return 0;
            } else if (n1 > n2) {
                return 1;
            } else {
                return -1;
            }
//            return e1.compareTo(e2);
        }
    }

    public String sendPost(String url, String param) {
        PrintWriter out = null;//网络请求对应的输出流，就是客户端把参数给服务器  叫输出，
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
// 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
// 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("x-qc-accesskey", "75JdAF7dsKZnY8hK");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
// 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
// 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
// 发送请求参数
            out.print(param);
// flush输出流的缓冲
            out.flush();
// 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }


        } catch (Exception e) {
            e.printStackTrace();

            return "send_fail";
        }
// 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
