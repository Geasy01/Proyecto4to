package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;

import com.example.proyecto4to.Otros.ConnectedThread;
import com.example.proyecto4to.R;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControlActivity extends AppCompatActivity {

    private ConnectedThread mConnectedThread;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        final JoystickView joystickRight = (JoystickView) findViewById(R.id.joystickView_right);

        joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMove(int angle, int strength) {
                JSONObject datosEnviar = new JSONObject();

                try {
                    datosEnviar.put("x", joystickRight.getNormalizedX());
                    datosEnviar.put("y", joystickRight.getNormalizedY());
                    datosEnviar.put("angulo", angle);
                    datosEnviar.put("strength", strength);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mBluetoothAdapter != null) {
                    if (mConnectedThread != null) {
                        mConnectedThread.write(datosEnviar.toString() + "?");
                    }
                }
            }
        }, 50);
    }
}