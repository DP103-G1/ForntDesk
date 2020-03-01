package com.example.ezeats.order;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.example.ezeats.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MenuDetailFragment extends Fragment {
    private static final String TAG = "TAG_MenuDetailFragment";
    private RecyclerView rvMd;
    private Activity activity;
    private CommonTask DetailGetAllTask;
    private ImageTask DetailImageTask;
    private List<MenuDetail> menuDetails;
    private int memId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
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
        memId = Common.getMemId(activity);
        rvMd = view.findViewById(R.id.rvMd);

        rvMd.setLayoutManager(new LinearLayoutManager(activity));
        menuDetails = getMenuDetail();
        showMenuDetail(menuDetails);
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
            String url = Url.URL + "/MenuDetailServlet";
            int memId = menuDetail.getMEMBER_ID();
            String id = menuDetail.getMENU_ID();
            DetailImageTask = new ImageTask(url, id);
            DetailImageTask.execute();
            holder.tvName.setText(menuDetail.getFOOD_NAME());
            holder.tvAmount.setText(String.valueOf(menuDetail.getFOOD_AMOUNT()));
            holder.tvPrice.setText(String.valueOf(menuDetail.getTOTAL()));
            holder.setStatus(menuDetail.isFOOD_ARRIVAL());
            if (menuDetail.isFOOD_ARRIVAL()) {
                holder.tvStatus.setText("已送達");
            } else {
                holder.tvStatus.setText("未送達");
            }


        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DetailGetAllTask != null) {
            DetailGetAllTask.cancel(true);
            DetailGetAllTask = null;
        }
        if (DetailImageTask != null) {
            DetailImageTask.cancel(true);
            DetailImageTask = null;
        }
    }
}
