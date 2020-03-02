package com.example.ezeats.select;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ezeats.R;
import com.example.ezeats.order.OrderFragment;
import com.google.android.material.tabs.TabLayout;


public class SelectMenuFragment extends Fragment implements ViewPager.OnPageChangeListener,TabLayout.OnTabSelectedListener{
    private static final String TAG = "TAG_SelectFragment";
    private Activity activity;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SelectBookingFragment selectBookingFragment = new SelectBookingFragment();
    private SelectOrderFragment selectOrderFragment = new SelectOrderFragment();

//    private ImageView ivBooking,ivOrderMenu;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_menu, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return selectBookingFragment;
                    case 1:
                        return selectOrderFragment;
                }
                return null;
            }

        });
    }



//        ivBooking = view.findViewById(R.id.ivBooking);
//        ivOrderMenu = view.findViewById(R.id.ivOrderMenu);
//
//
//        ivBooking.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_selectMenuFragment_to_selectBookingFragment));
//        ivOrderMenu.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_selectMenuFragment_to_selectOrderFragment));



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabLayout.getTabAt(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
