package com.example.ezeats.member;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.google.gson.JsonObject;


public class RegisteredFragment extends Fragment {
    private static final String TAG = "TAG_RegisteredFragment";
    private Activity activity;
    private EditText edaccs, edpassword, edagain, edname, edphone;
    private Button btCheck, btBack;
    private String textAccount, textAgain, textPassword, textName, textPhone;
    private CommonTask memberInsertTask;
    private NavController navController;
    private TextView tvTitle;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registered, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textRegister);
        navController = Navigation.findNavController(view);
        handledViews(view);
        edaccs.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textAccount = edaccs.getText().toString().trim().toLowerCase();
                edaccs.setText(textAccount);
                if (textAccount.isEmpty()) {
                    edaccs.setError(getString(R.string.textInputEmail));
                } else if (!textAccount.matches(Common.REGEX_EMAIL)) {
                    edaccs.setError(getString(R.string.textEmailFormateError));
                }
            }
        });

        edpassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textPassword = edpassword.getText().toString().trim();
                if (textPassword.isEmpty()) {
                    edpassword.setError(getString(R.string.textInputPassword));
                }
            }
        });

        edagain.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textPassword = edpassword.getText().toString().trim();
                textAgain = edagain.getText().toString().trim();
                if (!textAgain.equals(textPassword)) {
                    edagain.setError(getString(R.string.textPasswordComfirmedError));
                }
            }
        });

        edname.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textName = edname.getText().toString().trim();
                if (textName.isEmpty()) {
                    edname.setError(getString(R.string.textInputName));
                }
            }
        });

        edphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 10) {
                    textPhone = s.subSequence(0, 10).toString();
                    edphone.setText(textPhone);
                    edphone.setSelection(textPhone.length());
                }
            }
        });
        edphone.setOnFocusChangeListener((v, hasFocus) -> {
            textPhone = edphone.getText().toString().trim();
            if (textPhone.isEmpty()) {
                edphone.setError(getString(R.string.textInputPhone));
            } else if (!textPhone.matches(Common.REGEX_PHONE)) {
                edphone.setError(getString(R.string.textPhoneFormateError));
            }
        });

        btCheck.setOnClickListener(this::onRegisterClick);
        btBack.setOnClickListener(v -> navController.popBackStack());
    }

    private void handledViews(View view) {
        edaccs = view.findViewById(R.id.edaccs);
        edpassword = view.findViewById(R.id.edpassword);
        edagain = view.findViewById(R.id.edagain);
        edname = view.findViewById(R.id.edname);
        edphone = view.findViewById(R.id.edphone);
        btCheck = view.findViewById(R.id.btCheck);
        btBack = view.findViewById(R.id.btBack);
    }

    private void onRegisterClick(View view) {
        String url = Url.URL + "/MembersServlet";
        textAccount = edaccs.getText().toString().trim().toLowerCase();
        textPassword = edpassword.getText().toString().trim();
        textAgain = edagain.getText().toString().trim();
        textName = edname.getText().toString().trim();
        textPhone = edphone.getText().toString().trim();
        boolean canInsert = true;
        if (textAccount.isEmpty()) {
            canInsert = false;
            edaccs.setError(getString(R.string.textInputEmail));
        } else if (!textAccount.matches(Common.REGEX_EMAIL)) {
            canInsert = false;
            edaccs.setError(getString(R.string.textEmailFormateError));
        }
        if (textPassword.isEmpty()) {
            canInsert = false;
            edpassword.setError(getString(R.string.textInputPassword));
        }
        if (!textAgain.equals(textPassword)) {
            canInsert = false;
            edagain.setError(getString(R.string.textPasswordComfirmedError));
        }
        if (textName.isEmpty()) {
            canInsert = false;
            edname.setError(getString(R.string.textInputName));
        }
        if (textPhone.isEmpty()) {
            canInsert = false;
            edphone.setError(getString(R.string.textInputPhone));
        } else if (!textPhone.matches(Common.REGEX_PHONE)) {
            canInsert = false;
            edphone.setError(getString(R.string.textPhoneFormateError));
        }
        if (!canInsert) {
            Log.d(TAG, "123");
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "memberInsert");
            Member member = new Member(textAccount, textPassword, textName, textPhone);
            jsonObject.addProperty("member",  Common.gson.toJson(member));
            memberInsertTask = new CommonTask(url, jsonObject.toString());
            int memId = 0;
            try {
                String result = memberInsertTask.execute().get();
                memId = Integer.parseInt(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (memId > 0) {
                SharedPreferences pref = activity.getSharedPreferences(Common.MEMBER_PREFRENCE, Context.MODE_PRIVATE);
                pref.edit().putString("account", textAccount).putString("password", textPassword).putInt("memId", memId).apply();
                Common.showToast(activity, R.string.textRegisterSuccess);
                navController.popBackStack(R.id.homeFragment, false);
            } else if (memId == -1) {
                Common.showToast(activity, R.string.textUsingSameAccount);
            } else {
                Common.showToast(activity, R.string.textRegisterFail);
            }
        }
    }
}
