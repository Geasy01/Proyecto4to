package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.proyecto4to.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private RequestQueue nQueue;
    TextView inputMail, inputPass;
    Button btnLogin;
    TextView textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputMail = (TextView) findViewById(R.id.inputMail);
        inputPass = (TextView) findViewById(R.id.inputPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        btnLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnLogin)
        {

        }

        if(view.getId() == R.id.textViewSignUp)
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
