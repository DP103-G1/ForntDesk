package com.example.ezeats.order;


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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.MainActivity;
import com.example.ezeats.main.Table;
import com.example.ezeats.main.Url;
import com.example.ezeats.socket.SocketMessage;
import com.example.ezeats.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class MenuDetailFragment extends Fragment {
    private static final String TAG = "TAG_MenuDetailFragment";
    private TextView tvTitle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences preferences;
    private RecyclerView rvMd;
    private Button btBill;
    private TextView tvTotal;
    private EditText edDiscount;
    private MainActivity activity;
    private CommonTask DetailGetAllTask, OrderGetAllTask;
    private List<MenuDetail> menuDetails;
    private List<Order> orders;
    private int memId, distotal;
    private String dis;
    int total = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_menu_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final NavController navigation = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
        Table table = getTable(Common.getMemId(activity));
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textMenuDetail);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        memId = Common.getMemId(activity);
        dis = Common.getDis(activity);
        tvTotal = view.findViewById(R.id.tvTotal);
        edDiscount = view.findViewById(R.id.edDiscount);
        edDiscount.setText(dis);

        rvMd = view.findViewById(R.id.rvMd);
        btBill = view.findViewById(R.id.btBill);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            menuDetails = getMenuDetail();
            showMenuDetail(menuDetails);
            swipeRefreshLayout.setRefreshing(false);
        });

        rvMd.setLayoutManager(new LinearLayoutManager(activity));


        menuDetails = getMenuDetail();
        showMenuDetail(menuDetails);
        orders = getOrder();
        int bkid = table.getORD_ID();
        int ordid = menuDetails.get(0).getORD_ID();
        Log.d(TAG, "ordId = " + ordid);
//        int total = menuDetails.get(0).getORD_TOTAL();
        for (Order order : orders) {
            int ordId = order.getORD_ID();
            if (order.getORD_ID() != ordid) {
                for (MenuDetail menuDetail : menuDetails) {
                    if (!menuDetail.isFOOD_ARRIVAL()) {
                        Common.showToast(activity, R.string.textNoArrival);
                        return;
                    }

                }
                total += order.getORD_TOTAL();
            } else {
                total = order.getORD_TOTAL();
            }
        }
        Log.d(TAG, "total = " + total);

        final boolean[] bill = {menuDetails.get(0).isORD_BILL()};
        if (menuDetails != null && !menuDetails.isEmpty()) {
            tvTotal.setText(String.valueOf(total));
        }

        btBill.setOnClickListener(v -> {
            for (MenuDetail menuDetail : menuDetails) {
                if (!menuDetail.isFOOD_ARRIVAL()) {
                    Common.showToast(activity, R.string.textNoArrival);
                    return;
                }
            }
            String dis = edDiscount.getText().toString().trim();
            if (dis.equals("AA123b")) {
                Common.showToast(getActivity(), R.string.textdisok);
                distotal = (int) (total * 0.9);

            } else if (dis.length() != 0) {
                edDiscount.setError(getString(R.string.textdiserror));
                return;
            } else if (dis.length() == 0) {
                distotal = total;
            }

            if (!bill[0]) {
                bill[0] = true;
                if (Common.networkConnected(activity)) {
                    String url = Url.URL + "/OrderServlet";
                    Order order = new Order(ordid, memId, total, bill[0]);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "update");
                    jsonObject.addProperty("order", new Gson().toJson(order));
                    JsonObject updateTableJsonObjec = new JsonObject();
                    String tableServletUrl = Url.URL + "/TableServlet";
                    updateTableJsonObjec.addProperty("action", "updateBkId");
                    updateTableJsonObjec.addProperty("tableId", table.getTableId());
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        String updateBkIdResule = new CommonTask(tableServletUrl, updateTableJsonObjec.toString()).execute().get();
                        count = Integer.valueOf(result);
                        count += Integer.valueOf(updateBkIdResule);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 2) {
                        table.setORD_ID(0);
                        SocketMessage socketMessage = new SocketMessage("seat", "waiter", Common.gson.toJson(table));
                        Common.eZeatsWebSocketClient.send(Common.gson.toJson(socketMessage));
                        Log.d(TAG, String.valueOf(distotal));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("menudetail", distotal);
                        activity.onResume();
                        Navigation.findNavController(v).navigate(R.id.action_menuDetailFragment_to_billFragment, bundle);
                    } else {
                        Common.showToast(activity, R.string.textNoBill);
                    }
                }
            }
            SharedPreferences pref = activity.getSharedPreferences(Common.NUMBER, Context.MODE_PRIVATE);
            pref.edit().remove("number").apply();
        });

    }


    private List<MenuDetail> getMenuDetail() {
        List<MenuDetail> menuDetails = null;
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/MenuDetailServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllByMemberId");
            jsonObject.addProperty("memberId", memId);
            String jsonOut = jsonObject.toString();
            DetailGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = DetailGetAllTask.execute().get();
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

    private List<Order> getOrder() {
        List<Order> orders = null;
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/MenuDetailServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllByMemberId");
            jsonObject.addProperty("memberId", memId);
            String jsonOut = jsonObject.toString();
            OrderGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = OrderGetAllTask.execute().get();
                Type listType = new TypeToken<List<Order>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                orders = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return orders;
    }

    private void showMenuDetail(List<MenuDetail> menuDetails) {
        if (menuDetails == null || menuDetails.isEmpty()) {
            Common.showToast(activity, R.string.textNOMenu);
        }
        MenuDetailAdapter menuDetailAdapter = (MenuDetailAdapter) rvMd.getAdapter();

        if (menuDetailAdapter == null) {
            rvMd.setAdapter(new MenuDetailAdapter(activity, menuDetails));
        } else {
            menuDetailAdapter.setMenuDetail(menuDetails);
            menuDetailAdapter.notifyDataSetChanged();
        }
    }

    private class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailAdapter.MyviewHolder> {
        private LayoutInflater layoutInflater;
        private List<MenuDetail> menuDetails;

        MenuDetailAdapter(Context context, List<MenuDetail> menuDetails) {
            layoutInflater = LayoutInflater.from(context);
            this.menuDetails = menuDetails;
        }

        public void setMenuDetail(List<MenuDetail> menuDetails) {
            this.menuDetails = menuDetails;
        }

        class MyviewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvAmount, tvPrice, tvStatus;
            boolean foodarrival;

            MyviewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvAmount = itemView.findViewById(R.id.tvAmount);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvStatus = itemView.findViewById(R.id.tvStatus);
            }

            public void setStatus(boolean food_arrival) {
                this.foodarrival = food_arrival;
            }

        }

        @Override
        public int getItemCount() {
            return menuDetails.size();
        }

        @NonNull
        @Override
        public MenuDetailAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_menudetail, parent, false);
            return new MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuDetailAdapter.MyviewHolder holder, int position) {
            final MenuDetail menuDetail = menuDetails.get(position);
            holder.tvName.setText(menuDetail.getFOOD_NAME());
            holder.tvAmount.setText(String.valueOf(menuDetail.getFOOD_AMOUNT()));
            holder.tvPrice.setText(String.valueOf(menuDetail.getTOTAL()));
            holder.setStatus(menuDetail.isFOOD_ARRIVAL());
            if (menuDetail.isFOOD_ARRIVAL() && menuDetail.isFOOD_STATUS()) {
                holder.tvStatus.setText("已送達");
            } else if (!menuDetail.isFOOD_ARRIVAL() && menuDetail.isFOOD_STATUS()) {
                holder.tvStatus.setText("未送達");
            } else {
                holder.tvStatus.setText("製作中");
            }
        }
    }

    private Table getTable(int memId) {
        Table table = null;
        String url = Url.URL + "/TableServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getUsingTableByMemberId");
        jsonObject.addProperty("memberId", memId);
        try {
            String result = new CommonTask(url, jsonObject.toString()).execute().get();
            table = Common.gson.fromJson(result, Table.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return table;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DetailGetAllTask != null) {
            DetailGetAllTask.cancel(true);
            DetailGetAllTask = null;
        }
    }
}
