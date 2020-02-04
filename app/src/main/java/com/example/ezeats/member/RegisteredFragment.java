package com.example.ezeats.member;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ezeats.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisteredFragment extends Fragment {


    public RegisteredFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registered, container, false);
    }

}
