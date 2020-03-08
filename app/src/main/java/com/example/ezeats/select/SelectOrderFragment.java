package com.example.ezeats.select;


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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.order.Order;
import com.example.ezeats.task.CommonTask;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class SelectOrderFragment extends Fragment {
    private static final String TAG = "TAG_SelectOrderFragment";
    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvSO;
    private List<Order> orders;
    private CommonTask OrderGetAllTask;
    private int memId;
    private TextView tvTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_select_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textSelectOrder);

        memId = Common.getMemId(activity);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvSO = view.findViewById(R.id.rvSO);
        rvSO.setLayoutManager(new LinearLayoutManager(activity));
        orders = getorders();
        showorders(orders);


        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            showorders(orders);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private List<Order> getorders() {
        List<Order> orders = null;
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/OrderServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllByMemberId");
            jsonObject.addProperty("memberId", memId);
            String jsonOut = jsonObject.toString();
            OrderGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = OrderGetAllTask.execute().get();
                Type listType = new TypeToken<List<Order>>() {
                }.getType();
                orders = Common.gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return orders;
    }

    private void showorders(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            Common.showToast(activity, R.string.textNoOrder);
        }
        OrderAdapter orderAdapter = (OrderAdapter) rvSO.getAdapter();

        if (orderAdapter == null) {
            rvSO.setAdapter(new OrderAdapter(activity, orders));
        } else {
            orderAdapter.setOrders(orders);
            orderAdapter.notifyDataSetChanged();
        }

    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Order> orders;

        OrderAdapter(Context context, List<Order> orders) {
            layoutInflater = LayoutInflater.from(context);
            this.orders = orders;
        }

        void setOrders(List<Order> orders) {
            this.orders = orders;
        }

        class OrderViewHolder extends RecyclerView.ViewHolder {
            TextView tvOrdid, tvTabid, tvTotal, tvBill;
            boolean bill;

            OrderViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrdid = itemView.findViewById(R.id.tvOrdId);
                tvTabid = itemView.findViewById(R.id.tvTabid);
                tvTotal = itemView.findViewById(R.id.tvTotal);
                tvBill = itemView.findViewById(R.id.tvBill);


            }

            public void setStatus(boolean ord_bill) {
                this.bill = ord_bill;
            }
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_view_order, parent, false);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Order order = orders.get(position);
            holder.tvOrdid.setText(String.valueOf(order.getORD_ID()));
            holder.tvTabid.setText(String.valueOf(order.getBK_ID()));
            holder.tvTotal.setText(String.valueOf(order.getORD_TOTAL()));
            holder.setStatus(order.isORD_BILL());
            if (order.isORD_BILL()) {
                holder.tvBill.setText("已結單");
            } else {
                holder.tvBill.setText("尚未結帳");
            }
            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("order", order.getORD_ID());
                Navigation.findNavController(v).navigate(R.id.selectMenuDetailFragment, bundle);
            });
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (OrderGetAllTask != null) {
            OrderGetAllTask.cancel(true);
            OrderGetAllTask = null;
        }
    }
}
