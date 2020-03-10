package com.example.ezeats.order;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Table;
import com.example.ezeats.main.Url;
import com.example.ezeats.socket.SocketMessage;
import com.example.ezeats.task.CommonTask;
import com.example.ezeats.task.ImageTask;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderFragment extends Fragment {
    private static final String TAG = "TAG_OrderFragment";
    private RecyclerView rvMenu;
    private TextView edTotal, tvTitle;
    private ImageView btBell;
    private Button btChect;
    private Activity activity;
    private CommonTask menuGetAllTask;
    private ImageTask menuImageTask;
    private List<Menu> menus;
    private int totalPrice;
    private Set<MenuDetail> menuDetails;
    private SharedPreferences pref;
    private Table table;
    private LocalBroadcastManager broadcastManager;
    private int memberId;
    private TabLayout tabLayout;

    public OrderFragment(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_order, container, false);

    }



    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(activity);
        registerSocketReceiver();
        pref = activity.getSharedPreferences(Common.MEMBER_PREFRENCE, Context.MODE_PRIVATE);
        memberId = Common.getMemId(activity);
        final NavController navController = Navigation.findNavController(view);
        table = getTable(memberId);
        menuDetails = new HashSet<>();
        SearchView searchView = view.findViewById(R.id.searchView);
        totalPrice = 0;
        edTotal = view.findViewById(R.id.edTotal);
        edTotal.setText(String.valueOf(totalPrice));
        rvMenu = view.findViewById(R.id.rvMenu);
        btBell = view.findViewById(R.id.btbell);
        btChect = view.findViewById(R.id.btChect);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textMenu);

        rvMenu.setLayoutManager(new LinearLayoutManager(activity));
        menus = getMenu();
        showMenu(menus);

        if (table.isStatus()) {
            btBell.setBackgroundColor(Color.RED);
            btBell.setEnabled(false);
        } else {
            btBell.setBackgroundColor(Color.argb(0, 0, 0, 0));
            btBell.setEnabled(true);
        }
        btBell.setOnClickListener(v -> {
            table.setStatus(true);
            String url = Url.URL + "/TableServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "updateStatus");
            jsonObject.addProperty("table", Common.gson.toJson(table));
            int count = 0;
            try {
                String result = new CommonTask(url, jsonObject.toString()).execute().get();
                count = Integer.parseInt(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count != 0) {
                SocketMessage socketMessage = new SocketMessage("service", "waiter", Common.gson.toJson(table));
                Common.eZeatsWebSocketClient.send(new Gson().toJson(socketMessage));
                v.setBackgroundColor(Color.RED);
            } else {
                Common.showToast(activity, R.string.textServerFail);
            }
        });

        btChect.setOnClickListener(v -> {
            if (totalPrice <= 0){
                Common.showToast(getActivity(), R.string.noMenuDetail);
                return;
            }
            if(Common.networkConnected(activity)) {
               String url = Url.URL + "/OrderServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action","add");
                Order order = new Order(memberId, table.getORD_ID(),
                        Integer.parseInt(edTotal.getText().toString()),
                        new ArrayList<>(menuDetails));
                jsonObject.addProperty("order", Common.gson.toJson(order));
                int count = 0;
                try {
                    String result = new CommonTask(url, jsonObject.toString()).execute().get();
                    count = Integer.valueOf(result);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (count == 0) {
                    Common.showToast(getActivity(), R.string.textInsertFail);
                } else {
                    final int ordId = count;
                    Common.showToast(getActivity(), R.string.textInsertSuccess);
                    menuDetails.forEach(menuDetail -> menuDetail.setORD_ID(ordId));
                    SocketMessage socketMessage = new SocketMessage("menuDetail", "kitchen", Common.gson.toJson(menuDetails));
                    Common.eZeatsWebSocketClient.send(Common.gson.toJson(socketMessage));
                    socketMessage.setReceiver("waiter");
                    Common.eZeatsWebSocketClient.send(Common.gson.toJson(socketMessage));
                }
            } else {
                Common.showToast(getActivity(), R.string.textNoNetwork);
            }
            tabLayout.setScrollX(tabLayout.getWidth());
            tabLayout.getTabAt(1).select();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showMenu(menus);
                } else {
                    List<Menu> searchMenu = new ArrayList<>();
                    for (Menu menu : menus) {
                        if (menu.getFOOD_NAME().toUpperCase().contains(newText.toUpperCase())) {
                            searchMenu.add(menu);
                        }
                    }
                    showMenu(searchMenu);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void registerSocketReceiver() {
        IntentFilter filter = new IntentFilter("service");
        broadcastManager.registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SocketMessage socketMessage = (SocketMessage) intent.getSerializableExtra("socketMessage");
            if (socketMessage.getReceiver().equals("member" + memberId)) {
                btBell.setBackgroundColor(Color.argb(0, 0, 0, 0));
                btBell.setEnabled(true);
            }
        }
    };

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

    private List<Menu> getMenu() {
        List<Menu> menus = null;
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/MenuServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllShow");
            String jsonOut = jsonObject.toString();
            menuGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = menuGetAllTask.execute().get();
                Type ListType = new TypeToken<List<Menu>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                menus = gson.fromJson(jsonIn, ListType);
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
        MenuAdapter menuAdapter = (MenuAdapter) rvMenu.getAdapter();

        if (menuAdapter == null) {
            rvMenu.setAdapter(new MenuAdapter(activity, menus));
        } else {
            menuAdapter.setMenus(menus);
            menuAdapter.notifyDataSetChanged();
        }
    }


    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Menu> menus;
        private int imageSize;
        MenuAdapter(Context context, List<Menu> menus) {
            layoutInflater = LayoutInflater.from(context);
            this.menus = menus;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setMenus(List<Menu> menus) {
            this.menus = menus;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            int amount;
            int price;
            int bkid;
            String id;
            ImageView imageView;
            TextView tvName, tvPrice, tvAmount;
            Button btAdd, btLess;


            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivMenu);
                tvName = itemView.findViewById(R.id.tvName);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvAmount = itemView.findViewById(R.id.tvAmount);
                amount = Integer.parseInt(tvAmount.getText().toString());
                btAdd = itemView.findViewById(R.id.btadd);
                btAdd.setOnClickListener(v -> {
                    amount++;
                    tvAmount.setText(String.valueOf(amount));
                    totalPrice += price;
                    edTotal.setText(String.valueOf(totalPrice));
                    MenuDetail menuDetail = new MenuDetail(0, id, table.getTableId(),
                            tvName.getText().toString(), amount, false,
                            price * amount, false);
                    menuDetails.remove(menuDetail);
                    menuDetails.add(menuDetail);
                });
                btLess = itemView.findViewById(R.id.btless);
                btLess.setOnClickListener(v -> {
                    if(amount > 0) {
                        amount--;
                        tvAmount.setText(String.valueOf(amount));
                        totalPrice -= price;
                        edTotal.setText(String.valueOf(totalPrice));
                        MenuDetail menuDetail = new MenuDetail(0, id, table.getTableId(),
                                tvName.getText().toString(), amount, false,
                                price * amount, false);
                        menuDetails.remove(menuDetail);
                        if (amount != 0) {
                            menuDetails.add(menuDetail);
                        }
                    } else {
                        Common.showToast(activity, R.string.textNOFood);
                    }
                });
            }

            public void setPrice(int price) {
                this.price = price;
            }
            public void setId(String id) {
                this.id = id;
            }

        }

        @Override
        public int getItemCount() {
            return menus.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_menu, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Menu menu = menus.get(position);
            String url = Url.URL + "/MenuServlet";
            String id = menu.getMENU_ID();
            menuImageTask = new ImageTask(url, id, imageSize, holder.imageView);
            menuImageTask.execute();
                holder.tvName.setText(menu.getFOOD_NAME());
                holder.tvPrice.setText(String.valueOf(menu.getFOOD_PRICE()));
                holder.setPrice(menu.getFOOD_PRICE());
                holder.setId(menu.getMENU_ID());
            }
        }

    @Override
    public void onStop() {
        super.onStop();
        if (menuGetAllTask != null) {
            menuGetAllTask.cancel(true);
            menuGetAllTask = null;
        }

        if (menuImageTask != null) {
            menuImageTask.cancel(true);
            menuImageTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
