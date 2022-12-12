package com.example.proyecto4to.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto4to.Adaptadores.AdafruitFeedAdapter;
import com.example.proyecto4to.Modelos.AdafruitFeed;
import com.example.proyecto4to.Otros.SingletonRequest;
import com.example.proyecto4to.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInicio extends Fragment {
    Button btnControlar, btnAddFeed;
    View view;
    String temperatura, distancia, infrarrojo, polvo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String USER_PREFERENCES = "userPreferences";
    private static final String TOKEN_KEY = "token";

    private RequestQueue nQueue;
    ArrayList<AdafruitFeed> adF;
    AdafruitFeedAdapter adapterFeed;
    RecyclerView recyclerView;
    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;
    String token;

    public FragmentInicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInicio.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInicio newInstance(String param1, String param2) {
        FragmentInicio fragment = new FragmentInicio();
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
        view = inflater.inflate(R.layout.fragment_inicio, container, false);
        btnControlar = view.findViewById(R.id.btnControlar);
        btnAddFeed = view.findViewById(R.id.btnAddFeed);
        btnControlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ControlActivity.class));
            }
        });

        btnAddFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AgregarFeedActivity.class));
            }
        });

        nQueue = SingletonRequest.getInstance(view.getContext()).getRequestQueue();
        adF = new ArrayList<>();
        userPreferences = view.getContext().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();
        token = userPreferences.getString(TOKEN_KEY, null);

        getFeeds();
        return view;
    }

    public void getFeeds() {
        String url = "https://cleanbotapi.live/api/v1/feeds";

        final JsonObjectRequest getFeeds = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerFeed);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager linearManager = new LinearLayoutManager(view.getContext());
                recyclerView.setLayoutManager(linearManager);

                final Gson gson = new Gson();
                final AdafruitFeed adafruitFeed = gson.fromJson(response.toString(), AdafruitFeed.class);
                adapterFeed = new AdafruitFeedAdapter(adafruitFeed.getListFeedData());
                temperatura = adafruitFeed.getListFeedData().get(0).getName();
                distancia = adafruitFeed.getListFeedData().get(1).getName();
                infrarrojo = adafruitFeed.getListFeedData().get(2).getName();
                polvo = adafruitFeed.getListFeedData().get(3).getName();
                recyclerView.setAdapter(adapterFeed);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("errorPeticion", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        nQueue.add(getFeeds);
    }
}