package com.example.wanandroid.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.wanandroid.widget.CircleView;
import com.example.wanandroid.R;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.ui.fragment.ArchitectureFragment;
import com.example.wanandroid.ui.fragment.MainFragment;
import com.example.wanandroid.ui.fragment.NavigationFragment;
import com.example.wanandroid.ui.fragment.ProjectFragment;
import com.example.wanandroid.ui.fragment.PublicsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.bnv_home)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.vp_home)
    ViewPager viewPager;
    @BindView(R.id.nv_home)
    NavigationView navigationView;
    @BindView(R.id.dl_home)
    DrawerLayout drawerLayout;

    MainFragment mainFragment;
    ProjectFragment projectFragment;
    ArchitectureFragment architectureFragment;
    NavigationFragment navigationFragment;
    PublicsFragment publicsFragment;
    List<Fragment> fragments = new ArrayList<>();

    private String state = "1";

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getLayout() {
        return R.layout.home_activity;
    }

    @Override
    public void initEventAndView() {

        initViewPagerAndBottomBar();
        initNavigationBar();
    }

    public void controlDrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void initNavigationBar() {
        //更改抽屉显示
//        drawerLayout.setScrimColor(0x00000066);
        //自定义navigation布局
//        getSupportFragmentManager().beginTransaction().replace(R.id.nv_home,
//                new NavigationFragment()).commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Toast.makeText(mContext,menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initViewPagerAndBottomBar() {
        mainFragment = new MainFragment();
        projectFragment = new ProjectFragment();
        architectureFragment = new ArchitectureFragment();
        navigationFragment = new NavigationFragment();
        publicsFragment = new PublicsFragment();

        fragments.add(mainFragment);
        fragments.add(projectFragment);
        fragments.add(architectureFragment);
        fragments.add(navigationFragment);
        fragments.add(publicsFragment);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_main_page:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_pro_page:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_schem_page:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.menu_neg_page:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.menu_pub_page:
                        viewPager.setCurrentItem(4);
                        break;

                }
                return false;
            }
        });
    }

    @Override
    public RxPresenter setPresenter() {
        return null;
    }

}
