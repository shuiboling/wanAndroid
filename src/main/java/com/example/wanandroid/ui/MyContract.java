package com.example.wanandroid.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wanandroid.ui.activity.AActivity;
import com.example.wanandroid.ui.activity.WebActivity;

public class MyContract extends ActivityResultContract<String,String> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        Intent intent = new Intent(context, AActivity.class);
        intent.putExtra("input",input);
        return intent;
    }

    @Override
    public String parseResult(int resultCode, @Nullable Intent intent) {
        String data = intent.getStringExtra("result");

        if (resultCode == Activity.RESULT_OK && data != null)
            return data;
        else
            return "";
    }
}
