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
import com.example.proyecto4to.Modelos.UpdateAdafruit;
import com.example.proyecto4to.Modelos.UpdatePassword;
import com.example.proyecto4to.Otros.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActualizarPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String USER_PREFERENCES = "userPreferences";
    private static final String TOKEN_KEY = "token";

    EditText inputActPass, inputNewPass, inputConfirmPass;
    View bBack;
    Button btnSavePass;

    private RequestQueue nQueue;
    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_password);

        inputActPass = (EditText) findViewById(R.id.inputActPass);
        inputNewPass = (EditText) findViewById(R.id.inputNewPass);
        inputConfirmPass = (EditText) findViewById(R.id.inputConfirmPass);
        bBack = (View) findViewById(R.id.bBack);
        btnSavePass = (Button) findViewById(R.id.btnSavePass);

        bBack.setOnClickListener(this);
        btnSavePass.setOnClickListener(this);

        userPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();
        nQueue = SingletonRequest.getInstance(ActualizarPasswordActivity.this).getRequestQueue();
        token = userPreferences.getString(TOKEN_KEY, null);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bBack){
            startActivity(new Intent(ActualizarPasswordActivity.this, MainActivity.class));
        }

        if(v.getId() == R.id.btnSavePass) {
            updatePassword();
        }
    }

    public void updatePassword() {
        String url = "https://cleanbotapi.live/api/v1/update/password";

        JSONObject jsonBody = new JSONObject();

        try{
            jsonBody.put("password_actual", "" + inputActPass.getText());
            jsonBody.put("password", "" + inputNewPass.getText());
            jsonBody.put("password_confirmation", "" + inputConfirmPass.getText());

        } catch(
                JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest updatePassword = new JsonObjectRequest(Request.Method.PUT, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                final UpdatePassword updatePassword = gson.fromJson(response.toString(), UpdatePassword.class);

                if (updatePassword.getStatus() == 200) {
                    Toast.makeText(getApplicationContext(), "" + updatePassword.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ActualizarPasswordActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Ocurrió un error al registrarse. Por favor inténtelo en unos momentos.", Toast.LENGTH_SHORT).show();
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
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        nQueue.add(updatePassword);
    }

}