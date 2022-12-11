package com.example.proyecto4to.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyecto4to.Modelos.Bluetooth;
import com.example.proyecto4to.R;

import java.util.List;

public class BluetoothAdapterList extends ArrayAdapter<Bluetooth> {
    private final List<Bluetooth> ListaBluetooth;
    private final Context context;
    private final int resource;

    public BluetoothAdapterList(@NonNull Context context, int resource, @NonNull List<Bluetooth> ListaBluetooth) {
        super(context, resource, ListaBluetooth);
        this.ListaBluetooth = ListaBluetooth;
        this.context = context;
        this.resource = resource;
    }

    public List<Bluetooth> getListaBluetooth() {
        return ListaBluetooth;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        view = LayoutInflater.from(context).inflate(resource,  parent, false);
        Bluetooth bluetooth = ListaBluetooth.get(position);
        TextView txtNombre = view.findViewById(R.id.deviceName);
        TextView txtAddress = view.findViewById(R.id.deviceAddress);
        txtNombre.setText(bluetooth.getName());
        txtAddress.setText(bluetooth.getAddress());
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        view = LayoutInflater.from(context).inflate(resource,  parent, false);
        Bluetooth bluetooth = ListaBluetooth.get(position);
        TextView txtNombre = view.findViewById(R.id.deviceName);
        TextView txtAddress = view.findViewById(R.id.deviceAddress);
        txtNombre.setText(bluetooth.getName());
        txtAddress.setText(bluetooth.getAddress());
        return view;
    }
}
