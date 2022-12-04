package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.proyecto4to.R;

public class LoginActivity extends AppCompatActivity {

    private RequestQueue nQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btn=findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
