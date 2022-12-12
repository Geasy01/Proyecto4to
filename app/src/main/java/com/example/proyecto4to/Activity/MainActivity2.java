package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.example.proyecto4to.Modelos.Bluetooth;
import com.example.proyecto4to.Modelos.LiveDataBluetoothViewModel;
import com.example.proyecto4to.R;

public class MainActivity2 extends AppCompatActivity {

    TextView txtblue;
    private LiveDataBluetoothViewModel liveDataBluetoothViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        liveDataBluetoothViewModel= new ViewModelProvider(this).get(LiveDataBluetoothViewModel.class);
        liveDataBluetoothViewModel.getBluetoothMutableLiveData().observe(this, new Observer<Bluetooth>() {
            @Override
            public void onChanged(Bluetooth bluetooth) {
                txtblue.setText(bluetooth.getName());
            }
        });
        liveDataBluetoothViewModel.setBluetoothMutableLiveData(new Bluetooth("rasssmiro","direccion"));
    }
}