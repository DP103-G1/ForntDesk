package com.example.ezeats.HowTo;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        qaList.add(new QA("NO.1", "如何加入會員", "可在首頁點選註冊會員"));
        qaList.add(new QA("No.2", "忘記帳號密碼", "登入畫面中請點選忘記密碼，經驗證後重新設定您的新的帳號或密碼"));
        qaList.add(new QA("No.3", "如何訂位", "點選下排功能表椅子圖案開始訂位"));
        qaList.add(new QA("No.4", "如何查詢訂單", "點選下排功能表放大鏡圖案查詢訂單"));
        qaList.add(new QA("No.5", "如何訂餐", "點選下排功能表刀叉圖案選擇餐點"));
        qaList.add(new QA("No.6", "怎麼拿到優惠碼", "經小遊戲過關即可拿到優惠碼"));
        qaList.add(new QA("No.7", "營業時間幾點", "營業時間為早上11:00至晚上22:00週ㄧ休息"));
        qaList.add(new QA("No.8", "反應用餐體驗", "點選下排功能表表格圖案連結意見箱始反映意見"));
        qaList.add(new QA("No.9", "付款方式", "目前提供QR code結帳付款"));
    }
}
