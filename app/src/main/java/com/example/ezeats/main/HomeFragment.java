package com.example.ezeats.main;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ezeats.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {
    private static final String TAG = "TAG_HomeFragment";
    private Activity activity;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        if (Common.getMemId(activity) == 0) {
            navController.navigate(R.id.action_homeFragment_to_loginFragment);
        }
        handledViews();
        bottomNavigationView.setVisibility(View.VISIBLE);

        Button btOpi = view.findViewById(R.id.btOpi);
        btOpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_linkFragment);
            }
        });
    }

    private void handledViews() {
        bottomNavigationView = activity.findViewById(R.id.bv);
    }
}
