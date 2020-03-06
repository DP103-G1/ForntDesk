package com.example.ezeats.member;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.security.PrivilegedAction;

public class ChangePasswordFragment extends Fragment {
        private final static String TAG = "TAG_ChangePasswordFragment";
        private Activity activity;
        private EditText edUpDatePassword,edConfirmPassword;
        private String textUpDatePassword,textedConfirmPassword;
        private CommonTask changePassworTask;
        private String account,password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edConfirmPassword = view.findViewById(R.id.edConfirmPassword);
        edUpDatePassword = view.findViewById(R.id.edUpDatePassword);
        NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle != null){
            account = bundle.getString("account");
        }

        edUpDatePassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                textUpDatePassword = edUpDatePassword.getText().toString().trim();
                if (textUpDatePassword.isEmpty()){
                    edUpDatePassword.setError(getString(R.string.textUpdatePassword));
                }
            }
        });

        edConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                textedConfirmPassword = edConfirmPassword.getText().toString().trim();
                if (textedConfirmPassword.isEmpty()){
                    edConfirmPassword.setError(getString(R.string.textPasswordAgain));
                }
            }
        });

        Button btConfirm = view.findViewById(R.id.btConfirm);
        btConfirm.setOnClickListener(v -> {
            textUpDatePassword = edUpDatePassword.getText().toString().trim();
            Log.d(TAG,textUpDatePassword);
            textedConfirmPassword = edConfirmPassword.getText().toString().trim();
            if (textUpDatePassword.equals(textedConfirmPassword)){
                password = textUpDatePassword;
               Member member = new Member(account,password);
                String url = Url.URL + "/MembersServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action","updatePassword");
                jsonObject.addProperty("account", new Gson().toJson(member));
                changePassworTask = new CommonTask(url,jsonObject.toString());
                try {
                    String result = changePassworTask.execute().get();
                    int count = Integer.valueOf(result);
                    if (count != 0) {
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.textPasswordComplete)
                                .setMessage(R.string.textMassage)
                                .setPositiveButton(R.string.textYes, (dialog, which) -> navController.navigate(R.id.action_changePasswordFragment_to_loginFragment))
                                .show();
                    }
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }
            }else {
                Common.showToast(activity,R.string.textPasswordFail);
            }
        });

    }
}
