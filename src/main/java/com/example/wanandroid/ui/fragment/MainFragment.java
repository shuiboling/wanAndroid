package com.example.wanandroid.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.wanandroid.R;
import com.example.wanandroid.framework.mvp.BaseFragment;
import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.ui.activity.DimpleActivity;
import com.example.wanandroid.ui.activity.GranzortActivity;
import com.example.wanandroid.ui.activity.HomeActivity;
import com.example.wanandroid.ui.activity.PaletteActivity;
import com.example.wanandroid.ui.activity.ScrollingActivity;
import com.example.wanandroid.ui.activity.SearchActivity;
import com.example.wanandroid.ui.activity.SetWordSizeActivity;
import com.example.wanandroid.ui.activity.SignActivity;
import com.example.wanandroid.widget.CircleView;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment {

    @BindView(R.id.tb_home)
    Toolbar toolbar;
    @BindView(R.id.rl_home_left)
    RelativeLayout rlLeftBtn;
    @BindView(R.id.iv_home_icon)
    ImageView ivToolBarIcon;
    @BindView(R.id.rl_home_search)
    RelativeLayout rlSearch;
//    @BindView(R.id.tb_tab_home)
//    TabLayout tableLayout;
    @BindView(R.id.vp_main)
    ViewPager viewPager;

    @BindView(R.id.appbar_main)
    AppBarLayout appBarLayout;
    @BindView(R.id.ll_main_toolbar_open)
    LinearLayout openToolbar;
    @BindView(R.id.ll_main_toolbar_close)
    LinearLayout closeToolbar;
    @BindView(R.id.gl_collapsing_cotent)
    GridLayout collapsingContent;

    private ActionBar actionBar;
    private int searchWidth;

    ProjectFragment projectFragment;
    ArchitectureFragment architectureFragment;
    NavigationFragment navigationFragment;
    PublicsFragment publicsFragment;
    List<Fragment> fragments = new ArrayList<>();

    @Override
    public void initEventAndView() {
        initToolBar();
        initAppBar();
//        initTabLayout();
        initViewPager();
        setHasOptionsMenu(true);

        for (int i = 0; i < collapsingContent.getChildCount(); i++){
            collapsingContent.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.gl_1:
                            startActivity(new Intent(getActivity(), DimpleActivity.class));
                            break;
                        case R.id.gl_2:
                            startActivity(new Intent(getActivity(), PaletteActivity.class));
                            break;
                        case R.id.gl_3:
                            startActivity(new Intent(getActivity(), GranzortActivity.class));
                            break;
                        default:
                            startActivity(new Intent(getActivity(), ScrollingActivity.class));
                    }

                }
            });
        }

    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
    }

    private CollapsingToolbarLayoutState state;

    private void initAppBar() {
        state = CollapsingToolbarLayoutState.EXPANDED;
        rlSearch.post(new Runnable() {
            @Override
            public void run() {
                searchWidth = rlSearch.getWidth();
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int offset = Math.abs(verticalOffset);
                int scrollRange = appBarLayout.getTotalScrollRange();
                Log.d("zyy",verticalOffset+"");
                if(offset <= collapsingContent.getHeight()){
                    openToolbar.setVisibility(View.VISIBLE);
                    closeToolbar.setVisibility(View.GONE);
                    state = CollapsingToolbarLayoutState.EXPANDED;
                } else {
                    openToolbar.setVisibility(View.GONE);
                    closeToolbar.setVisibility(View.VISIBLE);
                    state = CollapsingToolbarLayoutState.COLLAPSED;
                }
                getActivity().invalidateOptionsMenu();

//                //垂直方向偏移
//                int offset = Math.abs(verticalOffset);
//                //最大偏移距离
//                int scrollRange = appBarLayout.getTotalScrollRange();
//                //当滑动没超过一半时，展开状态下toolbar显示内容，根据收缩位置，改变透明值
//                if (offset <= scrollRange / 2){
//                    openToolbar.setVisibility(View.VISIBLE);
//                    closeToolbar.setVisibility(View.GONE);
//                    //根据偏移百分比，计算透明值
//                    float scale2 = (float) offset / (scrollRange / 2);
//                    int alpha2 = (int) (255 * scale2);
////                    collapsingContent.setBackgroundColor(Color.argb(alpha2,25,131,209));
//
//                }else {//当滑动超过一半，收缩状态下toolbar显示内容，根据收缩位置，改变透明值
//                    closeToolbar.setVisibility(View.VISIBLE);
//                    openToolbar.setVisibility(View.GONE);
//                    float scale3 = (float) (scrollRange - offset) / (scrollRange / 2);
//                    int alpha3 = (int) (255 * scale3);
////                    collapsingContent.setBackgroundColor(Color.argb(alpha3,25,131,209));
//                }
//                //根据偏移值百分比计算扫一扫布局的透明度值
//                float scale = (float) offset / scrollRange;
//                int alpha = (int) (255 * scale);
////                collapsingContent.setBackgroundColor(Color.argb(alpha,25,131,209));
            }
        });
    }

    private void initViewPager() {
        projectFragment = new ProjectFragment();
        architectureFragment = new ArchitectureFragment();
        navigationFragment = new NavigationFragment();

        fragments.add(projectFragment);
        fragments.add(architectureFragment);
        fragments.add(navigationFragment);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

//    private void initTabLayout() {
//        tableLayout.addTab(tableLayout.newTab().setText("精选"));
//        tableLayout.addTab(tableLayout.newTab().setText("美好生活"));
//        tableLayout.addTab(tableLayout.newTab().setText("实惠"));
//
//        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }

    @Override
    public int getLayout() {
        return R.layout.main_fragment;
    }


    @Override
    public RxPresenter setPresenter() {
        return null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
//        if(state == CollapsingToolbarLayoutState.EXPANDED) {
            inflater.inflate(R.menu.toolbar_menu_open, menu);
//        }else {
//            inflater.inflate(R.menu.toolbar_menu_close, menu);
//
//        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if(state == CollapsingToolbarLayoutState.EXPANDED){
            menu.setGroupVisible(R.id.group_menu_1,true);
            menu.setGroupVisible(R.id.group_menu_2,false);

        } else {
            menu.setGroupVisible(R.id.group_menu_1,false);
            menu.setGroupVisible(R.id.group_menu_2,true);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cake:
                startActivity(new Intent(mActivity, SetWordSizeActivity.class));
                break;
            case R.id.action_page:
                startActivity(new Intent(mActivity, SignActivity.class));
                break;
            default:
                Toast.makeText(mContext,item.getTitle(),Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.rl_home_search)
    public void OnClick(){
        startActivity(new Intent(mContext, SearchActivity.class));
    }

    private void initToolBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.setTitle("标题");
        actionBar.setDisplayShowTitleEnabled(false);

        rlLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).controlDrawer();
            }
        });

        Drawable drawable = mContext.getDrawable(R.drawable.head);
        CircleView circleView = new CircleView(drawable,mContext,30);
        ivToolBarIcon.setImageDrawable(circleView);
    }

}
