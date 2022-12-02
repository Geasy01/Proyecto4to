package com.example.proyecto4to;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long l) {

            }

            public void onFinish() {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }.start();
    }
}