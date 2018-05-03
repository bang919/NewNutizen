package com.nutizen.nu.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BaseMainFragment;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.fragment.HomeFragment;
import com.nutizen.nu.fragment.KanalFragment;
import com.nutizen.nu.fragment.RadioFragment;
import com.nutizen.nu.fragment.TvFragment;

import java.util.HashMap;

/**
 * Created by bigbang on 2018/4/10.
 */

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    private int[] tabTitles = {R.string.home, R.string.kanal, R.string.tv, R.string.radio};
    private HashMap<Integer, BaseMainFragment> mFragmentMap;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentMap = new HashMap<>();
        mFragmentMap.put(0, new HomeFragment());
    }

    @Override
    public BaseMainFragment getItem(int position) {
        BaseMainFragment f = mFragmentMap.get(position);
        if (f == null) {
            switch (position) {
                case 0:
                    f = new HomeFragment();
                    break;
                case 1:
                    f = new KanalFragment();
                    break;
                case 2:
                    f = new TvFragment();
                    break;
                case 3:
                    f = new RadioFragment();
                    break;
            }
            mFragmentMap.put(position, f);
        }
        return f;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return MyApplication.getMyApplicationContext().getString(tabTitles[position]);
    }
}
