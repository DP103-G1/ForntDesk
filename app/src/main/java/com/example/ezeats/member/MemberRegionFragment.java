package com.example.ezeats.member;


import android.app.Activity;
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

import com.example.ezeats.R;


public class MemberRegionFragment extends Fragment {
    private ListView listView;
    private Activity activity;
    private String[] str = {"會員修改","登出"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_member_region, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(activity,R.layout.item_view_listviewitem,str);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            switch (position){
                case 0:
                    Navigation.findNavController(view1).navigate(R.id.action_memberRegionFragment_to_memberDataUpdateFragment);
                    break;
                case 1:
                    Navigation.findNavController(view1).navigate(R.id.action_memberRegionFragment_to_memberFragment);
                    break;
            }
        });

    }
}
