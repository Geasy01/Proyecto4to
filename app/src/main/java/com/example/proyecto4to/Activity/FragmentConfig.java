package com.example.proyecto4to.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Modelos.Logout;
import com.example.proyecto4to.Otros.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentConfig#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConfig extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String USER_PREFERENCES = "userPreferences";
    private static final String SESSION_KEY = "session";
    private static final String TOKEN_KEY = "token";

    View vista;
    Button logout;
    TextView upKeyAdafruit, updatePassword;
    private RequestQueue nQueue;
    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;
    String token;

    public FragmentConfig() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConfig.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConfig newInstance(String param1, String param2) {
        FragmentConfig fragment = new FragmentConfig();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_config, container, false);
        logout = vista.findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        upKeyAdafruit = vista.findViewById(R.id.upKeyAdafruit);
        upKeyAdafruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(vista.getContext(), ActualizarCredencialesAdafruitActivity.class));
            }
        });

        nQueue = SingletonRequest.getInstance(vista.getContext()).getRequestQueue();
        userPreferences = vista.getContext().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();
        token = userPreferences.getString(TOKEN_KEY, null);
        return vista;
    }

    public void logout() {
        String url = "https://cleanbotapi.live/api/v1/logout";

        JsonObjectRequest logout = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                final Logout logout = gson.fromJson(response.toString(), Logout.class);

                if (logout.getStatus() == 200) {
                    Toast.makeText(vista.getContext(), "" + logout.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(vista.getContext(), LoginActivity.class));
                    userEditor.putBoolean(SESSION_KEY, false);
                    userEditor.apply();
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

        nQueue.add(logout);
    }
}