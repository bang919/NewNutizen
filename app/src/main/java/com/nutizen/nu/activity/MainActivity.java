package com.nutizen.nu.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.MainViewPagerAdapter;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.BaseFragment;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.dialog.NormalDialog;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.utils.ScreenUtils;
import com.nutizen.nu.widget.MySwipeRefreshLayout;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainViewPagerAdapter mViewpagerAdapter;
    private MySwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout mDrawerLayout;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public int getBarColor() {
        return R.color.colorBlack;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.main_viewpager);
        mTabLayout = findViewById(R.id.main_tablayout);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setProgressViewOffset(true, 0, (int) ScreenUtils.dip2px(this, 36f));
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);//Toggle DrawerLayout and Toolbar

        mViewpagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewpagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Proof SwipeRefreshLayout's function when ViewPager dragging
                BaseFragment fragment = mViewpagerAdapter.getItem(mViewPager.getCurrentItem());
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    //这里要走Fragment的setRefreshEnable，看看有没有条件需要判断，不能直接走SwipeRefreshLayout.setRefreshEnable
                    fragment.setRefreshEnable(false);
                } else {
                    fragment.setRefreshEnable(true);
                }
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorGreen);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        BaseFragment fragment = mViewpagerAdapter.getItem(mViewPager.getCurrentItem());
        fragment.refreshData();
    }

    @Override
    protected void initData() {
        TextView versionTv = findViewById(R.id.tv_version);
        versionTv.setText(getString(R.string.version, getVersion()));
        mSwipeRefreshLayout.setRefreshing(true);
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        if (accountMessage != null) {
            ((TextView) findViewById(R.id.tv_left_menu_name)).setText(accountMessage.getDetail().getViewer_username());
        }
    }

    private String getVersion() {
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.00.00";
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.iv_main_search).setOnClickListener(this);
        findViewById(R.id.leftitem_profile).setOnClickListener(this);
        findViewById(R.id.leftitem_favourit).setOnClickListener(this);
        findViewById(R.id.leftitem_download).setOnClickListener(this);
        findViewById(R.id.leftitem_help_faq).setOnClickListener(this);
        findViewById(R.id.leftitem_notification).setOnClickListener(this);
        findViewById(R.id.iv_log_out).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_main_search:
                break;
            case R.id.leftitem_profile:
                break;
            case R.id.leftitem_favourit:
                break;
            case R.id.leftitem_download:
                break;
            case R.id.leftitem_help_faq:
                break;
            case R.id.leftitem_notification:
                break;
            case R.id.iv_log_out:
                new NormalDialog(this, getString(R.string.yes), getString(R.string.no), getString(R.string.measure_logout), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();
                    }
                }, null).show();
                break;
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setRefreshEnable(boolean enable) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshEnable(enable);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE || event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
            if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                mDrawerLayout.closeDrawer(Gravity.START);
            } else {
                new NormalDialog(this, getString(R.string.ye), getString(R.string.no), getString(R.string.do_you_want_to_exit), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
