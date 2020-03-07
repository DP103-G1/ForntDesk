package com.example.ezeats.booking;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.ezeats.main.Common;
import com.example.ezeats.R;
import com.example.ezeats.main.Table;
import com.example.ezeats.main.Url;
import com.example.ezeats.member.Member;
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

public class InsertFragment extends Fragment {
    private final static String TAG = "TAG_InsertFragment";
    private FragmentActivity activity;
    private EditText etPhone, etDate;
    private Spinner spTime, spAdult, spChild, spTable;
    private CommonTask bookingGetAllTask, getTableTask;
    private ImageTask bookingImageTask;
    private SimpleDateFormat simpleDateFormat;
    private Date bkDate;
    private String bkTime;
    private int mem_id;
    private SharedPreferences pref;
    private List<Booking> bookings;
    private List<Integer> tableIds;
    private String textPhone;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        mem_id = Common.getMemId(activity);
        Log.d(TAG, String.valueOf(mem_id));
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
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 10) {
                    textPhone = s.subSequence(0, 10).toString();
                    etPhone.setText(textPhone);
                    etPhone.setSelection(textPhone.length());
                }

            }
        });

        spTime = view.findViewById(R.id.spTime);
        String[] timeArray = getResources().getStringArray(R.array.textTimeArray);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, R.layout.myspinner, timeArray);
        arrayAdapter.setDropDownViewResource(R.layout.myspinner);
        bkDate = null;
        bkTime = null;
        spTime.setAdapter(arrayAdapter);
        spTime.setSelection(0, true);
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bkTime = timeArray[position];
                spTable.setAdapter(new ArrayAdapter<>(activity, R.layout.myspinner, comparison()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spAdult = view.findViewById(R.id.spAdult);
        String[] adultArray = getResources().getStringArray(R.array.textAdultArray);
        ArrayAdapter<String> adultAdapter = new ArrayAdapter<>(activity, R.layout.myspinner, adultArray);
        adultAdapter.setDropDownViewResource(R.layout.myspinner);
        spAdult.setAdapter(adultAdapter);
        spAdult.setSelection(0, true);
        spAdult.setOnItemSelectedListener(listener);

        spChild = view.findViewById(R.id.spChild);
        String[] childArray = getResources().getStringArray(R.array.textChildArray);
        ArrayAdapter<String> childAdapter = new ArrayAdapter<>(activity, R.layout.myspinner, childArray);
        childAdapter.setDropDownViewResource(R.layout.myspinner);
        spChild.setAdapter(childAdapter);
        spChild.setSelection(0, true);
        spChild.setOnItemSelectedListener(listener);


        etDate = view.findViewById(R.id.etDate);
        etDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity);
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMinDate(new Date().getTime());
                datePickerDialog.show();
                datePickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                    updateDisplay(year, month, dayOfMonth);
                    spTable.setAdapter(new ArrayAdapter<>(activity, R.layout.myspinner, comparison()));

                });
            }
        });
        spTable = view.findViewById(R.id.spTable);
        ArrayAdapter<String> tableArrayAdapter = new ArrayAdapter<>(activity, R.layout.myspinner, comparison());
        tableArrayAdapter.setDropDownViewResource(R.layout.myspinner);
        spTable.setAdapter(tableArrayAdapter);
        spTable.setSelection(0, true);
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
                String bkTime = timeArray[spTime.getSelectedItemPosition()];
                Log.d(TAG, bkTime);
                if (bkTime.equals("Select")) {
                    Common.showToast(getActivity(), R.string.textTimeNoSelect);
                    return;
                }
                int bkTable;
                String bkTableStr = comparison().get(spTable.getSelectedItemPosition());
                Log.d(TAG, bkTableStr);
                if (bkTableStr.equals("Select")) {
                    Common.showToast(getActivity(), R.string.textTableNoSelect);
                    return;
                } else {
                    bkTable = Integer.parseInt(bkTableStr);
                }


                String[] childArray = getResources().getStringArray(R.array.textChildArray);
                String bkChild = String.valueOf(childArray[spChild.getSelectedItemPosition()]);
                Log.d(TAG, bkChild);
                if (bkChild.equals("Select")) {
                    Common.showToast(getActivity(), R.string.textChildNoSelect);
                    return;
                }


                String[] adultArray = getResources().getStringArray(R.array.textAdultArray);
                String bkAdult = adultArray[spAdult.getSelectedItemPosition()];
                Log.d(TAG, bkAdult);
                if (bkAdult.equals("Select")) {
                    Common.showToast(getActivity(), R.string.textNoSelect);
                    return;
                }

                String bkPhone = etPhone.getText().toString().trim();
                if (bkPhone.length() <= 0) {
                    Common.showToast(getActivity(), R.string.textPhoneInvaild);
                    return;
                }
                int bkStatus = 1;
                if (Common.networkConnected(activity)) {
                    String url = Url.URL + "/BookingServlet";
                    Booking booking = new Booking(new Member(mem_id,null,null,null),
                            bkTable, bkTime, bkDate, bkChild, bkAdult, bkPhone,bkStatus);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "bookingInsert");
                    jsonObject.addProperty("booking", gson.toJson(booking));
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    } catch (Exception e) {
                        Log.e(TAG, toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.textInsertFail);
                    } else {
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.textBookingSuccess)
                                .setMessage(R.string.textMassage)
                                .setPositiveButton(R.string.textYes, (dialog, which) -> {
                                    navController.navigate(R.id.action_insertFragment_to_homeFragment);
                                })
                                .show();

                    }
                } else {
                    Common.showToast(getActivity(), R.string.textNoNetWork);
                }
//                navController.navigate(R.id.action_insertFragment_to_homeFragment);
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


    private String pad(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        } else {
            return "0" + number;
        }
    }

    private void updateDisplay(int year, int month, int day) {
        etDate.setText(new StringBuffer().append(year).append("-")
                .append(pad(month + 1)).append("-")
                .append(pad(day)));
    }


    @Override
    public void onStop() {
        super.onStop();
        if (bookingGetAllTask != null) {
            bookingGetAllTask.cancel(true);
            bookingGetAllTask = null;
        }
        if (bookingImageTask != null) {
            bookingImageTask.cancel(true);
            bookingImageTask = null;
        }
    }


    private List<String> comparison() {
        List<Integer> tablesAvalible = tableIds.stream().collect(Collectors.toList());
//        tablesAvalible.add(0, "請選取");
        if (!etDate.getText().toString().isEmpty()) {
            try {
                bkDate = simpleDateFormat.parse(etDate.getText().toString());
            } catch (ParseException e) {
                Log.e(TAG, e.toString());
            }
        }
        if (spTime.getSelectedItemPosition() != 0 && etDate != null) {
//            Log.d(TAG, simpleDateFormat.format(bkDate) + " " + bkTime);
            List<Integer> tablesOrdered = bookings.stream().filter(v -> v.getBkTime().equals(bkTime)
                    && v.getBkDate().equals(bkDate)).map(v -> v.getTableId())
                    .collect(Collectors.toList());
            tablesOrdered.forEach(tablesAvalible::remove);
            List<String> selectTableList = tablesAvalible.stream().map(String::valueOf).collect(Collectors.toList());
            selectTableList.add(0, "請選取");
            return selectTableList;
        }
        List<String> tablesNoSelect = new ArrayList<>();
//        tablesNoSelect.add("請選取");
        return tablesNoSelect;
    }

    private List<Integer> getTableIds() {

        List<Integer> tableIds = new ArrayList<>();

        if (Common.networkConnected(activity)) {
            String url = Url.URL + "/TableServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            getTableTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = getTableTask.execute().get();
                Type listType = new TypeToken<List<Table>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                List<Table> tables = gson.fromJson(jsonIn, listType);
                tableIds = tables.stream().map(v -> v.getTableId()).collect(Collectors.toList());
            } catch (Exception e) {
                Log.e(TAG, toString());
            }
        } else {
            Common.showToast(getActivity(), R.string.textNoNetWork);
        }
//        List<String> tableIds = new ArrayList<>();
//        for (Table table : tables) {
//            tableIds.add(table.getTableId());
//        }
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
                Type listType = new TypeToken<List<Booking>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                bookings = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, toString());
            }
        } else {
            Common.showToast(getActivity(), R.string.textNoNetWork);
        }
        return bookings;
    }

}
