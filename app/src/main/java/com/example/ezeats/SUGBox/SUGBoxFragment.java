package com.example.ezeats.SUGBox;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SUGBoxFragment extends Fragment
    implements DatePickerDialog.OnDateSetListener{
    private final static String TAG = "TAG_SUGBoxFragment";
    private FragmentActivity activity;
    private TextView tvShowDate, tvTitle;
    private static int year,month,day;
    private SimpleDateFormat simpleDateFormat;
    private EditText etTopicKeyIn,etPurpose,etSource,etMessage;
    private Button btQuik;
    private RatingBar ratingBar;
    private int mem_id;
    private String textMessage;
    private TabLayout tabLayout;
    public SUGBoxFragment(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        activity = getActivity();
        mem_id = Common.getMemId(activity);
        Log.d(TAG,String.valueOf(mem_id));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.fragment_sugbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textconnection);
        etTopicKeyIn = view.findViewById(R.id.etTopicKeyIn);
        etPurpose = view.findViewById(R.id.etPurpose);
        etSource = view.findViewById(R.id.etSource);
        etMessage = view.findViewById(R.id.etMessage);
        btQuik = view.findViewById(R.id.btQuik);
        ratingBar =view.findViewById(R.id.ratingBar);
        tvShowDate = view.findViewById(R.id.tvShowDate);
        showNow();

        Button btDatePicker = view.findViewById(R.id.btDatePicker);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(
                        activity,
                        SUGBoxFragment.this,
                        SUGBoxFragment.year,SUGBoxFragment.month,SUGBoxFragment.day);
                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String text = rating + "顆星";
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });


        Button btSent = view.findViewById(R.id.btSent);
        btSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = etTopicKeyIn.getText().toString().trim();
                if (topic.length() == 0){
                    etTopicKeyIn.setError("請輸入標題");
                    return;
                }
                String date = tvShowDate.getText().toString().trim();
                if (date.length() == 0){
                    tvShowDate.setError("請選擇用餐日期");
                    return;
                }

                String purpose = etPurpose.getText().toString().trim();
                String info = etSource.getText().toString().trim();
                String feed_back = etMessage.getText().toString().trim();
                String dateTime = tvShowDate.getText().toString().trim();
                Float satisfied = ratingBar.getRating();

                if(Common.networkConnected(activity)) {
                    String url = Url.URL + "/BoxServlet";

                    Box box = new Box(mem_id, topic, purpose, info, dateTime, satisfied, feed_back);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action","boxInsert");
                    jsonObject.addProperty("box",gson.toJson(box));
                    int count = 0;
                    try {
                        String result = new CommonTask(url,jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    }catch (Exception e){
                        Log.e(TAG,toString());
                    }
                    if (count == 0){
                        Common.showToast(getActivity(),R.string.textNoFail);
                    }else {
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.textBoxSuccess)
                                .setMessage(R.string.textMessage)
                                .setPositiveButton(R.string.textConfirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        navController.navigate(R.id.listBoxFragment);
                                        tabLayout.setScrollX(tabLayout.getWidth());
                                        tabLayout.getTabAt(1).select();
                                    }
                                })
                                .show();
                    }
                }else {
                    Common.showToast(getActivity(),R.string.textDisconnected);
                }
            }

        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 200) {
                    textMessage = s.subSequence(0, 200).toString();
                    etMessage.setText(textMessage);
                    etMessage.setSelection(textMessage.length());
                }
            }
        });

        btQuik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTopicKeyIn.setText("太鹹了！");
                etPurpose.setText("家庭聚餐");
                etSource.setText("雜誌報導");
                etMessage.setText("你好，你們家東西很好吃，但是吃到最後有點太鹹了，要喝大口水才消除鹹味 （哭）");
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        SUGBoxFragment.year = year;
        SUGBoxFragment.month = month;
        SUGBoxFragment.day = day;
        updateDisplay();
    }

    private void showNow(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
    }

    private void updateDisplay(){
        tvShowDate.setText(new StringBuilder().append(year).append("-").append(pad(month+1))
                .append("-").append(pad(day)));
    }

    private String pad(int number){
        if (number >= 10){
            return String.valueOf(number);
        }else {
            return "0" + number;
        }
    }

}
