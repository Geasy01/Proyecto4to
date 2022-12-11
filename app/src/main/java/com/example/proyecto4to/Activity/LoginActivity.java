package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.proyecto4to.Modelos.Login;
import com.example.proyecto4to.Otros.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String USER_PREFERENCES = "userPreferences";
    private static final String SESSION_KEY = "session";
    private static final String TOKEN_KEY = "token";

    private RequestQueue nQueue;
    TextView inputMail, inputPass;
    Button btnLogin;
    TextView textViewSignUp;
    CheckBox cbxRememberMe;
    String token = null, adafruit_username = null;
    DataUser dataUser;

    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputMail = (TextView) findViewById(R.id.inputMail);
        inputPass = (TextView) findViewById(R.id.inputPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        cbxRememberMe = (CheckBox) findViewById(R.id.cbxRememberMe);
        nQueue = SingletonRequest.getInstance(LoginActivity.this).getRequestQueue();
        userPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();

        btnLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

        if(checkSession())
        {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void login()
    {
        String url = "https://cleanbotapi.live/api/v1/login";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", "" + inputMail.getText());
            jsonBody.put("password", "" + inputPass.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest login = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                final Login login = gson.fromJson(response.toString(), Login.class);
                token = login.getToken();

                if (login.getStatus() == 200) {
                    //getUserData();
                    Toast.makeText(getApplicationContext(), "" + login.getMessage(), Toast.LENGTH_SHORT).show();

                    //if(adafruit_username == null)
                    //{
                        startActivity(new Intent(LoginActivity.this, MisCarritosActivity.class));
                    //}

                    //else
                    //{
                        //startActivity(new Intent(LoginActivity.this, MisCarritosActivity.class));
                    //}

                    setUserPreferences();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        nQueue.add(login);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnLogin) {
            if (inputPass.getText().toString().trim().isEmpty()) {
                inputPass.setError("El campo contraseña es obligatorio.");
            } else if (inputMail.getText().toString().trim().isEmpty()) {
                inputMail.setError("El campo correo electrónico es obligatorio");
            } else {
                saveSession(cbxRememberMe.isChecked());
                login();
            }
        }

        if(view.getId() == R.id.textViewSignUp)
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public boolean checkSession() {
        return this.userPreferences.getBoolean(SESSION_KEY, false);
    }

    public void saveSession(boolean bear) {
        userEditor.putBoolean(SESSION_KEY, bear);
        userEditor.apply();
    }

    public void setUserPreferences() {
        userEditor.putString(TOKEN_KEY, ""+token);
        userEditor.commit();
    }

    public void getUserData() {
        String con = "https://cleanbotapi.live/api/v1/user";

        JsonArrayRequest getUserData = new JsonArrayRequest(Request.Method.GET, con, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final Gson gson = new Gson();
                dataUser = gson.fromJson(response.toString(), DataUser.class);
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
               headers.put("Authorization", "Bearer " + token);
               return headers;
           }
        };

        nQueue.add(getUserData);
    }
}
