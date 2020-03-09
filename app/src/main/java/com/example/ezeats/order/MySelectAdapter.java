package com.example.ezeats.order;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ezeats.R;
import com.google.android.material.tabs.TabLayout;

public class MySelectAdapter extends FragmentStatePagerAdapter {
    private static final int[] TAB_TITLES = new int[]{R.string.textMenu,R.string.textOrderDetail};
    private Context mContext;
    private TabLayout tabLayout;



    public MySelectAdapter(@NonNull Context context, FragmentManager fm, TabLayout tabLayout) {
        super(fm);
        mContext = context;
        this.tabLayout = tabLayout;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OrderFragment(tabLayout);
            case 1:
                return new OrderDetailFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
