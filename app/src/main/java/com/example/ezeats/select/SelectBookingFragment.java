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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezeats.R;
import com.example.ezeats.booking.Booking;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.example.ezeats.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class SelectBookingFragment extends Fragment {
    private static final String TAG = "TAG_SelectBookingFragment";
    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvSelectBooking;
    private List<Booking> selectBooking;
    private CommonTask selecetBookingGetAllTask;
    private ImageTask selectBookingTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout =view.findViewById(R.id.swipeRefreshLayout);
        rvSelectBooking = view.findViewById(R.id.rvSelectBooking);
        rvSelectBooking.setLayoutManager(new LinearLayoutManager(activity));
        selectBooking = getSelectBooking();
        showSelectBooking(selectBooking);

    }

    private void showSelectBooking(List<Booking> selectBooking) {
        if (selectBooking == null || selectBooking.isEmpty()) {
            Common.showToast(activity, R.string.textNoSelectBookingFound);
        }
        SelectBookingAdapter selectBookingAdapter = (SelectBookingAdapter) rvSelectBooking.getAdapter();

        if (selectBookingAdapter == null) {
            rvSelectBooking.setAdapter(new SelectBookingAdapter(activity, selectBooking));
        } else {
            selectBookingAdapter.setSelectBooking(selectBooking);
            selectBookingAdapter.notifyDataSetChanged();
        }
    }


    private class SelectBookingAdapter extends RecyclerView.Adapter<SelectBookingAdapter.SelectBookingHolder> {
        private LayoutInflater layoutInflater;
        private List<Booking> selectBooking;

        SelectBookingAdapter(Context context,List<Booking> selectBooking){
            layoutInflater = LayoutInflater.from(context);
            this.selectBooking = selectBooking;

        }

        void setSelectBooking(List<Booking> selectBooking){
            this.selectBooking = selectBooking;
        }


        @NonNull
        @Override
        public SelectBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_view_selectbooking,parent,false);
            return new SelectBookingHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectBookingHolder selectBookingHolder, int position) {
            Booking booking = selectBooking.get(position);
            String url = Url.URL + "/BookingServlet";
            String memberId = booking.getMemberId();
            selectBookingTask = new ImageTask(url,memberId);
            selectBookingTask.execute();
            selectBookingHolder.tvBkId.setText(booking.getBkId());


        }

        @Override
        public int getItemCount() {
            return selectBooking.size();
        }

        private class SelectBookingHolder extends RecyclerView.ViewHolder {
            TextView tvBkId,tvBkDate;
            public SelectBookingHolder(View view) {
                super(view);
                tvBkDate = view.findViewById(R.id.tvBkDate);
                tvBkId = view.findViewById(R.id.tvBkId);
            }
        }
    }

    private List<Booking> getSelectBooking() {
        List<Booking> selectBooking = null;
        if (Common.networkConnected(activity)){
            String url = Url.URL + "/BookingServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action","getMemberId");
            String jsonOut = jsonObject.toString();
            selecetBookingGetAllTask = new CommonTask(url,jsonOut);
            try {
                String jsonIn = selecetBookingGetAllTask.execute().get();
                Type listType = new TypeToken<List<Booking>>(){
                }.getType();
                selectBooking = new Gson().fromJson(jsonIn,listType);
            }catch (Exception e){
               Log.e(TAG,e.toString());
            }
        }else {
            Common.showToast(activity,R.string.textNoNetWork);
        }


        return selectBooking;
    }


}
