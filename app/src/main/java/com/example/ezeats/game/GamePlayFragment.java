package com.example.ezeats.game;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;

import java.util.HashMap;
import java.util.Random;


public class GamePlayFragment extends Fragment {
    private final static String TAG = "TAG_GamePlayFragment";
    private final static String PREFERENCES_NAME = "preferences";
    private Activity activity;
    private final int cardColor = Color.argb(255, 235, 203, 95);
    private final int[] imageId = {R.drawable.banana, R.drawable.buger, R.drawable.drink, R.drawable.pizza};
    private ImageButton cards[] = new ImageButton[8];
    private ImageButton pressCard;
    private ImageButton tempPhoto;
    private TextView tvTimer, tvTitle;
    private HashMap<Integer, Integer> location = new HashMap<>();
    private int[] randomArray;
    private boolean pressed;
    private boolean timerSleeping;
    private boolean timesOut;
    private long startTime;
    private long startTime2;
    private int timeTaken;
    private int cardsDone;
    private NavController navController;

    private final Handler timerHandler = new Handler();

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            tvTimer.setText(String.format("%d:%02d", minutes, seconds));

            if (!timerSleeping) {
                long millis2 = System.currentTimeMillis() - startTime2;
                int seconds2 = (int) (millis2 / 1000);
                seconds2 = seconds2 % 60;
                timeTaken = seconds2;

                if (timeTaken >= 1) {
                    timerSleeping = true;
                    timeTaken = 0;

                    for (ImageButton Cards : cards) {
                        Cards.setClickable(true);
                    }
                    pressCard.setColorFilter(cardColor);
                    tempPhoto.setColorFilter(cardColor);
                }
            }
            timerHandler.postDelayed(this, 500);
        }
    };

    public void OnCreatHelper() {
        cardsDone = 0;
        startTime = System.currentTimeMillis();
        timerSleeping = true;
        pressCard = null;
        tempPhoto = null;
        pressed = false;
        timeTaken = 0;
        randomArray = getRandomNumbers();

        for (int i = 0; i < 8; i++) {
            cards[randomArray[i]].setImageResource(imageId[i / 2]);
            location.put(cards[randomArray[i]].getId(), i / 2);
            cards[randomArray[i]].setColorFilter(cardColor);
            cards[randomArray[i]].setClickable(true);
        }
    }

    private void openDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("獲得優惠碼" + "\n" + "\n" + "AA123b");
        builder1.setCancelable(false);

        builder1.setNegativeButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //跳出程式方法
//                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                homeIntent.addCategory(Intent.CATEGORY_HOME);
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(homeIntent);
                SharedPreferences pref = activity.getSharedPreferences(Common.NUMBER, Context.MODE_PRIVATE);
                String number = "AA123b";
                pref.edit()
                        .putString("number", number)
                        .apply();
                Common.showToast(activity, R.string.textsave);
                navController.popBackStack(R.id.linkFragment, false);
            }
        });

        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

//    private void timesOutDialog(){
//            AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
//            builder2.setMessage("挑戰失敗" + "\n" + "\n" + "再玩一次嗎？");
//            builder2.setCancelable(false);
//
//        builder2.setPositiveButton("再玩一次", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                OnCreatHelper();
//            }
//        });
//
//        builder2.setNegativeButton("離開", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                navController.popBackStack(R.id.linkFragment,false);
//            }
//        });
//
//        AlertDialog alert2 = builder2.create();
//        alert2.show();
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    builder2.show();
//                                }
//                            },6000
//        );
//    }


    public static int[] getRandomNumbers() {
        Random random = new Random();
        int[] ar = {0, 1, 2, 3, 4, 5, 6, 7};
        for (int i = 7; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textLianliankan);
        tvTimer = view.findViewById(R.id.tvTimer);
        cards[0] = view.findViewById(R.id.ibMatch1);
        cards[1] = view.findViewById(R.id.ibMatch2);
        cards[2] = view.findViewById(R.id.ibMatch3);
        cards[3] = view.findViewById(R.id.ibMatch4);
        cards[4] = view.findViewById(R.id.ibMatch5);
        cards[5] = view.findViewById(R.id.ibMatch6);
        cards[6] = view.findViewById(R.id.ibMatch7);
        cards[7] = view.findViewById(R.id.ibMatch8);

        OnCreatHelper();

        for (int i = 0; i < 8; i++) {
            cards[randomArray[i]].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (ImageButton Cards : cards) {
                        Cards.setClickable(false);
                    }
                    tempPhoto = (ImageButton) v;
                    if (tempPhoto.getColorFilter() != null) {
                        if (pressed) {
                            tempPhoto.clearColorFilter();
                            if (location.get(tempPhoto.getId()) == location.get(pressCard.getId())) {
                                cardsDone++;
                                if (cardsDone == 4) {
                                    openDialog();
                                }
                            } else {
                                tempPhoto.clearColorFilter();
                                ;
                                startTime2 = System.currentTimeMillis();
                                timerSleeping = false;
                            }
                            pressed = false;
                            if (timerSleeping)
                                pressCard = null;

                        } else {
                            tempPhoto.clearColorFilter();
                            pressCard = tempPhoto;
                            pressed = true;
                        }
                    }
                    if (timerSleeping)
                        for (ImageButton Card : cards) {
                            Card.setClickable(true);
                        }
                }
            });
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
        }

    }
}





