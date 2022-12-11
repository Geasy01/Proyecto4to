package com.example.proyecto4to.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyecto4to.R;
import com.example.proyecto4to.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FragmentInicio());

        binding.bottomnavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.mHome:
                    replaceFragment(new FragmentInicio());
                    break;
                case R.id.mPerson:
                    replaceFragment(new FragmentPerfil());
                    break;
                case R.id.mSetting:
                    replaceFragment(new FragmentConfig());
                    break;
            }

            return true;
        });

        findViewById(R.id.btnPersonalizado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoPersonalizado();
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    private void mostrarModal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(FragmentPerfil.class);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.modal_editar, null);

        builder.setView(view);

        //Todo botones por defecto
        builder.setView(inflater.inflate(R.layout.modal_editar, null))
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(getApplicationContext(), "Datos guardados",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });


        final AlertDialog dialog = builder.create();
        dialog.show();

        Button btnCancel = view.findViewById(R.id.btnModalCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Datos guardados", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}