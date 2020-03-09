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
        qaList.add(new QA("NO.1", "訂位人數有限制嗎？", "坐位最多容納4位成人，幼兒提供兒童座椅"));
        qaList.add(new QA("No.2", "消費方式及規範為何？", "每人需各點一份餐，不可共食"));
        qaList.add(new QA("No.3", "候位時間多久？", "平日約5~10分，假日20~40分"));
        qaList.add(new QA("No.4", "用餐時間多久？", "用餐時間為90分鐘"));
        qaList.add(new QA("No.5", "餐點有哪些？", "本餐廳提供各式牛排供給您選擇"));
        qaList.add(new QA("No.6", "如果訂錯日期怎麼辦？", "請取消訂位重新訂位"));
        qaList.add(new QA("No.7", "肉品來源？", "牛肉使用澳洲進口牛，豬肉使用台灣國產豬"));
        qaList.add(new QA("No.8", "餐廳是否提供WI-FI無線上網？", "有的，本餐廳有提供免費WI-FI"));
        qaList.add(new QA("No.9", "優惠碼怎麼用？", "小遊戲過關使獲得優惠碼並可於用餐當日結帳時使用"));
        qaList.add(new QA("No.10", "可不可以帶寵物進餐廳？", "可以攜帶寵物，但須將寵物安置於寵物籃內，全程不能放出逗玩"));
    }
}
