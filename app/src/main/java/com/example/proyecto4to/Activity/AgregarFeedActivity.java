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
import com.example.proyecto4to.Modelos.FeedAdd;
import com.example.proyecto4to.Modelos.SendAdafruit;
import com.example.proyecto4to.Otros.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AgregarFeedActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String USER_PREFERENCES = "userPreferences";
    private static final String TOKEN_KEY = "token";

    private RequestQueue nQueue;
    Button btnAddNewFeed;
    View vitBack;
    EditText inputNombreFeed, editTextTextMultiLine;
    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_feed);

        btnAddNewFeed = (Button) findViewById(R.id.btnAddNewFeed);
        vitBack = (View) findViewById(R.id.vitBack);
        inputNombreFeed = (EditText) findViewById(R.id.inputNombreFeed);
        editTextTextMultiLine = (EditText) findViewById(R.id.editTextTextMultiLine);
        nQueue = SingletonRequest.getInstance(AgregarFeedActivity.this).getRequestQueue();

        btnAddNewFeed.setOnClickListener(this);
        vitBack.setOnClickListener(this);

        userPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();
        token = userPreferences.getString(TOKEN_KEY, null);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnAddNewFeed){
            addFeed();
        }

        if(v.getId() == R.id.viBack){
            startActivity(new Intent(AgregarFeedActivity.this, MainActivity.class));
        }
    }

    public void addFeed() {
        String url = "https://cleanbotapi.live/api/v1/add/feed";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("name", ""+inputNombreFeed.getText());
            jsonBody.put("description", ""+editTextTextMultiLine.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest addFeed = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                FeedAdd feedAdd = gson.fromJson(response.toString(), FeedAdd.class);

                if (feedAdd.getStatus() == 200) {
                    Toast.makeText(getApplicationContext(), "" + feedAdd.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AgregarFeedActivity.this, MainActivity.class));
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
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " +token);
                return headers;
            }
        };

        nQueue.add(addFeed);
    }
}