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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.order.Order;
import com.example.ezeats.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SelectMenuDetailFragment extends Fragment {

    private static final String TAG = "TAG_SelectMenuDetailFragment";
    private RecyclerView rvSmd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity activity;
    private CommonTask DetailGetAllTask;
    private List<Order> selectMenuDetails;
    private int memId;
    private int ordId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        memId = Common.getMemId(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_menu_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSmd = view.findViewById(R.id.rvSmd);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ordId = bundle.getInt("order");
        }
        rvSmd.setLayoutManager(new LinearLayoutManager(activity));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            selectMenuDetails = getSelectMenuDetail();
            showSelectMenuDetail(selectMenuDetails);
            swipeRefreshLayout.setRefreshing(false);
        });
        selectMenuDetails = getSelectMenuDetail();
        showSelectMenuDetail(selectMenuDetails);
    }

    private List<Order> getSelectMenuDetail() {
        List<Order> selectMenuDetails = new ArrayList<>();
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/OrderServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllByOrdId");
            jsonObject.addProperty("ordId", ordId);
            String jsonOut = jsonObject.toString();
            DetailGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = DetailGetAllTask.execute().get();
                Type listType = new TypeToken<List<Order>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                selectMenuDetails = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return selectMenuDetails;
    }

    private class SelectMenuDetailAdapter extends RecyclerView.Adapter<SelectMenuDetailAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Order> selectMenuDetails;
        SelectMenuDetailAdapter(Context context, List<Order> selectMenuDetails) {
            layoutInflater = LayoutInflater.from(context);
            this.selectMenuDetails = selectMenuDetails;
        }

        void setSelectMenuDetails(List<Order> selectMenuDetails) {
            this.selectMenuDetails = selectMenuDetails;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvAmount, tvPrice, tvStatus;
            boolean foodArrival;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvAmount = itemView.findViewById(R.id.tvAmount);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvStatus = itemView.findViewById(R.id.tvStatus);
            }

            void setStatus(boolean foodArrival) {
                this.foodArrival = foodArrival;
            }
        }

        @Override
        public int getItemCount() {
            return selectMenuDetails.size();
        }

        @NonNull
        @Override
        public SelectMenuDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_menudetails, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectMenuDetailAdapter.MyViewHolder holder, int position) {
            final Order selectMenuDetail = selectMenuDetails.get(position);
            holder.tvName.setText(selectMenuDetail.getFOOD_NAME());
            holder.tvAmount.setText(String.valueOf(selectMenuDetail.getFOOD_AMOUNT()));
            holder.tvPrice.setText(String.valueOf(selectMenuDetail.getTOTAL()));
            holder.setStatus(selectMenuDetail.isFOOD_ARRIVAL());
            if (selectMenuDetail.isFOOD_ARRIVAL()) {
                holder.tvStatus.setText("已送達");
            } else {
                holder.tvStatus.setText("未送達");
            }
            holder.itemView.setOnClickListener(v -> {
                    if(!selectMenuDetail.isFOOD_ARRIVAL() || !selectMenuDetail.isORD_BILL())
                    Navigation.findNavController(v).navigate(R.id.action_selectMenuDetailFragment_to_menuDetailFragment);
            });
        }
    }

    private void showSelectMenuDetail(List<Order> selectMenuDetails) {
        if (selectMenuDetails == null || selectMenuDetails.isEmpty()) {
            Common.showToast(activity, R.string.textNOMenu);
        }
        SelectMenuDetailAdapter selectMenuDetailAdapter = (SelectMenuDetailAdapter) rvSmd.getAdapter();
        if (selectMenuDetailAdapter == null) {
            rvSmd.setAdapter(new SelectMenuDetailAdapter(activity, selectMenuDetails));
        } else {
            selectMenuDetailAdapter.setSelectMenuDetails(selectMenuDetails);
            selectMenuDetailAdapter.notifyDataSetChanged();
        }
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
