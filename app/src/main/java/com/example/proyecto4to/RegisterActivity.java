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
import com.example.proyecto4to.Modelos.ErrorRegister;
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
    Register register;
    ErrorRegister errorRegister;


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
                final Gson nGson = new Gson();
                register = gson.fromJson(response.toString(), Register.class);
                errorRegister = nGson.fromJson(response.toString(), ErrorRegister.class);


                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Main")
                        .setMessage(""+register.getError())
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
                Log.i("errorPeticion", error.toString());
            }
        });

        nQueue.add(registerUser);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.alreadyHaveAccount)
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));


        if(view.getId() == R.id.btnRegister)
            if(! inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Password incorrecto")
                        .setMessage("Las contraseñas no coinciden. Intenta nuevamente")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        else if(inputPassword.getText().toString().trim().isEmpty() || inputPassword.getText().toString().trim().length() > 30 || inputPassword.getText().toString().trim().length() < 8) {
            inputPassword.setError(""+errorRegister.getPassword());
        }

        else if(inputUsername.getText().toString().trim().isEmpty() || inputUsername.getText().toString().trim().length()>20) {
            inputUsername.setError(""+errorRegister.getName());
        }

        else if(ap_paterno.getText().toString().trim().isEmpty() || ap_paterno.getText().toString().trim().length()>20) {
            ap_paterno.setError(""+errorRegister.getAp_paterno());
        }

        else if(ap_materno.getText().toString().trim().isEmpty() || ap_materno.getText().toString().trim().length()>20) {
            ap_materno.setError(""+errorRegister.getAp_materno());
        }

        else if(inputPhone.getText().toString().trim().isEmpty() || inputPhone.getText().toString().trim().length()>10) {
            inputPhone.setError(""+errorRegister.getPhone_number());
        }

        else if(inputEmail.getText().toString().trim().isEmpty() || inputEmail.getText().toString().trim().length()>70) {
            inputEmail.setError(""+errorRegister.getEmail());
        }

        else {
            registerUser();
            startActivity(new Intent(RegisterActivity.this,VerificarCuentaActivity.class));
        }
    }
}
