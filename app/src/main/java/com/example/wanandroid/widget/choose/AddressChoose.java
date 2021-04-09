package com.example.wanandroid.widget.choose;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.widget.CommonPopWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressChoose {

    private Context context;

    private RecyclerView upRecycler;
    private RecyclerView downRecycler;
    private AddressChooseAdapter upAdapter;
    private AddressChooseAdapter downAdapter;
    private LinearLayoutManager upLinearLayoutManager;
    private LinearLayoutManager downLinearLayoutManager;
    private AddressChooseDecoration addressChooseDecoration;
//    private List<AddressChooseBean> upList;
    private List<AddressChooseBean> downList;

    private int lastSelectedPos = 0;
//    private List<Integer> upChooseList;
//    private List<Integer> downChooseList;

    private CommonPopWindow commonPopWindow;
    private TextView tvCancel;

    private List<AddressChooseBean> downSelectedList;  //用于保存已选元素
    private AddressChooseBean[] downSelectedHistory;

    public AddressChoose(Context context){
        this.context = context;

        initPopupWindow();
    }

    private void initPopupWindow() {
        View mPopupWindowView = LayoutInflater.from(context).inflate(R.layout.choose_pop_list, null);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;

        commonPopWindow = new CommonPopWindow.PopupWindowBuilder(context)
                .setView(mPopupWindowView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight * 3 / 4)
                .setOutsideTouchable(true)
                .setFocusable(true)
                .setBgDarkAlpha(0.6f)
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        addressChooseInterface.popDismiss();
                    }
                })
                .create();

        tvCancel = mPopupWindowView.findViewById(R.id.tv_etc_choose_cancel);
        downRecycler = mPopupWindowView.findViewById(R.id.rv_etc_choose_recyclerview);
        upRecycler = mPopupWindowView.findViewById(R.id.rc_etc_choose_tab);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downSelectedList = Arrays.asList(downSelectedHistory);
                commonPopWindow.dismiss();
            }
        });

        initUpRecycler();
        initDownRecycler();

    }

    private void initUpRecycler() {

//        if(upChooseList != null){
//            upChooseList.clear();
//        }else {
//            upChooseList = new ArrayList<>();
//        }

        addressChooseDecoration = new AddressChooseDecoration(context, Color.RED,50,0,downSelectedList);
        upAdapter = new AddressChooseAdapter(context,downSelectedList);
        upLinearLayoutManager = new LinearLayoutManager(context);

        upRecycler.addItemDecoration(addressChooseDecoration);
        upRecycler.setLayoutManager(upLinearLayoutManager);
        upRecycler.setAdapter(upAdapter);

        upAdapter.setOnItemClickListener(new AddressChooseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                changeUpItemStatus(position);
                changeDownList(position);
            }
        });

    }

    private void initDownRecycler() {

        if(downSelectedList != null){
            downSelectedList.clear();
        }else {
            downSelectedList = new ArrayList<>();
        }
//        if(downChooseList != null){
//            downChooseList.clear();
//        }else {
//            downChooseList = new ArrayList<>();
//        }

        downAdapter = new AddressChooseAdapter(context,downList);
        downLinearLayoutManager = new LinearLayoutManager(context);

        downRecycler.setLayoutManager(downLinearLayoutManager);
        downRecycler.setAdapter(downAdapter);

        downAdapter.setOnCancelClickListener(new AddressChooseAdapter.OnCancelClickListener() {
            @Override
            public void OnCancelClick() {
                if(lastSelectedPos <= downSelectedList.size()){
                    downSelectedList.subList(0,lastSelectedPos+1);
                }
                upAdapter.updateData(downSelectedList);
            }
        });

        downAdapter.setOnItemClickListener(new AddressChooseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                saveDownItemSelected(position);
                changeUpItemStatus(position);
                changeDownList(position);
            }
        });
    }

    private void saveDownItemSelected(int position) {
        // TODO: 2020-08-14 保存选择
        if(lastSelectedPos <= downSelectedList.size()){
            downSelectedList.subList(position,downSelectedList.size()+1).clear();
        }
//        downList.get(position).setChoose(true);
        downSelectedList.add(downList.get(position));
    }

    private void changeUpItemStatus(int position) {
        //取消之前选择
        downSelectedList.get(lastSelectedPos).setChoose(false);
        //更新当前选择
        downSelectedList.get(position).setChoose(true);
        upAdapter.updateData(downSelectedList);
        //保存当前选择
        lastSelectedPos = position;
    }

    private void changeDownList(int position) {
        //获取新的列表
        addressChooseInterface.getDownList(position);
    }

    public void updateDownList(List<AddressChooseBean> downList){
        // 更新列表
        this.downList = downList;
        downAdapter.updateData(downList);

        //获取已选位置
        AddressChooseBean chooseBean;
        int position = 0;
        if(downSelectedList.size() > lastSelectedPos){
            chooseBean = downSelectedList.get(lastSelectedPos);

            for(int i=0;i<downList.size();i++){
                if(downList.get(i).getId().equals(chooseBean.getId())){
                    position = i;
                }
            }
        }
        //滑动到已选位置
        downRecycler.scrollToPosition(position);
    }

    public void showPop(View anchor){
        commonPopWindow.showAtBottom(anchor);

        if(downSelectedList != null){
            downSelectedHistory = downSelectedList.toArray(new AddressChooseBean[downSelectedList.size()]);
        }

    }

    private AddressChooseInterface addressChooseInterface;

    public void setInterface(AddressChooseInterface addressChooseInterface){
        this.addressChooseInterface = addressChooseInterface;
    }

    public interface AddressChooseInterface{
        public void getDownList(int position);
        public void popDismiss();
    }

}
