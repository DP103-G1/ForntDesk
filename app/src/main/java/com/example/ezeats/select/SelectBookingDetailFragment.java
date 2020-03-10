package com.example.ezeats.select;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ezeats.R;
import com.example.ezeats.booking.Booking;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.member.Member;
import com.example.ezeats.task.CommonTask;
import com.example.ezeats.task.ImageTask;

import java.text.SimpleDateFormat;
import java.util.List;


public class SelectBookingDetailFragment extends Fragment {
        private final static String TAG = "TAG_SelectBookingDetailFragment";
        private Activity activity;
        private CommonTask commonTask;
        private TextView tvBkIdGet,tvTableGet,tvTimeGet,
                tvDateGet,tvChildGet,tvAdultGet,tvPhoneGet;
        private Booking selectBookingDetail;
        private  int memberId;
        private TextView tvTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        memberId = Common.getMemId(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_booking_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textBookingDetail);
        tvBkIdGet = view.findViewById(R.id.tvBkIdGet);
        tvTableGet = view.findViewById(R.id.tvTableGet);
        tvTimeGet = view.findViewById(R.id.tvTimeGet);
        tvDateGet = view.findViewById(R.id.tvDateGet);
        tvChildGet = view.findViewById(R.id.tvChildGet);
        tvAdultGet = view.findViewById(R.id.tvAdultGet);
        tvPhoneGet = view.findViewById(R.id.tvPhoneGet);
        Bundle bundle = getArguments();
        if (bundle != null){
            selectBookingDetail = (Booking)bundle.getSerializable("booking");
            showSelectBookingDetail();
        }
//        Button btBack = view.findViewById(R.id.btBack);
//        btBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            navController.navigate(R.id.action_selectBookingDetailFragment_to_selectBookingFragment);
//            }
//        });

    }

    private void showSelectBookingDetail() {
        String url = Url.URL + "/BookingServlet";
//        Booking booking = new Booking(new Member(memberId,null,null,null),)


//        Bitmap bitmap = null;
//        try {
//            bitmap = new ImageTask(url,String.valueOf(booking)).execute().get();
//        }catch (Exception e){
//            Log.e(TAG,e.toString());
//        }

        tvBkIdGet.setText(String.valueOf(selectBookingDetail.getBkId()));
        tvTableGet.setText(String.valueOf(selectBookingDetail.getTableId()));
        tvTimeGet.setText(selectBookingDetail.getBkTime());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(selectBookingDetail.getBkDate());
        tvDateGet.setText(date);
        tvChildGet.setText(selectBookingDetail.getBkChild());
        tvAdultGet.setText(selectBookingDetail.getBkAdult());
        tvPhoneGet.setText(selectBookingDetail.getBkPhone());

    }
}
