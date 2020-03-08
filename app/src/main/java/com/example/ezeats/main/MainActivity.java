package com.example.ezeats.main;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ezeats.R;
import com.example.ezeats.member.Member;
import com.example.ezeats.task.CommonTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG_MainActivity";
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private ImageButton ibBill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bv);
        ibBill = findViewById(R.id.ibBill);
        ibBill.setOnClickListener(v -> {
            navController.navigate(R.id.menuDetailFragment);
        });
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    @Override
    public void onResume() {
        super.onResume();
        int memId;
        if ((memId = Common.getMemId(this)) == 0) {
            ibBill.setVisibility(View.GONE);
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.normal_menu);
            navController.navigate(R.id.loginFragment);
            return;
        }
        Member member = getMember(memId);
        if (member.getState() == 0) {
            ibBill.setVisibility(View.GONE);
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.normal_menu);
        } else {
            ibBill.setVisibility(View.VISIBLE);
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.seat_menu);
        }
    }

    private Member getMember(int memId) {
        Member member = null;
        if (Common.networkConnected(this)) {
            String url = Url.URL + "/MembersServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findByMemberId");
            jsonObject.addProperty("member_Id", memId);
            CommonTask getMemberTask = new CommonTask(url, jsonObject.toString());
            try {
                String result = getMemberTask.execute().get();
                member = new Gson().fromJson(result, Member.class);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(this, R.string.textNoNetwork);
        }
        return member;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, navController);
        return super.onOptionsItemSelected(item);
    }
}


