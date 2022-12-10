package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Modelos.Register;
import com.example.proyecto4to.Otros.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String USER_PREFERENCES = "userPreferences";
    private static final String NAME_KEY = "nombre";
    private static final String AP_KEY = "apaterno";
    private static final String AM_KEY = "amaterno";
    private static final String EMAIL_KEY = "email";
    private static final String PHONE_KEY = "phone";

    private RequestQueue nQueue;
    EditText inputUsername, inputApaterno, inputAmaterno, inputEmail, inputPassword, inputConfirmPassword, inputPhone;
    Button btnRegister;
    TextView alreadyHaveAccount;
    String signedUrl;
    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputApaterno = (EditText) findViewById(R.id.inputApaterno);
        inputAmaterno = (EditText) findViewById(R.id.inputApmaterno);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);
        inputPhone = (EditText) findViewById(R.id.inputPhone);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        alreadyHaveAccount = (TextView) findViewById(R.id.alreadyHaveAccount);
        nQueue = SingletonRequest.getInstance(RegisterActivity.this).getRequestQueue();

        btnRegister.setOnClickListener(this);
        alreadyHaveAccount.setOnClickListener(this);

        userPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();
    }

    public void registerUser() {
        String url = "https://cleanbotapi.live/api/v1/register";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("name", "" + inputUsername.getText());
            jsonBody.put("ap_paterno", "" + inputApaterno.getText());
            jsonBody.put("ap_materno", "" + inputAmaterno.getText());
            jsonBody.put("email", "" + inputEmail.getText());
            jsonBody.put("phone_number", "" + inputPhone.getText());
            jsonBody.put("password", "" + inputPassword.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest registerUser = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                final Register register = gson.fromJson(response.toString(), Register.class);
                signedUrl = register.getUrl();

                if(register.getStatus() == 200) {
                    setUserPreferences();
                    Toast.makeText(getApplicationContext(), "" + register.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, VerificarCuentaActivity.class));

                    Intent sendSigned = new Intent(RegisterActivity.this, VerificarCuentaActivity.class);
                    sendSigned.putExtra("url", signedUrl);
                    startActivity(sendSigned);
                    finish();
                }

                else {
                    Toast.makeText(getApplicationContext(), "Ocurrió un error al registrarse. Por favor inténtelo en unos momentos.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        nQueue.add(registerUser);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.alreadyHaveAccount)
            //startActivity(new Intent(this, LoginActivity.class));
            startActivity(new Intent(this, MisCarritosActivity.class));

        if (view.getId() == R.id.btnRegister) {
            if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
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

            } else if (inputPassword.getText().toString().trim().isEmpty()) {
                inputPassword.setError("El campo contraseña es obligatorio.");
            } else if(inputPassword.getText().toString().trim().length() > 30) {
                inputPassword.setError("El campo contraseña debe tener un máximo de 30 caracteres.");
            } else if(inputPassword.getText().toString().trim().length() < 8) {
                inputPassword.setError("El campo contraseña debe tener un mínimo de 8 caracteres.");
            } else if (inputUsername.getText().toString().trim().isEmpty()) {
                inputUsername.setError("El campo nombre es obligatorio.");
            } else if (inputUsername.getText().toString().trim().length() > 25) {
                inputUsername.setError("El campo nombre debe tener un máximo de 25 caracteres.");
            } else if (inputApaterno.getText().toString().trim().isEmpty()) {
                inputApaterno.setError("El campo apellido paterno es obligatorio.");
            } else if(inputApaterno.getText().toString().trim().length() > 25) {
                inputApaterno.setError("El campo apellido paterno debe tener un máximo de 25 caracteres.");
            } else if (inputAmaterno.getText().toString().trim().isEmpty()) {
                inputAmaterno.setError("El campo apellido materno es obligatorio.");
            } else if(inputAmaterno.getText().toString().trim().length() > 20) {
                inputAmaterno.setError("El campo apellido materno debe tener un máximo de 25 caracteres.");
            } else if (inputPhone.getText().toString().trim().isEmpty()) {
                inputPhone.setError("El campo teléfono es obligatorio.");
            } else if(inputPhone.getText().toString().trim().length() > 10) {
                inputPhone.setError("El campo teléfono debe tener un máximo de 10 caracteres.");
            } else if(inputPhone.getText().toString().trim().length() < 10) {
                inputPhone.setError("El campo teléfono debe tener un mínimo de 10 caracteres.");
            } else if (inputEmail.getText().toString().trim().isEmpty()) {
                inputEmail.setError("El campo correo electrónico es obligatorio.");
            } else if (inputEmail.getText().toString().trim().length() > 70) {
                inputEmail.setError("El campo correo electrónico debe tener un máximo de 70 caracteres.");
            } else {
                    registerUser();
            }
        }
    }

    public void setUserPreferences() {
        userEditor.putString(NAME_KEY, inputUsername.getText().toString());
        userEditor.putString(AP_KEY, inputApaterno.getText().toString().trim());
        userEditor.putString(AM_KEY,inputAmaterno.getText().toString().trim());
        userEditor.putString(EMAIL_KEY, inputEmail.getText().toString().trim());
        userEditor.putString(PHONE_KEY, inputPhone.getText().toString().trim());
        userEditor.commit();
    }
}