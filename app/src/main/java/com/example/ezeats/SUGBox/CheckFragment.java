package com.example.ezeats.SUGBox;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ezeats.R;
import com.google.android.material.tabs.TabLayout;


public class CheckFragment extends Fragment {
    private static final String TAG = "TAG_SUGFragment";
    private Activity activity;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MySugBoxAdapter pagerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tabLayout);
        pagerAdapter = new MySugBoxAdapter(activity,getChildFragmentManager(),tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
