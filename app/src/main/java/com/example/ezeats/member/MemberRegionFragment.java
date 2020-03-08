package com.example.ezeats.member;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.MainActivity;


public class MemberRegionFragment extends Fragment {
    private TextView tvTitle;
    private ListView listView;
    private MainActivity activity;
    private NavController navController;
    private String[] str = {"會員修改","訂位查詢","訂單查詢","登出"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_member_region, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textMember);

        listView = view.findViewById(R.id.listView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(activity,R.layout.item_view_listviewitem,str);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            switch (position){
                case 0:
                    Navigation.findNavController(view1).navigate(R.id.action_memberRegionFragment_to_memberDataUpdateFragment);
                    break;
                case 1:
                    Navigation.findNavController(view1).navigate(R.id.action_memberRegionFragment_to_selectBookingFragment);
                    break;
                case 2:
                    Navigation.findNavController(view1).navigate(R.id.action_memberRegionFragment_to_selectOrderFragment);
                    break;
                case 3:
                   logout(activity);
            }
        });
    }

    private void logout(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Common.MEMBER_PREFRENCE, Context.MODE_PRIVATE);
        pref.edit().putString("account", null).putString("password", null).putInt("memId", 0).apply();
        activity.onResume();
        navController.popBackStack(R.id.homeFragment, false);
        navController.navigate(R.id.loginFragment);
    }
}
