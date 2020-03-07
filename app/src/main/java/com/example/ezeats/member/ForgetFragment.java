package com.example.ezeats.member;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.gson.JsonObject;


public class ForgetFragment extends Fragment {
    private static final String TAG = "TAG_ForgetFragment";
    private Activity activity;
    private EditText edAccount,edPhone;
    private String textAccount,textPhone;
    private CommonTask forgetTask;
    private Member member;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        edAccount = view.findViewById(R.id.edAccount);
        edPhone = view.findViewById(R.id.edPhone);

        edAccount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                textAccount = edAccount.getText().toString().trim().toLowerCase();
                edAccount.setText(textAccount);
                if (textAccount.isEmpty()){
                    edAccount.setError(getString(R.string.textInputEmail));
                }else if (!textAccount.matches(Common.REGEX_EMAIL)){
                    edAccount.setError(getString(R.string.textEmailFormateError));
                }
            }
        });

        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 10){
                    textPhone = s.subSequence(0,10).toString();
                    edPhone.setText(textPhone);
                    edPhone.setSelection(textPhone.length());
                }
            }
        });

        edPhone.setOnFocusChangeListener((v, hasFocus) -> {
            textPhone = edPhone.getText().toString().trim();
            if (textPhone.isEmpty()){
                edPhone.setError(getString(R.string.textInputPhone));
            }else if (!textPhone.matches(Common.REGEX_PHONE)){
                edPhone.setError(getString(R.string.textPhoneFormateError));
            }
        });

        Button btOk = view.findViewById(R.id.btOk);
        btOk.setOnClickListener(v -> {

            textAccount = edAccount.getText().toString().trim().toLowerCase();
            textPhone = edPhone.getText().toString().trim();
            String url = Url.URL + "/MembersServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action","forget");
            jsonObject.addProperty("account",textAccount);
            jsonObject.addProperty("phone",textPhone);
            forgetTask = new CommonTask(url,jsonObject.toString());
            try {
                String result = forgetTask.execute().get();
                boolean passwordForget = Boolean.valueOf(result);
                if (passwordForget){
                    Bundle bundle = new Bundle();
                    bundle.putString("account", textAccount);
                    Navigation.findNavController(v).navigate(R.id.action_forgetFragment_to_changePasswordFragment,bundle);
                    navController.navigate(R.id.action_forgetFragment_to_changePasswordFragment);
                }else {
                    Common.showToast(activity,R.string.textDataWrong);
                }
            }catch (Exception e){
                Log.e(TAG,e.toString()) ;
            }
        });

        Button btNo = view.findViewById(R.id.btNo);
        btNo.setOnClickListener(v -> {
            navController.popBackStack();
        });
    }
}
