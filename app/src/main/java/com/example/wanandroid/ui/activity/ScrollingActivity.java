package com.example.wanandroid.ui.activity;

import android.os.Bundle;

import com.example.wanandroid.ui.fragment.ProjectFragment;
import com.example.wanandroid.widget.PullableLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;

import java.util.Arrays;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    PullableLayout pull;
    List<String> list = Arrays.asList("1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8");
    ListView listView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pullable_layout);

        pull = (PullableLayout) findViewById(R.id.pull);
        pull.setRefreshListener(new PullableLayout.onRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(0);
                    }
                }).start();
            }

            @Override
            public void onLoad() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        mHandler.sendEmptyMessage(1);
//                    }
//                }).start();
            }
        });


//        listView = (ListView)findViewById(R.id.pull_list);
//        listView.setAdapter(new ArrayAdapter<String>(ScrollingActivity.this,android.R.layout.simple_list_item_1,list));

        recyclerView = findViewById(R.id.pull_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SimpleAdapter(list));
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                pull.refreshDone();
            } else {
                pull.loadDone();//
            }
        }
    };

    class SimpleAdapter extends RecyclerView.Adapter{

        List<String> list;

        SimpleAdapter(List<String> list){
            this.list = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ScrollingActivity.this).inflate(android.R.layout.simple_list_item_1,parent,false);
            return new SimpleAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((SimpleAdapter.MyHolder)holder).textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            TextView textView;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }

}
