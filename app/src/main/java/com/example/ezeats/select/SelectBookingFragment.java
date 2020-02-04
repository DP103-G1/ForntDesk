package com.example.ezeats.select;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezeats.R;
import com.example.ezeats.booking.Booking;
import com.example.ezeats.task.CommonTask;

import java.util.List;


public class SelectBookingFragment extends Fragment {
    private static final String TAG = "TAG_SelectBookingFragment";
    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvSelectBooking;
    private List<Booking> selectBooking;
    private CommonTask selecetBookingGetAllTask;


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

    }

//    private List<Booking> getSelectBooking() {
//        List<Booking> bookings = null;
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "BookingServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getAll");
//            String jsonOut = jsonObject.toString();
//            selecetBookingGetAllTask = new CommonTask(url, jsonOut);
//            try {
//                String jsonIn = selecetBookingGetAllTask.execute().get();
//                Type listType = new TypeToken<List<Booking>>() {
//                }.getType();
//                bookings = new Gson().fromJson(jsonIn, listType);
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        } else {
//            Common.showToast(activity, R.string.textNoNetwork);
//        }
//        return bookings;
//    }


}
