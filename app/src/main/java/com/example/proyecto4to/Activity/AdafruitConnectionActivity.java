package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.proyecto4to.R;

public class AdafruitConnectionActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdafruitConnection;
    TextView txtSiguiente;
    String url = "https://accounts.adafruit.com/users/sign_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adafruit);

        btnAdafruitConnection = (Button) findViewById(R.id.btnAdafruitConnection);
        txtSiguiente = (TextView) findViewById(R.id.txtSiguiente);
        btnAdafruitConnection.setOnClickListener(this);
        txtSiguiente.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAdafruitConnection) {
            Uri adafruitLink = Uri.parse(url);
            Intent abrirAdafruit = new Intent(Intent.ACTION_VIEW, adafruitLink);
            startActivity(abrirAdafruit);
        }

        if(view.getId() == R.id.txtSiguiente) {
            startActivity(new Intent(this, AdafruitLoginActivity.class));
        }
    }
}