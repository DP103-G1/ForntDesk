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
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class SelectMenuFragment extends Fragment {
    private static final String TAG = "TAG_SelectFragment";
    private Activity activity;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MySelectAdapter pagerAdapter;



//    private ImageView ivBooking,ivOrderMenu;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        activity.setTitle("查詢");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_menu, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pagerAdapter = new MySelectAdapter(activity,getChildFragmentManager());
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }



//        ivBooking = view.findViewById(R.id.ivBooking);
//        ivOrderMenu = view.findViewById(R.id.ivOrderMenu);
//
//
//        ivBooking.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_selectMenuFragment_to_selectBookingFragment));
//        ivOrderMenu.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_selectMenuFragment_to_selectOrderFragment));





}
