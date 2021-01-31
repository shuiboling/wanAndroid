package com.example.wanandroid.ui.activity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.contract.SearchContract;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.network.response.HotWordResp;
import com.example.wanandroid.network.response.SearchListData;
import com.example.wanandroid.network.response.SearchResp;
import com.example.wanandroid.presenter.SearchPresenter;
import com.example.wanandroid.ui.adapter.SearchResultAdapter;
import com.example.wanandroid.widget.FlowLayoutVGVersion;
import com.example.wanandroid.widget.SignatureView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {
    @BindView(R.id.et_search)
    EditText editText;
    @BindView(R.id.iv_search_delete)
    ImageView ivDelete;
    @BindView(R.id.id_stickynavlayout_viewpager)
    RecyclerView recyclerView;
//    @BindView(R.id.fl_search)
//    FlowLayoutVGVersion flowLayoutRVersion;
//    @BindView(R.id.ll_content_search)
//    LinearLayout linearLayout;
//    @BindView(R.id.id_e)
//    LinearLayout l;

    private SearchResultAdapter searchResultAdapter;
    private List<SearchResp> list = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private Handler handler = new Handler();

    @Override
    public int getLayout() {
        return R.layout.new_layout;
    }

    @Override
    public void initEventAndView() {
        initEditText();
        initRecyclerView();

        mPresenter.getHotWord();
    }

    private void initRecyclerView() {
        searchResultAdapter = new SearchResultAdapter(mContext,list);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchResultAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            private boolean isSlidingUpward = false;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滑动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的itemPosition
                    int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                    int itemCount = manager.getItemCount();

                    // 判断是否滑动到了最后一个item，并且是向上滑动
                    if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                        //加载更多
                        onLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingUpward = dy > 0;
//                lastPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void onLoadMore() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                searchResultAdapter.setFootShow(true);
//            }
//        },300);
        searchResultAdapter.setFootShow(true);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchResultAdapter.setFootShow(false);

            }
        },300);

    }

    private void initEditText() {
        editText.requestFocus();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    if (s.length() > 0){
                        ivDelete.setVisibility(View.VISIBLE);
                    }else {
                        ivDelete.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    mPresenter.search(0,editText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public SearchPresenter setPresenter() {
        return new SearchPresenter();
    }

    @OnClick({R.id.tv_search_cancel,R.id.iv_search_delete})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_search_cancel:
                finish();
                break;
            case R.id.iv_search_delete:
                editText.setText("");
                ivDelete.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void searchResult(SearchListData data) {

        if(data != null && data.getDatas() != null && !data.getDatas().isEmpty()){
            list = data.getDatas();
            searchResultAdapter.setData(list);

//            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

//            Log.d("zyy",l.getMeasuredHeight()+"");

        }

    }

    @Override
    public void getHotWordSuccess(List<HotWordResp> data) {
//        if(data != null && !data.isEmpty()) {
//            flowLayoutRVersion.removeAllViews();
//                for (int i = 0; i < data.size(); i++) {
//                    String key = data.get(i).getName();
//                    View view = View.inflate(mContext, R.layout.hot_word_layout, null);
//                    TextView textView = view.findViewById(R.id.tv_hot_word);
//                    textView.setText(key);
//                    textView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            editText.setText(key);
//                            editText.setSelection(key.length());
//                            mPresenter.search(0, key);
//                        }
//                    });
//                    flowLayoutRVersion.addView(view);
//            }
//        }
    }
}
