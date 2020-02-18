package com.example.ezeats.game;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.ezeats.R;

import java.util.HashMap;
import java.util.Random;


public class GamePlayFragment extends Fragment {
        private final static  String TAG = "TAG_GamePlayFragment";
        private Activity activity;
        private final int cardColor = Color.argb(255,235,203,95);
        private final int[] imageId = {R.drawable.banana,R.drawable.buger,R.drawable.drink,R.drawable.pizza};
        ImageView ivHead;
        Button btCode;
        private ImageButton cards[] = new ImageButton[8];
        private ImageButton pressCard;
        private ImageButton tempPhoto;
        private TextView tvTimer,tvUserName2;

        private HashMap<Integer,Integer> location = new HashMap<>();

        private int[] randomArray;

        private boolean pressed;
        private boolean timerSleeping;

        private long startTime;
        private long startTime2;
        private int timeTaken;
        private int cardsDone;

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

        public void OnCreatHelper () {
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
        builder1.setMessage("成功");
        builder1.setCancelable(false);

        builder1.setPositiveButton("再玩一次", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                OnCreatHelper();
            }
        });

        builder1.setNegativeButton("離開", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

        AlertDialog alert1 = builder1.create();
        alert1.show();
    }


        public static int[] getRandomNumbers(){
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
}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_play,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
                    for (ImageButton Cards: cards) {
                        Cards.setClickable(false);
                    }
                    tempPhoto = (ImageButton)v;
                    if (tempPhoto.getColorFilter() != null) {
                        if (pressed) {
                            tempPhoto.clearColorFilter();
                            if (location.get(tempPhoto.getId()) == location.get(pressCard.getId())) {
                                if (cardsDone == 4) {
                                    openDialog();
                                }
                            } else {
                                tempPhoto.clearColorFilter();;
                                startTime2 = System.currentTimeMillis();
                                timerSleeping = false;
                            }
                            pressed = false;
                            if (timerSleeping) {
                                pressCard = null;
                            }
                        } else {
                            tempPhoto.clearColorFilter();
                            pressCard = tempPhoto;
                            pressed = true;
                        }
                    }
                    if (timerSleeping) {
                        for (ImageButton Card: cards) {
                            Card.setClickable(true);
                        }
                    }
                }
            });
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable,0);
        }
    }


}





