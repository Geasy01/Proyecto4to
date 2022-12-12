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
import com.example.proyecto4to.Modelos.Register;
import com.example.proyecto4to.Modelos.UpdateUser;
import com.example.proyecto4to.Otros.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditPerfilActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String USER_PREFERENCES = "userPreferences";
    private static final String NAME_KEY = "nombre";
    private static final String AP_KEY = "apaterno";
    private static final String AM_KEY = "amaterno";
    private static final String TOKEN_KEY = "token";

    private RequestQueue nQueue;
    EditText etNombre, etApellido, etApellido2;
    Button btnGuardar, btnCancelar;
    View vBack;
    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellido = (EditText) findViewById(R.id.etApellido);
        etApellido2 = (EditText) findViewById(R.id.etApellido2);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        vBack = (View) findViewById(R.id.vBack);

        btnCancelar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        vBack.setOnClickListener(this);

        userPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();
        nQueue = SingletonRequest.getInstance(EditPerfilActivity.this).getRequestQueue();

        String nombre = userPreferences.getString(NAME_KEY, null);
        String ap_paterno = userPreferences.getString(AP_KEY, null);
        String ap_materno = userPreferences.getString(AM_KEY, null);
        token = userPreferences.getString(TOKEN_KEY, null);

        etNombre.setText(nombre);
        etApellido.setText(ap_paterno);
        etApellido2.setText(ap_materno);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnGuardar)
        {
            if (etNombre.getText().toString().trim().isEmpty()) {
                etNombre.setError("El campo nombre es obligatorio.");
            } else if (etNombre.getText().toString().trim().length() > 25) {
                etNombre.setError("El campo nombre debe tener un máximo de 25 caracteres.");
            } else if (etApellido.getText().toString().trim().isEmpty()) {
                etApellido.setError("El campo apellido paterno es obligatorio.");
            } else if(etApellido.getText().toString().trim().length() > 25) {
                etApellido.setError("El campo apellido paterno debe tener un máximo de 25 caracteres.");
            } else if (etApellido2.getText().toString().trim().isEmpty()) {
                etApellido2.setError("El campo apellido materno es obligatorio.");
            } else if(etApellido2.getText().toString().trim().length() > 20) {
                etApellido2.setError("El campo apellido materno debe tener un máximo de 25 caracteres.");
            } else {
                updateUser();
            }
        }

        if(v.getId() == R.id.btnCancelar)
        {
            startActivity(new Intent(this, MainActivity.class));
        }

        if(v.getId() == R.id.vBack)
        {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void updateUser() {
        String url = "https://cleanbotapi.live/api/v1/update/user";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("name", "" + etNombre.getText());
            jsonBody.put("ap_paterno", "" + etApellido.getText());
            jsonBody.put("ap_materno", "" + etApellido2.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest updateUser = new JsonObjectRequest(Request.Method.PUT, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                final UpdateUser updateUser = gson.fromJson(response.toString(), UpdateUser.class);

                if(updateUser.getStatus() == 200) {
                    setUserPreferences();
                    Toast.makeText(getApplicationContext(), "" + updateUser.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditPerfilActivity.this, MainActivity.class));
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        nQueue.add(updateUser);
    }

    public void setUserPreferences() {
        userEditor.putString(NAME_KEY, etNombre.getText().toString());
        userEditor.putString(AP_KEY, etApellido.getText().toString().trim());
        userEditor.putString(AM_KEY,etApellido2.getText().toString().trim());
        userEditor.commit();
    }
}