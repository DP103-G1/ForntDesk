package com.example.ezeats.HowTo;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezeats.R;
import com.example.ezeats.qa.AnswerAdapter;
import com.example.ezeats.qa.QA;

import java.util.ArrayList;
import java.util.List;


public class HowToFragment extends Fragment {
    private TextView tvTitle;
    private Activity activity;
    private RecyclerView rvHowTo;
    List<QA> qaList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_how_to, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textcommon);
        rvHowTo = view.findViewById(R.id.rvHowTo);

        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        AnswerAdapter answerAdapter = new AnswerAdapter(qaList);
        rvHowTo.setLayoutManager(new LinearLayoutManager(activity));
        rvHowTo.setAdapter(answerAdapter);
    }

    private void initData() {
        qaList = new ArrayList<>();
        qaList.add(new QA("NO.1", "如何加入會員", "打開EZeats的APP填入帳號密碼即可註冊會員"));
        qaList.add(new QA("No.2", "忘記帳號密碼", "於登入選項下請點選忘記密碼，輸入帳號和手機號碼後可更改密碼"));
        qaList.add(new QA("No.3", "如何訂位", "點選下排功能表“訂位”進入訂位頁面"));
        qaList.add(new QA("No.4", "如何查詢訂單", "點選下排功能表“會員”後選擇訂單查詢"));
        qaList.add(new QA("No.5", "如何點餐", "APP首頁點選餐點圖案後開始點餐"));
        qaList.add(new QA("No.6", "怎麼拿到優惠碼", "小遊戲過關即可拿到優惠碼"));
        qaList.add(new QA("No.7", "營業時間幾點", "營業時間為早上9:00至晚上21:00"));
        qaList.add(new QA("No.8", "反應用餐體驗", "點選下排功能表“會員”後選擇意見箱選擇我要留言"));
    }
}
