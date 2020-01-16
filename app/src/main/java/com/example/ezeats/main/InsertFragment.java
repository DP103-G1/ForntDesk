package com.example.ezeats.main;



import android.app.AlertDialog;


import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;


import com.example.ezeats.R;
import com.example.ezeats.CommonTask;
import com.example.ezeats.ImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//
public class InsertFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final  static String TAG = "TAG_InsertFragment";
    private FragmentActivity activity;
    private EditText etPhone,etDate,etTimer;
    private Spinner spTime,spAdult,spChild;
    private static int year, month, day;
    private CommonTask bookingGetAllTask;
    private ImageTask bookingImageTask;
    private SimpleDateFormat simpleDateFormat;
    private static int minute,hour;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insert, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            etPhone = view.findViewById(R.id.etPhone);
            etDate =view.findViewById(R.id.etDate);

            spTime = view.findViewById(R.id.spTime);
            spTime.setSelection(0,true);
            spTime.setOnItemSelectedListener(listener);

            spAdult = view.findViewById(R.id.spAdult);
            spAdult.setSelection(0,true);
            spAdult.setOnItemSelectedListener(listener);

            spChild = view.findViewById(R.id.spChild);
            spChild.setSelection(0,true);
            spChild.setOnItemSelectedListener(listener);

            etDate = view.findViewById(R.id.etDate);
            showNow();
            etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        InsertFragment.this,InsertFragment.year,
                        InsertFragment.month,InsertFragment.day);
                        DatePicker datePicker = datePickerDialog.getDatePicker();
                        datePicker.setMinDate(new Date().getTime());
                        datePickerDialog.show();

                }
            });
            etTimer = view.findViewById(R.id.etTimer);

            etTimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(activity,InsertFragment.this
                            ,InsertFragment.hour,InsertFragment.minute,true);
                             timePickerDialog.show();


                }
            });


            Button btInsert = view.findViewById(R.id.btInsert);
            btInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Date bkDate = null;
                    try {
                        bkDate = simpleDateFormat.parse(etDate.getText().toString().trim());
                    } catch (ParseException e) {
                        Log.e(TAG, e.toString());
                    }

                    showTime();


                    String[] timeArray = getResources().getStringArray(R.array.textTimeArray);
                    String bkTime = timeArray[spTime.getSelectedItemPosition()];
                    if(bkTime.equals("Select")){
                        Common.showToast(getActivity(),R.string.textNoSelect);
                        return;
                    }




                    String[] childArray = getResources().getStringArray(R.array.textChildArray);
                    String bkChild = String.valueOf(childArray[spChild.getSelectedItemPosition()]);
                    if(bkChild.equals("Select")){
                        Common.showToast(getActivity(),R.string.textNoSelect);
                        return;
                    }

//                    Resources res = getResources();
                    String[] adultArray = getResources().getStringArray(R.array.textAdultArray);
                    String bkAdult = adultArray[spAdult.getSelectedItemPosition()];
                    if(bkAdult.equals("Select")){
                        Common.showToast(getActivity(),R.string.textNoSelect);
                        return;
                    }

                    String bkPhone = etPhone.getText().toString().trim();
                    if (bkPhone.length() <= 0) {
                        Common.showToast(getActivity(), R.string.textPhoneInvaild);
                        return;
                    }
                   if(Common.networkConnected(activity)){
                       String url = Common.URL_SERVER + "BookingServlet";
                       Booking booking = new Booking(bkTime, bkDate, bkChild,bkAdult, bkPhone);
                       Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                       JsonObject jsonObject = new JsonObject();
                       jsonObject.addProperty("action","bookingInsert");
                       jsonObject.addProperty("booking",gson.toJson(booking));
                       int count = 0;
                       try {
                           String result = new CommonTask(url,jsonObject.toString()).execute().get();
                           count = Integer.valueOf(result);
                       }catch (Exception e){
                           Log.e(TAG,toString());
                       }
                       if (count == 0){
                           Common.showToast(getActivity(),R.string.textInsertFail);
                       }else {
                          new AlertDialog.Builder(activity)
                                  .setTitle(R.string.textBookingSuccess)
                                  .setMessage(R.string.textMassage)
                                  .setPositiveButton(R.string.textYes, new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int which) {
                                          navController.popBackStack();
                                      }
                                  })
                                  .show();

                       }
                   }else {
                       Common.showToast(getActivity(),R.string.textNoNetWork);
                   }
                   navController.popBackStack();
                }

            });


    }

     Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
};

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        InsertFragment.year = year;
        InsertFragment.month = month;
        InsertFragment.day = day;
        updateDisplay();
    }



    private String pad(int number){
        if(number>=10){
            return String.valueOf(number);
        }else {
            return  "0" + number;
        }
    }

    private void showNow() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        updateDisplay();

    }

    private void updateDisplay() {
        etDate.setText(new StringBuffer().append(year).append("-")
        .append(pad(month+1)).append("-")
        .append(day));
    }



    @Override
    public void onStop() {
        super.onStop();
        if (bookingGetAllTask !=null){
           bookingGetAllTask.cancel(true);
            bookingGetAllTask = null;
        }
        if (bookingImageTask!=null){
            bookingImageTask.cancel(true);
            bookingImageTask = null;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        InsertFragment.hour = hourOfDay ;
        InsertFragment.minute = minute;
        updateTime();
    }

    private void showTime() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        int minuteOfDay = hour * 60 + minute;
        final int start = 9 * 60 ;
        final int end = 11 * 60;
        if(minuteOfDay>=start&&minuteOfDay<=end){
            updateTime();
        }else {
            Common.showToast(getActivity(),R.string.textTimeOver);
        }

    }

    private void updateTime(){
        etTimer.setText(new StringBuilder()
                .append(pad(hour)).append(":")
                .append(pad(minute)));
    }

}
