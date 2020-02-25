package com.example.ezeats.select;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ezeats.R;


public class SelectMenuFragment extends Fragment {
    private static final String TAG = "TAG_SelectFragment";
    private Activity activity;
    private ImageView ivBooking,ivOrderMenu;


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
        ivBooking = view.findViewById(R.id.ivBooking);
        ivOrderMenu = view.findViewById(R.id.ivOrderMenu);
        ivOrderMenu = view.findViewById(R.id.ivOrderMenu);

        ivBooking.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_selectMenuFragment_to_selectBookingFragment));
        ivOrderMenu.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_selectMenuFragment_to_selectOrderFragment));
    }

}
