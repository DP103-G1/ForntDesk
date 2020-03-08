package com.example.ezeats.member;


import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class MemberDataUpdateFragment extends Fragment {
    private static final String TAG = "TAG_MemberDataUpdateFragment";
    private Activity activity;
    private TextView tvMemberIdGet, tvAccountGet;
    private EditText edPasswordGet, edNameGet, edPhoneGet;
    private CommonTask memberTask;
    private Member member;
    private String textPassword, textName, textPhone;
    private int memId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        memId = Common.getMemId(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_data_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        tvMemberIdGet = view.findViewById(R.id.tvMemberIdGet);
        tvAccountGet = view.findViewById(R.id.tvAccountGet);
        edPasswordGet = view.findViewById(R.id.edPasswordGet);
        edNameGet = view.findViewById(R.id.edNameGet);
        edPhoneGet = view.findViewById(R.id.edPhoneGet);
        member = getMemberData();
        tvMemberIdGet.setText(String.valueOf(memId));
        tvAccountGet.setText(member.getAccount());
        edPasswordGet.setText(member.getpassword());
        edNameGet.setText(member.getname());
        edPhoneGet.setText(member.getphone());
        edPasswordGet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textPassword = edPasswordGet.getText().toString().trim();
                    if (textPassword.isEmpty()) {
                        edPasswordGet.setError(getString(R.string.textInputPassword));

                    }
                }
            }
        });
        edNameGet.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textName = edNameGet.getText().toString().trim();
                if (textName.isEmpty()) {
                    edNameGet.setError(getString(R.string.textInputName));
                }
            }
        });
        edPhoneGet.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textPhone = edNameGet.getText().toString().trim();
                if (textName.isEmpty()) {
                    edPhoneGet.setError(getString(R.string.textInputPhone));
                }
            }
        });


        Button btOk = view.findViewById(R.id.btOk);
        btOk.setOnClickListener(v -> {
            String password = edPasswordGet.getText().toString().trim();
            if (password.length() <= 0) {
                Common.showToast(activity, R.string.textPasswordIsInvalid);
                return;
            }
            String name = edNameGet.getText().toString().trim();
            if (name.length() <= 0) {
                Common.showToast(activity, R.string.textNoName);
                return;

            }
            String phone = edPhoneGet.getText().toString().trim();
            if (phone.length() <= 0) {
                Common.showToast(activity, R.string.textPhoneInvaild);
                return;
            }
                if (Common.networkConnected(activity)) {
                    String url = Url.URL + "/MembersServlet";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "memberUpdate");
                    member.Member(password, name, phone, 0);
                    jsonObject.addProperty("member", new Gson().toJson(member));
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.textUpdateFail);
                    } else if (count == 1 ) {
                        SharedPreferences pref = activity.getSharedPreferences(Common.MEMBER_PREFRENCE, Context.MODE_PRIVATE);
                        pref.edit().putString("password", password).putString("name",name).putString("phone",phone).apply();
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.textUpdate)
                                .setMessage(R.string.textMemberSuccess)
                                .setPositiveButton(R.string.textYes, (dialog, which) -> navController.navigate(R.id.action_memberDataUpdateFragment_to_homeFragment))
                                .show();
                    }
                } else {
                    Common.showToast(activity, R.string.textNoNetWork);
                }

        });

        Button btCancel = view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(v -> navController.navigate(R.id.action_memberDataUpdateFragment_to_memberRegionFragment));
    }

    private Member getMemberData() {
        Member memberData = null;
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/MembersServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findByMemberId");
            jsonObject.addProperty("member_Id", memId);
            String jsonOut = jsonObject.toString();
            memberTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = memberTask.execute().get();
                Type listType = new TypeToken<Member>() {
                }.getType();
                memberData = Common.gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetWork);
        }
        return memberData;
    }


}
