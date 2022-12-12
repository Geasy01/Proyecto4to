package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Modelos.UpdateAdafruit;
import com.example.proyecto4to.Modelos.UpdateUser;
import com.example.proyecto4to.Otros.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActualizarCredencialesAdafruitActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String USER_PREFERENCES = "userPreferences";
    private static final String IO_KEY = "iokey";
    private static final String IO_USERNAME_KEY = "iousername";
    private static final String TOKEN_KEY = "token";

    EditText inputIO, inputUserAda;
    private RequestQueue nQueue;
    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;
    String token;
    View viBack;
    Button btnLoginAda2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_credenciales_adafruit);

        inputIO = (EditText) findViewById(R.id.inputIO);
        inputUserAda = (EditText) findViewById(R.id.inputUserAda);
        viBack = (View) findViewById(R.id.viBack);
        btnLoginAda2 = (Button) findViewById(R.id.btnLoginAda2);

        viBack.setOnClickListener(this);
        btnLoginAda2.setOnClickListener(this);

        userPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();
        nQueue = SingletonRequest.getInstance(ActualizarCredencialesAdafruitActivity.this).getRequestQueue();
        token = userPreferences.getString(TOKEN_KEY, null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.viBack) {
            startActivity(new Intent(this, MainActivity.class));
        }

        if(v.getId() == R.id.btnLoginAda2) {
            updateAdafruit();
        }
    }

    public void updateAdafruit() {
        String url = "https://cleanbotapi.live/api/v1/update/adafruit";

        JSONObject jsonBody = new JSONObject();

        try{
        jsonBody.put("adafruit_username", "" + inputUserAda.getText());
        jsonBody.put("io_key", "" + inputIO.getText());

        } catch(
        JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest updateAdafruit = new JsonObjectRequest(Request.Method.PUT, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                final UpdateAdafruit updateAdafruit = gson.fromJson(response.toString(), UpdateAdafruit.class);

                if (updateAdafruit.getStatus() == 200) {
                    setUserPreferences();
                    Toast.makeText(getApplicationContext(), "" + updateAdafruit.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ActualizarCredencialesAdafruitActivity.this, MainActivity.class));
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

        nQueue.add(updateAdafruit);
    }

    public void setUserPreferences() {
            userEditor.putString(IO_USERNAME_KEY, inputUserAda.getText().toString());
            userEditor.putString(IO_KEY, inputIO.getText().toString());
            userEditor.commit();
    }
}