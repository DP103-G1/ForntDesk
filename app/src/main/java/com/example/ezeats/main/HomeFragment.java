package com.example.ezeats.main;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezeats.R;
import com.example.ezeats.order.Menu;
import com.example.ezeats.task.CommonTask;
import com.example.ezeats.task.ImageTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "TAG_HomeFragment";
    private Activity activity;
    private NavController navController;
    private RecyclerView rvHome;
    private CommonTask homeGetAllTask, getRanImagesTask;
    private ImageTask homeImageTask;
    private List<Menu> menus;
    private BottomNavigationView bottomNavigationView;
    private ViewFlipper flipperImage;

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
        flipperImage = view.findViewById(R.id.flipperImage);
        flipperImages(getMenuImages());
        rvHome.setLayoutManager(new LinearLayoutManager(activity));
        menus = getMenu();
        showMenu(menus);
        navController = Navigation.findNavController(view);
        handledViews();
        bottomNavigationView.setVisibility(View.VISIBLE);
        flipperImage.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_orderFragment));
    }

    private List<Menu> getMenu() {
        List<Menu> menus = null;
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/MenuServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllShow");
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

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
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
            holder.itemView.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_orderFragment));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (homeGetAllTask != null) {
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

    private List<byte[]> getMenuImages() {
        List<String> base64Images = null;
        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/MenuServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getImageList");
            String jsonOut = jsonObject.toString();
            getRanImagesTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = getRanImagesTask.execute().get();
                Type listType = new TypeToken<List<String>>() {
                }.getType();
                base64Images = Common.gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return getImages(base64Images);
    }

    private List<byte[]> getImages(List<String> base64Images) {
        List<byte[]> images = new ArrayList<>();
        for (String base64Image : base64Images) {
            byte[] image = Base64.decode(base64Image, Base64.DEFAULT);
            images.add(image);
        }

        return images;
    }

    private void flipperImages(List<byte[]> images) {
        for (byte[] image : images) {
            ImageView imageView = new ImageView(activity);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageView.setImageBitmap(bitmap);
            flipperImage.addView(imageView);
            flipperImage.setFlipInterval(2500);
            flipperImage.setAutoStart(true);
        }
        flipperImage.setInAnimation(activity, android.R.anim.slide_in_left);
        flipperImage.setOutAnimation(activity, android.R.anim.slide_out_right);
        flipperImage.startFlipping();
    }
}
