package com.example.ezeats.member;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.ezeats.main.MainActivity;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

public class LoginFragment extends Fragment {
    private static final String TAG = "TAG_LoginFragment";
    private MainActivity activity;
    private TextView tvTitle;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private EditText edAcc, edPass;
    private Button btLogin, btReg, btForget, btIn;
    private String textAccount, textPassword;
    private CommonTask loginTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        handledViews(view);
        tvTitle.setText(R.string.textLogin);
        navController = Navigation.findNavController(view);
        bottomNavigationView.setVisibility(View.GONE);


        edAcc.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textAccount = edAcc.getText().toString().trim().toLowerCase();
                edAcc.setText(textAccount);
                if (textAccount.isEmpty()) {
                    edAcc.setError(getString(R.string.textInputEmail));
                } else if (!textAccount.matches(Common.REGEX_EMAIL)) {
                    edAcc.setError(getString(R.string.textEmailFormateError));
                }
            }
        });
        edPass.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textPassword = edPass.getText().toString().trim();
                if (textPassword.isEmpty()) {
                    edPass.setError(getString(R.string.textInputPassword));
                }
            }
        });
        btLogin.setOnClickListener(this::onLoginClick);
        btReg.setOnClickListener(v ->
                navController.navigate(R.id.action_loginFragment_to_registeredFragment));
//        btForget.setOnClickListener(v ->
//                navController.navigate(R.id.action_loginFragment_to_forgetFragment));
        btIn.setOnClickListener(v -> {
            edAcc.setText("glen123@gmail.com");
            edPass.setText("1234567890");
        });
    }

    private void handledViews(View view) {
        tvTitle = activity.findViewById(R.id.tvTitle);
        bottomNavigationView = activity.findViewById(R.id.bv);
        edAcc = view.findViewById(R.id.edAcc);
        edPass = view.findViewById(R.id.edPass);
        btLogin = view.findViewById(R.id.btLogin);
        btReg = view.findViewById(R.id.btReg);
//        btForget = view.findViewById(R.id.btForget);
        btIn = view.findViewById(R.id.btIn);
    }

    private void onLoginClick(View view) {
        textAccount = edAcc.getText().toString().trim().toLowerCase();
        textPassword = edPass.getText().toString().trim();
        String url = Url.URL + "/MembersServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "login");
        jsonObject.addProperty("account", textAccount);
        jsonObject.addProperty("password", textPassword);
        loginTask = new CommonTask(url, jsonObject.toString());
        try {
            String result = loginTask.execute().get();
            int memId = Integer.parseInt(result);
            if (memId != 0) {
                SharedPreferences pref = activity.getSharedPreferences(Common.MEMBER_PREFRENCE, Context.MODE_PRIVATE);
                pref.edit().putString("account", textAccount).putString("password", textPassword).putInt("memId", memId).apply();
                navController.popBackStack(R.id.homeFragment, false);
                activity.onResume();
                Common.showToast(activity, getString(R.string.textLoginSuccess));
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
