package com.example.proyecto4to.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto4to.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPerfil extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String USER_PREFERENCES = "userPreferences";
    private static final String NAME_KEY = "nombre";
    private static final String AP_KEY = "apaterno";
    private static final String AM_KEY = "amaterno";
    private static final String EMAIL_KEY = "email";
    private static final String PHONE_KEY = "phone";

    TextView namePerfil, lastname1Perfil, lastname2Perfil, telefonoPerfil, emailPerfil;
    View vista;
    Button btnEditarProfile;
    SharedPreferences userPreferences;
    SharedPreferences.Editor userEditor;

    public FragmentPerfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPerfil.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPerfil newInstance(String param1, String param2) {
        FragmentPerfil fragment = new FragmentPerfil();
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
        vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        btnEditarProfile = vista.findViewById(R.id.btnEditarProfile);
        namePerfil = vista.findViewById(R.id.namePerfil);
        lastname1Perfil = vista.findViewById(R.id.lastname1Perfil);
        lastname2Perfil = vista.findViewById(R.id.lastname2Perfil);
        emailPerfil = vista.findViewById(R.id.emailPerfil);
        telefonoPerfil = vista.findViewById(R.id.telefonoPerfil);

        btnEditarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), EditPerfilActivity.class));
            }
        });

        userPreferences = vista.getContext().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        userEditor = userPreferences.edit();

        String nombre = userPreferences.getString(NAME_KEY, null);
        String ap_paterno = userPreferences.getString(AP_KEY, null);
        String ap_materno = userPreferences.getString(AM_KEY, null);
        String email = userPreferences.getString(EMAIL_KEY, null);
        String telefono = userPreferences.getString(PHONE_KEY, null);

        namePerfil.setText(nombre);
        lastname1Perfil.setText(ap_paterno);
        lastname2Perfil.setText(ap_materno);
        emailPerfil.setText(email);
        telefonoPerfil.setText(telefono);

        return vista;
    }
}