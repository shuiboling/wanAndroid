package com.example.wanandroid.widget.choose;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;

import java.util.List;

public class AddressChooseAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<AddressChooseBean> list;
//    private int movePosition;
    private OnItemClickListener onItemClickListener;
    private OnCancelClickListener onCancelClickListener;

    public AddressChooseAdapter(Context context, List<AddressChooseBean> list){
        this.context = context;
        this.list = list;
//        this.movePosition = movePosition;
    }

    public void updateData(List<AddressChooseBean> list){
        this.list = list;
//        this.movePosition = movePosition;
        notifyDataSetChanged();
    }

//    public void setMovePosition(int movePosition){
//        this.movePosition = movePosition;
//        notifyDataSetChanged();
//    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener){
        this.onCancelClickListener = onCancelClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.abc_activity_choose_item,null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder)holder;

        if(position == 0 && onCancelClickListener != null){
            itemHolder.rlCancel.setVisibility(View.VISIBLE);
            itemHolder.rlCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCancelClickListener.OnCancelClick();
                }
            });

        }else {
            itemHolder.rlCancel.setVisibility(View.GONE);
        }

        if(list.get(position).isChoose()){
            itemHolder.tvText.setTextColor(context.getResources().getColor(R.color.color_00c2ab));
            itemHolder.ivTick.setVisibility(View.VISIBLE);
        }else {
            itemHolder.tvText.setTextColor(context.getResources().getColor(R.color.color_323232));
            itemHolder.ivTick.setVisibility(View.GONE);
        }

        itemHolder.tvText.setText(list.get(position).getName());
        itemHolder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ItemHolder extends RecyclerView.ViewHolder{
        TextView tvText;
        RelativeLayout rlItem;
        ImageView ivTick;
        RelativeLayout rlCancel;

        public ItemHolder(View itemView) {
            super(itemView);
            if(itemView != null) {
                tvText = itemView.findViewById(R.id.tv_choose_item);
                rlItem = itemView.findViewById(R.id.rl_choose_item);
                ivTick = itemView.findViewById(R.id.iv_choose_tick);
                rlCancel = itemView.findViewById(R.id.rl_not_choose);
            }
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public interface OnCancelClickListener{
        void OnCancelClick();
    }
}
