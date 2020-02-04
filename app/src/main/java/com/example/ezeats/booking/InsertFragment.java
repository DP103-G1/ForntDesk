package com.example.ezeats.booking;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

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
import com.example.ezeats.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//

public class InsertFragment extends Fragment{
    private final  static String TAG = "TAG_InsertFragment";
    private FragmentActivity activity;
    private EditText etPhone,etDate;
    private Spinner spTime,spAdult,spChild,spTable;
    private static int year, month, day;
    private CommonTask bookingGetAllTask,getTableTask;
    private ImageTask bookingImageTask;
    private SimpleDateFormat simpleDateFormat;
    private static int hour,minute;
    private Date bkDate;
    private String bkTime, mem_id;
    private SharedPreferences pref;
    private List<Booking> bookings;
    private List<String> tableIds;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        pref = activity.getSharedPreferences(Common.MEMBER_PREFRENCE, Context.MODE_PRIVATE);
        mem_id = pref.getString("mem_id", null);
        mem_id = "0";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insert, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        bookings = getBookings();
        tableIds = getTableIds();
        final NavController navController = Navigation.findNavController(view);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        etPhone = view.findViewById(R.id.etPhone);
        spTime = view.findViewById(R.id.spTime);

        String[] timeArray = getResources().getStringArray(R.array.textTimeArray);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,android.R.layout.simple_spinner_item,timeArray);
        bkDate = null;
        bkTime = null;
        spTime.setAdapter(arrayAdapter);
        spTime.setSelection(0,true);
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bkTime = timeArray[position];
                spTable.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, comparison()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAdult = view.findViewById(R.id.spAdult);
        spAdult.setSelection(0,true);
        spAdult.setOnItemSelectedListener(listener);

        spChild = view.findViewById(R.id.spChild);
        spChild.setSelection(0,true);
        spChild.setOnItemSelectedListener(listener);


        etDate = view.findViewById(R.id.etDate);
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity);
                    DatePicker datePicker = datePickerDialog.getDatePicker();
                    datePicker.setMinDate(new Date().getTime());
                    datePickerDialog.show();
            datePickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                updateDisplay(year, month, dayOfMonth);
                spTable.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, comparison()));
            });
        });
        spTable = view.findViewById(R.id.spTable);
//            Set<String> tables = new HashSet<String>();
        ArrayAdapter<String> tableArrayAdapter = new ArrayAdapter<>(activity,android.R.layout.simple_spinner_item, comparison());
        spTable.setAdapter(tableArrayAdapter);
        spTable.setSelection(0,true);
        spTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    String[] timeArray = getResources().getStringArray(R.array.textTimeArray);
                    String bkTime = String.valueOf(spTime.getSelectedItemPosition());
                    if(bkTime.equals("Select")){
                        Common.showToast(getActivity(),R.string.textNoSelect);
                        return;
                    }
                    String bkTable = tableIds.get(spTable.getSelectedItemPosition());
                    if (bkTable.equals("Select")){
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
                       String url = Url.URL + "/BookingServlet";
                       Booking booking = new Booking(bkTable, bkTime, bkDate, bkChild, bkAdult, bkPhone);
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



    private String pad(int number){
        if(number>=10){
            return String.valueOf(number);
        }else {
            return  "0" + number;
        }
    }

    private void updateDisplay(int year, int month, int day) {
        etDate.setText(new StringBuffer().append(year).append("-")
        .append(pad(month+1)).append("-")
        .append(pad(day)));
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

//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        InsertFragment.hour = hourOfDay ;
//        InsertFragment.minute = minute;
//        updateTime();
//    }

//    private void showTime() {
//        Calendar calendar = Calendar.getInstance();
//        long time = System.currentTimeMillis();
//        calendar.setTimeInMillis(time);
//        hour = calendar.get(Calendar.HOUR_OF_DAY);
//        minute = calendar.get(Calendar.MINUTE);
//        int morningstart = (int) (time * 60) ;
//        int morningend = 11 * 60 ;
//        int afternoonstart = 12 * 60;
//        int afternoonend = 15 * 60;
//        int nightstart = 17 * 60;
//        int nightend = 21 * 60;
//        if (morningstart <= time && morningend >= time){
//            updateTime();
//        }else if (afternoonstart <= time && afternoonend >= time){
//            updateTime();
//        }else if (nightstart <= time && nightend >= time){
//            updateTime();
//        }else {
//            Common.showToast(getActivity(),R.string.textTimeOver);
//
//        }
//
//    }

//    private void updateTime(){
//        etTimer.setText(new StringBuilder()
//                .append(pad(hour)).append(":")
//                .append(pad(minute)));
//    }
//
    private List<String> comparison(){
        List<String> tablesAvalible = tableIds.stream().collect(Collectors.toList());
        tablesAvalible.add(0, "Select");
        if (!etDate.getText().toString().isEmpty()) {
            try {
                bkDate = simpleDateFormat.parse(etDate.getText().toString());
            } catch (ParseException e) {
                Log.e(TAG, e.toString());
            }
        }
        if (spTime.getSelectedItemPosition() != 0 && etDate != null) {
            Log.d(TAG, simpleDateFormat.format(bkDate) + " " + bkTime);
            List<String> tablesOrdered = bookings.stream().filter(v -> v.getBkTime().equals(bkTime)
                    && v.getBkDate().equals(bkDate))
                    .flatMap(v -> Stream.of(v.getTableId()))
                    .collect(Collectors.toList());
            tablesOrdered.forEach(tablesAvalible::remove);
            return tablesAvalible;
        }
        List<String> tablesNoSelect = new ArrayList<>();
        tablesNoSelect.add("Select");
        return tablesNoSelect;
    }

    private List<String> getTableIds() {

        List<Table> tables = new ArrayList<>();

        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/TableServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            getTableTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = getTableTask.execute().get();
                Type listType = new TypeToken<List<Table>>() {}.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                tables = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, toString());
            }
            List<String> tableIds = tables.stream()
                    .flatMap(v -> Stream.of(v.getTableId())).collect(Collectors.toList());
        }else {
            Common.showToast(getActivity(),R.string.textNoNetWork);
        }
        List<String> tableIds = new ArrayList<>();
        for (Table table : tables) {
            tableIds.add(table.getTableId());
        }
//        List<String> tableIds = tables.stream()
//                .flatMap(v -> Stream.of(v.getTableId())).collect(Collectors.toList());
        return tableIds;
    }

    private List<Booking> getBookings() {

        List<Booking> bookings = new ArrayList<>();

        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/BookingServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            getTableTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = getTableTask.execute().get();
                Type listType = new TypeToken<List<Booking>>() {}.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                bookings = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, toString());
            }
        }else {
            Common.showToast(getActivity(),R.string.textNoNetWork);
        }
        return bookings;
    }

}
