package com.example.proyecto4to.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto4to.Modelos.Bluetooth;
import com.example.proyecto4to.Otros.ConnectedThread;
import com.example.proyecto4to.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

public class MisCarritosActivity extends AppCompatActivity implements View.OnClickListener {

    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final static int REQUEST_ENABLE_BT = 1;
    public final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;

    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private Handler mHandler;
    private ConnectedThread mConnectedThread;
    private BluetoothSocket mBTSocket = null;
    private ListView mDevicesListView;

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
        mBTArrayAdapter = new ArrayAdapter<>(this, R.layout.item_bluetooth);

        mDevicesListView = (ListView) findViewById(R.id.devices_list_view);
        mDevicesListView.setAdapter(mBTArrayAdapter);
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        BluetoothSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            if (! mBTAdapter.isEnabled() && isChecked) {
                bluetoothOn();
            } else {
                bluetoothOff();
            }
        });

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

        if (mBTArrayAdapter == null) {
            txtStatusBlue.setText("Status: Bluetooth no encontrado");
            Toast.makeText(getApplicationContext(), "Bluetooth no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAddBluetooth){
            discover();
        }
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
        } else {
            mBTAdapter.disable();
            txtStatusBlue.setText("Bluetooth desactivado");
            Toast.makeText(getApplicationContext(), "Bluetooth apagado", Toast.LENGTH_SHORT).show();
        }
    }

    private void discover() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
        } else {
            if (mBTAdapter.isDiscovering()) {
                mBTAdapter.cancelDiscovery();
                Toast.makeText(getApplicationContext(), "Búsqueda detenida", Toast.LENGTH_SHORT).show();
            } else {
                if (mBTAdapter.isEnabled()) {
                    mBTArrayAdapter.clear();
                    mBTAdapter.startDiscovery();
                    Toast.makeText(getApplicationContext(), "Búsqueda iniciada", Toast.LENGTH_SHORT).show();
                    registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth no encendido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(MisCarritosActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MisCarritosActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                } else {
                    mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    mBTArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void listPairedDevices() {
        mBTArrayAdapter.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MisCarritosActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
        } else {
            mPairedDevices = mBTAdapter.getBondedDevices();
            if (mBTAdapter.isEnabled()) {
                for (BluetoothDevice device : mPairedDevices)
                    mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                Toast.makeText(getApplicationContext(), "Mostrando carritos", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Bluetooth no encendido", Toast.LENGTH_SHORT).show();
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth no encendido", Toast.LENGTH_SHORT).show();
                return;
            }

            txtStatusBlue.setText("Conectando...");
            String info = ((TextView) view).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);

            new Thread() {
                @Override
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creado sin exito", Toast.LENGTH_SHORT).show();
                    }

                    try {
                        if (ActivityCompat.checkSelfPermission(MisCarritosActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                        } else {
                            mBTSocket.connect();
                        }
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
    };

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {

        }
    }
}