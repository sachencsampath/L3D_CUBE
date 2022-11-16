package com.example.l3d_cube.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.Random;

import no.nordicsemi.android.ble.livedata.ObservableBleManager;

public class BluetoothDeviceManager extends ObservableBleManager {

    private BluetoothGattCharacteristic readCharacteristic, writeCharacteristic;
    private boolean supported;

    public final MutableLiveData<Boolean> isDeviceConnected = new MutableLiveData<>();

    public void setIsDeviceConnected(boolean isDeviceConnected) {
        this.isDeviceConnected.setValue(isDeviceConnected);
    }


    public BluetoothDeviceManager(@NonNull final Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return new BluetoothDeviceManagerGattCallback();
    }

    private class BluetoothDeviceManagerGattCallback extends BleManagerGattCallback {
        @Override
        protected boolean isRequiredServiceSupported(@NonNull BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(BluetoothUtils.getUuidUart());
            if (service != null) {
                readCharacteristic = service.getCharacteristic(BluetoothUtils.getUuidRead());
                writeCharacteristic = service.getCharacteristic(BluetoothUtils.getUuidWrite());
            }
            supported = readCharacteristic != null && writeCharacteristic != null;
            return supported;
        }

        @Override
        protected void onServicesInvalidated() {
            writeCharacteristic = null;
            readCharacteristic = null;
            if(Boolean.TRUE.equals(isDeviceConnected.getValue())){
                isDeviceConnected.setValue(false);
            }
        }
    }

    public void write(byte[] data){

        if(writeCharacteristic == null){
            return;
        }

        writeCharacteristic(
                writeCharacteristic,
                data,
                BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        ).enqueue();
    }
}
