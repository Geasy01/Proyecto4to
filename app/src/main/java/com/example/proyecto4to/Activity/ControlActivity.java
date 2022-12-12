package com.example.proyecto4to.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyecto4to.Adaptadores.BluetoothAdapterList;
import com.example.proyecto4to.Adaptadores.BluetoothAdapterLista;
import com.example.proyecto4to.Modelos.Bluetooth;
import com.example.proyecto4to.Otros.ConnectedThread;
import com.example.proyecto4to.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControlActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final static int REQUEST_ENABLE_BT = 1;
    public final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;
    private static String address = null;

    List<Bluetooth> ListaBluetooth;
    Spinner SpnListaBluetooth;
    private BluetoothAdapter mBTAdapter = null;
    private Handler mHandler;
    private ConnectedThread mConnectedThread;
    private BluetoothSocket mBTSocket = null;

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {

                    } else {

                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        SpnListaBluetooth = (Spinner) findViewById(R.id.SpnListaBluetooth);
        ListaBluetooth = new ArrayList<>();

        if (! mBTAdapter.isEnabled()) {
            bluetoothOn();
        }

        listPairedDevices();
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
        SpnListaBluetooth.setOnItemSelectedListener(this);

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

    private void bluetoothOn() {
        if (mBTAdapter == null) {
            Toast.makeText(getApplicationContext(), "Dispositivo Bluetooth no disponible", Toast.LENGTH_LONG).show();
            finish();
        } else {
            if (mBTAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Bluetooth está encendido", Toast.LENGTH_LONG).show();
            } else {
                Intent enableBtnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mGetContent.launch(enableBtnIntent);
            }
        }
    }

    private void listPairedDevices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ControlActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }

        Set<BluetoothDevice> mPairedDevices;
        ListaBluetooth.clear();

        mPairedDevices = mBTAdapter.getBondedDevices();

        if (mBTAdapter.isEnabled()) {
            mPairedDevices.forEach(device -> {
                BluetoothDevice mDevice = device;
                ListaBluetooth.add(new Bluetooth(mDevice.getName(), mDevice.getAddress()));
            });

            SpnListaBluetooth.setAdapter(new BluetoothAdapterLista(this, R.layout.item_nwbluetooth, ListaBluetooth));
            Toast.makeText(getApplicationContext(), "Mostrando carritos", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Bluetooth no encendido", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        BluetoothAdapterLista adapter = (BluetoothAdapterLista) parent.getAdapter();
        Bluetooth mDevice = adapter.getListaBluetooth().get(position);

        if (!mBTAdapter.isEnabled()) {
            Toast.makeText(getBaseContext(), "Bluetooth no encendido", Toast.LENGTH_SHORT).show();
            return;
        }

        final String address = mDevice.getAddress();
        final String name = mDevice.getName();

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
                        Toast.makeText(getBaseContext(),"Error al crear el socket", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!fail) {
                    mConnectedThread = new ConnectedThread(mBTSocket, mHandler);
                    mConnectedThread.start();

                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                            .sendToTarget();
                }
            }
        }.start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}