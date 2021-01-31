package com.example.wanandroid.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.network.response.SearchListData;
import com.example.wanandroid.network.response.SearchResp;
import com.example.wanandroid.ui.activity.WebActivity;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<SearchResp> datas;
    private boolean footShow = false;

    private static final int TYPE_FOOT = R.layout.foot_view;
    private static final int TYPE_ITEM = R.layout.search_item_layout;

    public SearchResultAdapter(Context context, List<SearchResp> datas){
        this.context = context;
        this.datas = datas;
    }

    public void setData(List<SearchResp> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_FOOT){
            View view = LayoutInflater.from(context).inflate(viewType,parent,false);
            return new FootHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.search_item_layout, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_ITEM) {
            SearchResp searchData = datas.get(position);
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            if (TextUtils.isEmpty(searchData.getTitle())) {
                myViewHolder.title.setText("");
            } else {
                myViewHolder.title.setText(Html.fromHtml(searchData.getTitle()));
            }

            if (TextUtils.isEmpty(searchData.getChapterName())) {
                myViewHolder.tag.setVisibility(View.GONE);
            } else {
                myViewHolder.tag.setText(searchData.getChapterName());
            }

            if (TextUtils.isEmpty(searchData.getAuthor())) {
                myViewHolder.author.setVisibility(View.GONE);
            } else {
                myViewHolder.author.setText(searchData.getAuthor());
            }

            myViewHolder.heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "收藏", Toast.LENGTH_SHORT).show();
                }
            });

            myViewHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", searchData.getLink());
                    intent.putExtra("title", Html.fromHtml(searchData.getTitle()).toString());
                    context.startActivity(intent);
                }
            });
        } else {
            FootHolder footHolder = (FootHolder) holder;
            if(footShow){
                footHolder.foot.setVisibility(View.VISIBLE);
            } else {
                footHolder.foot.setVisibility(View.GONE);

            }
        }

    }

    @Override
    public int getItemCount() {
        return datas.size()/* == 0 ? 0 : datas.size()+1*/;
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1 == getItemCount()){
            return TYPE_FOOT;
        }else {
            return TYPE_ITEM;
        }
    }

    public void setFootShow(boolean footShow){
        this.footShow = footShow;
        notifyDataSetChanged();
    }

    public boolean isFootShow() {
        return footShow;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView tag;
        private TextView author;
        private ImageView heart;
        private LinearLayout item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title_search_item);
            tag = (TextView) itemView.findViewById(R.id.tv_tag_search_item);
            author = (TextView) itemView.findViewById(R.id.tv_author_search_item);
            heart = (ImageView) itemView.findViewById(R.id.iv_heart_search_item);
            item = (LinearLayout) itemView.findViewById(R.id.ll_search_item);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        private LinearLayout foot;

        public FootHolder(@NonNull View itemView) {
            super(itemView);
            foot = (LinearLayout) itemView.findViewById(R.id.ll_foot);
        }
    }
}
