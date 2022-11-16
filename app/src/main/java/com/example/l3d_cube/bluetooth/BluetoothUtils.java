package com.example.l3d_cube.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.l3d_cube.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class BluetoothUtils {

    public final static UUID UUID_UART = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");

    private final static UUID UUID_Read = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    private final static UUID UUID_Write = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");

    public static boolean isBluetoothPermissionGranted(@NonNull final Context context) {
        if (isSorAbove())
            return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED;
        else{
            return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    @SuppressLint("MissingPermission")
    public static List<String> getBluetoothNamesAndAddresses(List<BluetoothDevice> bluetoothDevices){
        List<String> deviceNamesAddresses = new ArrayList<>();
        for (BluetoothDevice device : bluetoothDevices)
        {

            String deviceName = device.getName();
            String deviceAddress = device.getAddress();
            deviceNamesAddresses.add(deviceName + "\n" + deviceAddress);
        }
        return deviceNamesAddresses;
    }

    public static boolean isSorAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    public static UUID getUuidUart() {
        return UUID_UART;
    }

    public static UUID getUuidRead() {
        return UUID_Read;
    }

    public static UUID getUuidWrite() {
        return UUID_Write;
    }

    public static String getBluetoothState(boolean isConnected) {
        return isConnected ? "Connected" : "Disconnected";
    }

    public static void bluetoothConnectToast(Context context, boolean isConnected) {
        String connectionMsg = (isConnected ? "Connected to" : "Disconnected from") + " Bluetooth Module";
        bluetoothInfoToast(context, connectionMsg);
    }

    public static void bluetoothConnectToastFail(Context context){
        String failMsg = "Failed to connect to Bluetooth Device";
        bluetoothErrorToast(context, failMsg);
    }

    public static void noBluetoothDeviceConnectedToast(Context context){
        String failMsg = "No Bluetooth Device connected";
        bluetoothErrorToast(context, failMsg);
    }

    private static void bluetoothInfoToast(Context context, String message) {
        Toasty.custom(context,
                        message,
                        R.drawable.bluetooth_settings,
                        es.dmoral.toasty.R.color.infoColor ,
                        Toast.LENGTH_LONG,
                        true,
                        true)
                .show();
    }

    private static void bluetoothErrorToast(Context context, String message) {
        Toasty.error(context, message).show();
    }

}
