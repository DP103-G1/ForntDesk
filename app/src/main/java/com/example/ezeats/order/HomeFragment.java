package com.example.ezeats.order;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.example.ezeats.task.ImageTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "TAG_HomeFragment";
    private Activity activity;
    private NavController navController;
    private ImageView homeImage;
    private RecyclerView rvHome;
    private CommonTask homeGetAllTask;
    private ImageTask homeImageTask;
    private List<Menu> menus;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvHome = view.findViewById(R.id.rvHome);

        rvHome.setLayoutManager(new LinearLayoutManager(activity));
        menus = getMenu();
        showMenu(menus);

        navController = Navigation.findNavController(view);
        if (Common.getMemId(activity) == 0) {
            navController.navigate(R.id.action_homeFragment_to_loginFragment);
        }
        handledViews();
        bottomNavigationView.setVisibility(View.VISIBLE);

        Button btOpi = view.findViewById(R.id.btOpi);
        btOpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_linkFragment);
            }
        });
    }

    private List<Menu> getMenu() {
        List<Menu> menus = null;
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/MenuServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            homeGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = homeGetAllTask.execute().get();
                Type listType = new TypeToken<List<Menu>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                menus = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return menus;
    }


    private void showMenu(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            Common.showToast(activity, R.string.textNOMenu);
        }
          MenuAdapter menuAdapter = (MenuAdapter) rvHome.getAdapter();

        if (menuAdapter == null) {
            rvHome.setAdapter(new MenuAdapter(activity, menus));
        } else {
            menuAdapter.setMenus(menus);
            menuAdapter.notifyDataSetChanged();
        }
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder>{
        private LayoutInflater layoutInflater;
        private List<Menu> menus;
        private int imageSize;

        MenuAdapter(Context context, List<Menu> menus) {
            layoutInflater = LayoutInflater.from(context);
            this.menus = menus;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        public void setMenus(List<Menu> menus) {
            this.menus = menus;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivMenu;
            TextView tvName, tvContent;

            MyViewHolder(View itemView) {
                super(itemView);
                ivMenu = itemView.findViewById(R.id.ivMenu);
                tvName = itemView.findViewById(R.id.tvName);
                tvContent = itemView.findViewById(R.id.tvContent);
            }
        }

        @Override
        public int getItemCount() {
            return menus.size();
        }

        @NonNull
        @Override
        public MenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemview = layoutInflater.inflate(R.layout.item_view_home, parent, false);
            return new MyViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Menu menu = menus.get(position);
            String url = Url.URL + "/MenuServlet";
            String id = menu.getMENU_ID();
            homeImageTask = new ImageTask(url, id, imageSize, holder.ivMenu);
            homeImageTask.execute();
            holder.tvName.setText(menu.getFOOD_NAME());
            holder.tvContent.setText(menu.getFOOD_CONTENT());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_orderFragment);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (homeGetAllTask != null){
            homeGetAllTask.cancel(true);
            homeGetAllTask = null;
        }
        if (homeImageTask != null) {
            homeImageTask.cancel(true);
            homeImageTask = null;
        }
    }

    private void handledViews() {
        bottomNavigationView = activity.findViewById(R.id.bv);
    }
}
