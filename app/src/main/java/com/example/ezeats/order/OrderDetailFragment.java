package com.example.ezeats.order;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.socket.SocketMessage;
import com.example.ezeats.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class OrderDetailFragment extends Fragment {
    private static final String TAG = "TAG_OrderDetailFragment";
    private TextView tvTitle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvOd;
    private CommonTask OrderGetAllTask;
    private Activity activity;
    private List<MenuDetail> menuDetails;
    private int memId;
    private Comparator<MenuDetail> cmp =
            Comparator.comparing(MenuDetail::isFOOD_ARRIVAL).thenComparing(v -> !v.isFOOD_STATUS());
    private LocalBroadcastManager broadcastManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textOrderDetail);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        memId = Common.getMemId(activity);
        rvOd = view.findViewById(R.id.rvOd);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            menuDetails = getMenuDetail();
            showMenuDetail(menuDetails);
            swipeRefreshLayout.setRefreshing(false);
        });
        rvOd.setLayoutManager(new LinearLayoutManager(activity));
        menuDetails = getMenuDetail();
        showMenuDetail(menuDetails);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("1", "1");
        broadcastManager = LocalBroadcastManager.getInstance(activity);
        registerSocketReceiver();
    }

    private void registerSocketReceiver() {
        IntentFilter filter = new IntentFilter("menuDetail");
        broadcastManager.registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SocketMessage socketMessage = (SocketMessage) intent.getSerializableExtra("socketMessage");
            Type listType = new TypeToken<List<MenuDetail>>(){}.getType();
            List<MenuDetail> socketMenuDetails = Common.gson.fromJson(socketMessage.getMessage(), listType);
            menuDetails.removeAll(socketMenuDetails);
            menuDetails.addAll(socketMenuDetails);
            menuDetails = menuDetails.stream().sorted(cmp).collect(Collectors.toList());
            showMenuDetail(menuDetails);
        }
    };

    private List<MenuDetail> getMenuDetail() {
        List<MenuDetail> menuDetails = null;
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/MenuDetailServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllByMemberId");
            jsonObject.addProperty("memberId", memId);
            String jsonOut = jsonObject.toString();
            OrderGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = OrderGetAllTask.execute().get();
                Type listType = new TypeToken<List<MenuDetail>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                menuDetails = gson.fromJson(jsonIn, listType);
                menuDetails = menuDetails.stream().sorted(cmp).collect(Collectors.toList());
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return menuDetails;
    }

    private void showMenuDetail(List<MenuDetail> menuDetails) {
        OrderDetailAdapter orderDetailAdapter = (OrderDetailAdapter) rvOd.getAdapter();

        if (orderDetailAdapter == null) {
            rvOd.setAdapter(new OrderDetailAdapter(activity, menuDetails));
        } else {
            orderDetailAdapter.setMenuDetail(menuDetails);
            orderDetailAdapter.notifyDataSetChanged();
        }
    }

    private class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyviewHolder> {
        private LayoutInflater layoutInflater;
        private List<MenuDetail> menuDetails;

        OrderDetailAdapter(Context context, List<MenuDetail> menuDetails) {
            layoutInflater = LayoutInflater.from(context);
            this.menuDetails = menuDetails;
        }

        void setMenuDetail(List<MenuDetail> menuDetails) {
            this.menuDetails = menuDetails;
        }

        class MyviewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvAmount, tvPrice, tvStatus;

             MyviewHolder(@NonNull View itemView) {
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
        public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_order_detail, parent, false);
            return new MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderDetailAdapter.MyviewHolder holder, int position) {
            final MenuDetail menuDetail = menuDetails.get(position);
            holder.tvName.setText(menuDetail.getFOOD_NAME());
            holder.tvAmount.setText(String.valueOf(menuDetail.getFOOD_AMOUNT()));
            holder.tvPrice.setText(String.valueOf(menuDetail.getTOTAL()));
            if (menuDetail.isFOOD_ARRIVAL() && menuDetail.isFOOD_STATUS()) {
                holder.tvStatus.setText("已送達");
            } else if (!menuDetail.isFOOD_ARRIVAL() && menuDetail.isFOOD_STATUS()) {
                holder.tvStatus.setText("未送達");
            } else {
                holder.tvStatus.setText("製作中");
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        broadcastManager.unregisterReceiver(receiver);
        if (OrderGetAllTask != null){
            OrderGetAllTask.cancel(true);
            OrderGetAllTask = null;
        }

    }
}
