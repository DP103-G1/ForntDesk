package com.example.ezeats.qa;


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

import java.util.ArrayList;
import java.util.List;


public class QAFragment extends Fragment {
    private TextView tvTitle;
    private Activity activity;
    private RecyclerView rvHowTo;
    List<QA> qaList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_q_a, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textqa);
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
        qaList.add(new QA("NO.1", "訂位人數最多幾人？", "最多四位成人"));
        qaList.add(new QA("No.2", "如果訂錯日期怎麼辦？", "請取消訂位重新訂位"));
        qaList.add(new QA("No.3", "候位時間多久？", "平日約5~10分，假日20~40分"));
        qaList.add(new QA("No.4", "用餐時間多久？", "用餐時間為90分鐘"));
        qaList.add(new QA("No.5", "餐點有哪些？", "本餐廳提供牛肉套餐、豬肉套餐與蔬食套餐"));
        qaList.add(new QA("No.6", "如果訂錯日期怎麼辦？", "請取消訂位重新訂位"));
        qaList.add(new QA("No.7", "肉品來源？", "牛肉使用澳洲進口牛，豬肉使用台灣國產豬"));
        qaList.add(new QA("No.8", "訂位可預訂多久以前的時間？", "訂位最早可於三週前訂位"));
        qaList.add(new QA("No.9", "優惠碼怎麼用？", "小遊戲過關使獲得優惠碼並可在結帳時使用"));
        qaList.add(new QA("NO.10", "素食者可選的餐點？", "素食者可選擇蔬食套餐"));
    }
}
