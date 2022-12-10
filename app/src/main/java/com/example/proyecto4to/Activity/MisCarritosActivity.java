package com.example.proyecto4to.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto4to.Modelos.Bluetooth;
import com.example.proyecto4to.Otros.ConnectedThread;
import com.example.proyecto4to.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MisCarritosActivity extends AppCompatActivity implements View.OnClickListener {

    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final static int REQUEST_ENABLE_BT = 1;
    public final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;

    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    List<Bluetooth> ListaBluetooth;
    private Handler mHandler;
    private ConnectedThread mConnectedThread;
    private BluetoothSocket mBTSocket = null;

    Switch BluetoothSwitch;
    Button btnAddBluetooth;
    TextView txtBlue, txtStatusBlue;

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        txtStatusBlue.setText("Encendido");
                    } else {
                        txtStatusBlue.setText("Desactivado");
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_carritos);

        BluetoothSwitch = (Switch) findViewById(R.id.BluetoothSwitch);
        btnAddBluetooth = (Button) findViewById(R.id.btnAddBluetooth);
        txtStatusBlue = (TextView) findViewById(R.id.txtStatusBlue);

        btnAddBluetooth.setOnClickListener(this);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        BluetoothSwitch.setOnCheckedChangeListener((v,i) -> {
            if(!mBTAdapter.isEnabled()) {
                bluetoothOn();
            } else {
                bluetoothOff();
            }
        });

        if (mBTAdapter.isEnabled()) {
            BluetoothSwitch.setChecked(true);
        } else {
            BluetoothSwitch.setChecked(false);
        };

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    readMessage = new String((byte[]) msg.obj, StandardCharsets.UTF_8);
                    txtBlue.setText(readMessage);

                }

                if (msg.what == CONNECTING_STATUS) {
                    char[] sConnected;
                    if (msg.arg1 == 1) {
                        txtStatusBlue.setText("Status: Conectado a " + msg.obj);
                    } else {
                        txtStatusBlue.setText("Conexión fallida");
                    }
                }
            }
        };
    }

    @Override
    public void onClick(View view) {

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

    private void bluetoothOff() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

        }
        mBTAdapter.disable();
        txtStatusBlue.setText("Bluetooth desactivado");
        Toast.makeText(getApplicationContext(),"Bluetooth apagado", Toast.LENGTH_SHORT).show();
    }



    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
            Log.e("Error", "Could not create Insecure RFComm Connection", e);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

        }
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }
}