package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Modelos.SendAdafruit;
import com.example.proyecto4to.Modelos.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdafruitLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String USER_PREFERENCES = "userPreferences";
    private static final String IO_KEY = "iokey";
    private static final String IO_USERNAME_KEY = "iousername";
    private static final String TOKEN_KEY = "token";

    private RequestQueue nQueue;
    Button btnLoginAda;
    EditText inputUsernameAda, inputKeyAda;
    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adafruit_login);

        btnLoginAda = (Button) findViewById(R.id.btnLoginAda);
        inputUsernameAda = (EditText) findViewById(R.id.inputUsernameAda);
        inputKeyAda = (EditText) findViewById(R.id.inputKeyAda);
        nQueue = SingletonRequest.getInstance(AdafruitLoginActivity.this).getRequestQueue();

        btnLoginAda.setOnClickListener(this);

        userPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();
        token = userPreferences.getString(TOKEN_KEY, null);
    }

    public void sendAdafruitData()
    {
        String url = "https://cleanbotapi.live/api/v1/adafruit/keys";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("adafruit_username", ""+inputUsernameAda.getText());
            jsonBody.put("io_key", ""+inputKeyAda.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest sendAdafruiitData = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                SendAdafruit sendAdafruit = gson.fromJson(response.toString(), SendAdafruit.class);

                if (sendAdafruit.getStatus() == 200) {
                    setUserPreferences();
                    Toast.makeText(getApplicationContext(), "" + sendAdafruit.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdafruitLoginActivity.this, MisCarritosActivity.class));
                    finish();
                }
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
                headers.put("auth_token", "5|pwupvLtza6O4eIs6yOmY8Ogc9hiskOECXlA9jqBU");
                return headers;
            }
        };

        nQueue.add(sendAdafruiitData);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnLoginAda) {
            if (inputUsernameAda.getText().toString().trim().isEmpty()) {
                inputUsernameAda.setError("El campo contraseña es obligatorio.");
            } else if (inputKeyAda.getText().toString().trim().isEmpty()) {
                inputKeyAda.setError("El campo correo electrónico es obligatorio");
            } else {
                sendAdafruitData();
            }
        }
    }

    public void setUserPreferences() {
        userEditor.putString(IO_USERNAME_KEY, inputUsernameAda.getText().toString());
        userEditor.putString(IO_KEY, inputKeyAda.getText().toString());
        userEditor.commit();
    }
}