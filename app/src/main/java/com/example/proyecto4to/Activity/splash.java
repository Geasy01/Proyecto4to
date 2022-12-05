package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.proyecto4to.Activity.LoginActivity;
import com.example.proyecto4to.R;

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
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }.start();
    }
}