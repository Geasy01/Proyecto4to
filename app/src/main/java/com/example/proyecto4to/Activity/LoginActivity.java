package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Modelos.Login;
import com.example.proyecto4to.Modelos.Register;
import com.example.proyecto4to.Modelos.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private RequestQueue nQueue;
    TextView inputMail, inputPass;
    Button btnLogin;
    TextView textViewSignUp;
    String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputMail = (TextView) findViewById(R.id.inputMail);
        inputPass = (TextView) findViewById(R.id.inputPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        nQueue = SingletonRequest.getInstance(LoginActivity.this).getRequestQueue();

        btnLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
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
                    Toast.makeText(getApplicationContext(), "" + login.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                login();
            }
        }

        if(view.getId() == R.id.textViewSignUp)
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
