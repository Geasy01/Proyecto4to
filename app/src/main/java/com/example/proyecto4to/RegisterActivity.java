package com.example.proyecto4to;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Modelos.Register;
import com.example.proyecto4to.Modelos.SingletonRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private RequestQueue nQueue;
    EditText inputUsername, ap_paterno, ap_materno, inputEmail, inputPassword, inputConfirmPassword, inputPhone;
    Button btnRegister;
    TextView alreadyHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUsername = (EditText) findViewById(R.id.inputUsername);
        ap_paterno = (EditText) findViewById(R.id.ap_paterno);
        ap_materno = (EditText) findViewById(R.id.ap_materno);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);
        inputPhone = (EditText) findViewById(R.id.inputPhone);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        alreadyHaveAccount = (TextView) findViewById(R.id.alreadyHaveAccount);
        nQueue = SingletonRequest.getInstance(RegisterActivity.this).getRequestQueue();

        btnRegister.setOnClickListener(this);
        alreadyHaveAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.alreadyHaveAccount)
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));


        if(view.getId() == R.id.btnRegister)
            if(inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                registerUser();
                startActivity(new Intent(RegisterActivity.this,VerificarCuentaActivity.class));
            }

        else {

            }
    }

    public void registerUser()
    {
        String url="https://cleanbotapi.live/api/v1/register";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("name", ""+inputUsername.getText());
            jsonBody.put("ap_paterno", ""+ap_paterno.getText());
            jsonBody.put("ap_materno", ""+ap_materno.getText());
            jsonBody.put("email", ""+ap_paterno.getText());
            jsonBody.put("phone_number", ""+inputPhone.getText());
            jsonBody.put("password", ""+inputPassword.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest registerUser = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                final Register register = gson.fromJson(response.toString(), Register.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Main")
                        .setMessage(""+ register.getMessage()+ "" + register.getError())
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        nQueue.add(registerUser);
    }
}
