package com.example.ezeats.member;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;

public class MemberFragment extends Fragment {
    private static final String TAG = "TAG_MemberFragment";
    private Activity activity;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        logout(activity);
    }

    private void logout(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Common.MEMBER_PREFRENCE, Context.MODE_PRIVATE);
        pref.edit().putString("account", null).putString("password", null).putInt("memId", 0).apply();
        navController.popBackStack(R.id.homeFragment, false);
    }
}
