package com.example.myapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.TimeInterpolator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.sql.Time;
import java.util.Locale;
import java.util.Timer;

public class Timer2 extends AppCompatActivity {
    private static final long START_TIME=3000;
    private TextView CountDown;
    private Button StartPause;
    private  Button Reset,Next2;
    private CountDownTimer CountDownTimer;
    private  boolean TimerRunning;
    private long TimeLeft = START_TIME;
    private SlidrInterface slide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer2);
        slide= Slidr.attach(this);
        CountDown = findViewById(R.id.text_view_countdown);
        StartPause = findViewById(R.id.button_start_pause);
        Reset = findViewById(R.id.button_reset);
        Next2 = findViewById(R.id.Nextbtn2);

        setTitle("Level 2");

        StartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TimerRunning){
                    pauseTimer();
                }else{
                    startTimer();
                }
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDown();
    }

    private void startTimer(){

        CountDownTimer = new CountDownTimer(TimeLeft, 1000) {
            @Override

            public void onTick(long millisUntilFinished) {
                TimeLeft = millisUntilFinished;
                updateCountDown();
            }

            @Override
            public void onFinish() {
                TimerRunning = false;
                updateButtons();
            }

        }.start();


        TimerRunning=true;
        updateButtons();
    }

    private  void pauseTimer(){
        CountDownTimer.cancel();
        TimerRunning=false;
        updateButtons();
    }

    private void resetTimer(){
        TimeLeft=START_TIME;
        updateCountDown();
        updateButtons();

    }

    private void updateCountDown(){
        int minutes = (int) (TimeLeft/1000)/60;
        int seconds = (int) (TimeLeft/1000)%60;
        String mTimeleft =String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        CountDown.setText(mTimeleft);
    }


    private void updateButtons(){
        if(TimerRunning){
            Reset.setVisibility(View.INVISIBLE);
            StartPause.setText("pause");
            slide.lock();

        }else {
            StartPause.setText("Start");
            slide.unlock();
            if(TimeLeft< 1000){
                StartPause.setVisibility(View.INVISIBLE);
                Next2.setVisibility(View.VISIBLE);
            }else {
                StartPause.setVisibility(View.VISIBLE);
            }

            if(TimeLeft<START_TIME){
                Reset.setVisibility(View.VISIBLE);
            }else{
                Reset.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void Nextbtn2(View view) {
        startActivity(new Intent(getApplicationContext(),Timer3.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }

}
