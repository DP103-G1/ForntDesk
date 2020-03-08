package com.example.ezeats.order;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ezeats.R;
import com.example.ezeats.main.MainActivity;
import com.example.ezeats.select.SelectBookingFragment;
import com.example.ezeats.select.SelectOrderFragment;

public class MySelectAdapter extends FragmentStatePagerAdapter {
    private static final int[] TAB_TITLES = new int[]{R.string.textMenu,R.string.textOrderDetail};
    private Context mContext;



    public MySelectAdapter(@NonNull Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OrderFragment();
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
