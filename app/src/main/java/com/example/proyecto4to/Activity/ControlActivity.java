package com.example.proyecto4to.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.proyecto4to.Otros.ConnectedThread;
import com.example.proyecto4to.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControlActivity extends AppCompatActivity {

    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final static int REQUEST_ENABLE_BT = 1;
    public final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;
    private static String address = null;

    private BluetoothAdapter mBTAdapter = null;
    private Set<BluetoothDevice> mPairedDevices;
    private Handler mHandler;
    private ConnectedThread mConnectedThread;
    private BluetoothSocket mBTSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    readMessage = new String((byte[]) msg.obj, StandardCharsets.UTF_8);
                }

                if (msg.what == CONNECTING_STATUS) {
                    char[] sConnected;
                    if (msg.arg1 == 1) {
                    } else {
                    }
                }
            }
        };

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

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
                if (mBTAdapter != null) {
                    if (mConnectedThread != null) {
                        mConnectedThread.write(datosEnviar.toString() + "?");
                    }
                }
            }
        }, 50);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent getAddress = getIntent();
        address = getAddress.getStringExtra("btDevice");

        if (!mBTAdapter.isEnabled()) {
            Toast.makeText(getBaseContext(), "Bluetooth no encendido", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread() {
            @Override
            public void run() {
                boolean fail = false;
                BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                try {
                    mBTSocket = createBluetoothSocket(device);
                } catch (IOException e) {
                    fail = true;
                    Toast.makeText(getBaseContext(), "Error al crear el socket", Toast.LENGTH_SHORT).show();
                }

                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ControlActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                    }
                    mBTSocket.connect();
                } catch (IOException e) {
                    try {
                        fail = true;
                        mBTSocket.close();
                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                .sendToTarget();
                    } catch (IOException e2) {
                        Toast.makeText(getBaseContext(), "Error al crear el socket", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!fail) {
                    mConnectedThread = new ConnectedThread(mBTSocket, mHandler);
                    mConnectedThread.start();

                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, device.getName())
                            .sendToTarget();

                }
            }
        }.start();
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
            Log.e("Error", "Could not create Insecure RFComm Connection", e);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ControlActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }
}