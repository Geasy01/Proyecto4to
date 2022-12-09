package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Modelos.DataUser;
import com.example.proyecto4to.Modelos.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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