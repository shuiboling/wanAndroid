package com.example.wanandroid.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
//import androidx.databinding.BindingMethod;

import com.example.wanandroid.R;
import com.example.wanandroid.contract.LoginContract;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.presenter.LoginPresenter;
import com.example.wanandroid.util.CryptologyUtil;
import com.example.wanandroid.widget.VerCodeUtil;

import java.math.BigInteger;
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

      verCodeUtil = new VerCodeUtil();
        Bitmap code = verCodeUtil.getCodePic();
        if(code != null){
            imageView.setImageBitmap(code);
        }
    }

    @Override
    public LoginPresenter setPresenter() {
        return new LoginPresenter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick({R.id.bt_login_login,R.id.bt_login_register,R.id.code,R.id.bt_login_ver})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_login_ver:
                Bitmap bitmap = verCodeUtil.isEqualCode(etCode.getText().toString());
                if( bitmap != null){
                    Toast.makeText(mContext,"不一样",Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);
                }else {
                    Toast.makeText(mContext,"一样",Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.bt_login_login:

//                Log.d("zyy",String.format("%032x",123));
//
                startActivity(new Intent(mContext,HomeActivity.class));
//                if(TextUtils.isEmpty(etName.getText())){
//                    Toast.makeText(mContext,"用户名不能为空",Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                if(TextUtils.isEmpty(etPw.getText())){
//                    Toast.makeText(mContext,"密码不能为空",Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                mPresenter.login(etName.getText().toString(),etPw.getText().toString());

                break;
            case R.id.bt_login_register:
                startActivity(new Intent(mContext,AActivity.class));
                break;
            case R.id.code:
                Bitmap code = verCodeUtil.getCodePic();
                if(code != null){
                    imageView.setImageBitmap(code);
                }
                break;
        }
    }

    private void fun() {
        int num = Integer.valueOf(etName.getText().toString());
        String moveCustListDataSqlAdd = "insert into cust_list select *";
        for(int i = 0;i<num;i++){
            moveCustListDataSqlAdd += ",''";
        }
        moveCustListDataSqlAdd += " from cust_list_temp";
        Log.d("zyy",moveCustListDataSqlAdd);
    }


    @Override
    public void loginSuccess() {
        Toast.makeText(mContext,"登陆成功",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(mContext,HomeActivity.class));
    }

    @Override
    public void loginFail() {
        Toast.makeText(mContext,"账号密码不匹配",Toast.LENGTH_SHORT).show();

    }

    public static boolean isNumberAndLetter(String str){
        if(TextUtils.isEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("^[a-z0-9A-Z]+");
        return pattern.matcher(str).matches();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sort(){
        List<String> list = Arrays.asList("key13","key10","key1","key","key2");
        MyComparator mc = new MyComparator() ;
        Collections.sort(list, mc) ;
        Log.d("zyyl",String.join(",",list));
    }

    private class MyComparator implements Comparator<String> {
        public int compare(String o1,String o2) {
            String e1=(String)o1;
            String e2=(String)o2;

            int n1=0,n2=0;
            if(!TextUtils.isEmpty(e1) && e1.contains("y")) {
                String s1 = e1.substring(e1.indexOf("y") + 1);
                if(!TextUtils.isEmpty(s1)) {
                    n1 = Integer.valueOf(s1);
                }
            }
            if(!TextUtils.isEmpty(e2) && e2.contains("y")) {
                String s2 = e2.substring(e2.indexOf("y") + 1);
                if(!TextUtils.isEmpty(s2)) {
                    n2 = Integer.valueOf(s2);
                }
            }

            if(n1 == n2){
                return 0;
            } else if(n1 > n2){
                return 1;
            } else {
                return -1;
            }
//            return e1.compareTo(e2);
        }
    }

}
