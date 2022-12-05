package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Modelos.ActivateUrl;
import com.example.proyecto4to.Modelos.SignedUrl;
import com.example.proyecto4to.Modelos.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class VerificarCuentaActivity extends AppCompatActivity implements View.OnClickListener {
    EditText inputCode;
    Button btnSendCode;
    String urlUnsigned = null, newUrl = null;
    private RequestQueue nQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_cuenta);

        inputCode = (EditText) findViewById(R.id.inputCode);
        btnSendCode = (Button) findViewById(R.id.btnSendCode);

        btnSendCode.setOnClickListener(this);

        nQueue = SingletonRequest.getInstance(VerificarCuentaActivity.this).getRequestQueue();

        Bundle getSignedUrl = this.getIntent().getExtras();
        urlUnsigned = getSignedUrl.getString("url");

        getUrlSigned();
    }

    public void getUrlSigned() {
        final JsonObjectRequest getUrl = new JsonObjectRequest(Request.Method.GET, urlUnsigned, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                final ActivateUrl activateUrl = gson.fromJson(response.toString(), ActivateUrl.class);
                newUrl = activateUrl.getUrl();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        nQueue.add(getUrl);
    }

    public void sendCode() {
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("codigo", "" + inputCode.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest sendCode = new JsonObjectRequest(Request.Method.POST, newUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                final SignedUrl newSignedUrl = gson.fromJson(response.toString(), SignedUrl.class);

                if(newSignedUrl.getStatus() == 200) {
                    Toast.makeText(getApplicationContext(), "" + newSignedUrl.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(VerificarCuentaActivity.this, LoginActivity.class));
                    finish();
                }

                else {
                    Toast.makeText(getApplicationContext(), "El código es inválido. Por favor vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        nQueue.add(sendCode);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSendCode) {
            if(inputCode.getText().toString().trim().isEmpty()) {
                inputCode.setError("El campo código es obligatorio");
            } else {
                if(newUrl != null)
                sendCode();
            }
        }
    }
}