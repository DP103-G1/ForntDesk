package com.example.ezeats.order;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Table;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class BillFragment extends Fragment {
    private final static String TAG = "TAG_BillFragment";
    private TextView tvTitle;
    private Activity activity;
    private EditText edName, edNumber, edLast;
    private Button btBillCheck;
    private Spinner spMou, spDay;
    private int mem_id;
    private int total;
    private Table table;

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
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navigation = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            total = bundle.getInt("menudetail", 0);
            Log.d(TAG, String.valueOf(total));
        }

        edName = view.findViewById(R.id.edName);
        edNumber = view.findViewById(R.id.edNumber);
        edLast = view.findViewById(R.id.edLast);
        btBillCheck = view.findViewById(R.id.btBillCheck);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textBill);

        spMou = view.findViewById(R.id.spMou);
        String[] mounthArray = getResources().getStringArray(R.array.textMounth);
        ArrayAdapter<String> mounthAdapter = new ArrayAdapter<>(activity, R.layout.myspinner, mounthArray);
        mounthAdapter.setDropDownViewResource(R.layout.myspinner);
        spMou.setAdapter(mounthAdapter);
        spMou.setSelection(0, true);
        spMou.setOnItemSelectedListener(listener);

        spDay = view.findViewById(R.id.spDay);
        String[] dayArray = getResources().getStringArray(R.array.textYear);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(activity, R.layout.myspinner, dayArray);
        dayAdapter.setDropDownViewResource(R.layout.myspinner);
        spDay.setAdapter(dayAdapter);
        spDay.setSelection(0, true);
        spDay.setOnItemSelectedListener(listener);


        btBillCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString().trim();
                if (name.length() == 0) {
                    edName.setError(getString(R.string.textNoName));
                    return;
                }
                String number = edNumber.getText().toString().trim();
                if (number.length() != 16) {
                    edNumber.setError(getString(R.string.textNoNumber));
                    return;
                }


                String last = edLast.getText().toString().trim();
                if (last.length() != 3) {
                    edLast.setError(getString(R.string.textNoLast));
                    return;
                }

                int cardlast = Integer.parseInt(edLast.getText().toString().trim());

                String[] monthArray = getResources().getStringArray(R.array.textMounth);
                String month = String.valueOf(monthArray[spMou.getSelectedItemPosition()]);
                if (month.equals("月")) {
                    Common.showToast(getActivity(), R.string.textMonthDay);
                    return;
                }

                String[] dayArray = getResources().getStringArray(R.array.textYear);
                String day = String.valueOf(dayArray[spDay.getSelectedItemPosition()]);
                if (day.equals("日")) {
                    Common.showToast(getActivity(), R.string.textMonthDay);
                    return;
                }

                String date = month + "/" + day;
                System.out.println(date);

                if (Common.networkConnected(activity)) {
                    String url = Url.URL + "/CardServlet";
                    Card card = new Card(mem_id, name, number, date, cardlast);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "add");
                    jsonObject.addProperty("card", gson.toJson(card));
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    } catch (Exception e) {
                        Log.e(TAG, toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.textNoBill);
                    } else {
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.textBillOK)
                                .setMessage("原金額:" + " " + (total/(0.9)))
                                .setMessage("折扣九折" + " " + (total*(0.1)))
                                .setMessage("總金額:" + " " + total)
                                .setPositiveButton(R.string.textYes, (dialog, which) -> navigation.navigate(R.id.action_billFragment_to_homeFragment))
                                .show();
                    }
                } else {
                    Common.showToast(getActivity(), R.string.textNoNetwork);
                }
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
}
