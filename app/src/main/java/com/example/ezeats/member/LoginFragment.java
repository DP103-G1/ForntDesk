package com.example.ezeats.member;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ezeats.R;



public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btReg = view.findViewById(R.id.btReg);
        btReg.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registeredFragment));

        Button btForget = view.findViewById(R.id.btForget);
        btForget.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgetFragment));
    }

}
