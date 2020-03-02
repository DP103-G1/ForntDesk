package com.example.ezeats.order;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.ezeats.R;

public class BillFragment extends Fragment {
    private final  static String TAG = "TAG_BillFragment";
    private Activity activity;
    private EditText edName, edNumber, edLast;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

}
