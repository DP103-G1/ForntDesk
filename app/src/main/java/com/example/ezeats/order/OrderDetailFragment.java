package com.example.ezeats.order;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class OrderDetailFragment extends Fragment {
    private static final String TAG = "TAG_OrderDetailFragment";
    private Navigation navigation;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvTitle;
    private RecyclerView rvOd;
    private Activity activity;
    private CommonTask orderGetAllTask;
    private List<MenuDetail> menuDetails;
    private int memId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        memId = Common.getMemId(activity);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textMenuStatus);
        rvOd = view.findViewById(R.id.rvOd);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvOd.setLayoutManager(new LinearLayoutManager(activity));

        menuDetails = getMenuDetail();
        showMenuDetail(menuDetails);
    }

    private List<MenuDetail> getMenuDetail() {
        List<MenuDetail> menuDetails = new ArrayList<>();
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/MenuDetailServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllByMemberId");
            jsonObject.addProperty("memberId", memId);
            String jsonOut = jsonObject.toString();
            orderGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = orderGetAllTask.execute().get();
                Type listType = new TypeToken<List<MenuDetail>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                menuDetails = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
            return menuDetails;

    }

    private void showMenuDetail(List<MenuDetail> menuDetails) {
        if (menuDetails == null || menuDetails.isEmpty()) {
            Common.showToast(activity, R.string.textNOMenu);
        }
        OrderDetailAdapter orderDetailAdapter = (OrderDetailAdapter) rvOd.getAdapter();

        if (orderDetailAdapter == null) {
            rvOd.setAdapter(new OrderDetailAdapter(activity, menuDetails));
        } else {
            orderDetailAdapter.setMenuDetail(menuDetails);
            orderDetailAdapter.notifyDataSetChanged();
        }
    }

    public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyviewHolder> {
        private LayoutInflater layoutInflater;
        private List<MenuDetail> menuDetails;

        public OrderDetailAdapter(Context context, List<MenuDetail> menuDetails) {
            layoutInflater = LayoutInflater.from(context);
            this.menuDetails = menuDetails;
        }

        public void setMenuDetail(List<MenuDetail> menuDetails) {
            this.menuDetails = menuDetails;
        }

        class MyviewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvAmount, tvPrice, tvStatus;

            public MyviewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvAmount = itemView.findViewById(R.id.tvAmount);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvStatus = itemView.findViewById(R.id.tvStatus);
            }
        }

        @Override
        public int getItemCount() {
            return menuDetails.size();
        }


        @NonNull
        @Override
        public OrderDetailAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        }

    }

}
