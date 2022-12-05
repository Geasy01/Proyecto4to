package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.proyecto4to.R;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final static int REQUEST_ENABLE_BT = 1;
    public final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}