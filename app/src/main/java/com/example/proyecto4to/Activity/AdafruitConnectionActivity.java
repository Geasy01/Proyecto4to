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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Modelos.DataUser;
import com.example.proyecto4to.Modelos.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdafruitConnectionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String USER_PREFERENCES = "userPreferences";
    private static final String TOKEN_KEY = "token";

    private RequestQueue nQueue;
    Button btnAdafruitConnection;
    TextView txtSiguiente;
    String url = "https://accounts.adafruit.com/users/sign_in";
    String adafruit_username;

    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adafruit);

        btnAdafruitConnection = (Button) findViewById(R.id.btnAdafruitConnection);
        txtSiguiente = (TextView) findViewById(R.id.txtSiguiente);
        btnAdafruitConnection.setOnClickListener(this);
        txtSiguiente.setOnClickListener(this);

        nQueue = SingletonRequest.getInstance(AdafruitConnectionActivity.this).getRequestQueue();
        userPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();
        token = userPreferences.getString(TOKEN_KEY, null);

        getDataUser();
        if(adafruit_username != null)
        {
            startActivity(new Intent(this, MisCarritosActivity.class));
        }
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

    public void getDataUser() {
        String url = "https://cleanbotapi.live/api/v1/user";

        JsonObjectRequest getUserData = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                DataUser dataUser = gson.fromJson(response.toString(), DataUser.class);
                adafruit_username = dataUser.getAdafruit_username();
                Toast.makeText(getApplicationContext(), "" + dataUser.getName(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("bearerToken", ""+token);
                return headers;
            }
        };

        nQueue.add(getUserData);
    }
}