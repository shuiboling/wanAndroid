package com.example.wanandroid.ui.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class AActivity extends ListActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String []str = {"111","222","333","444","5555"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,str);

        setListAdapter(arrayAdapter);

        String name = getIntent().getStringExtra("input");
        Toast.makeText(AActivity.this,name,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent();
        intent.putExtra("result","Hello world");

        setResult(Activity.RESULT_OK,intent);
        finish();

    }
}
