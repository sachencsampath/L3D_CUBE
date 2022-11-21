package com.example.l3d_cube.bluetooth;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.l3d_cube.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import es.dmoral.toasty.Toasty;
import no.nordicsemi.android.ble.ConnectRequest;

public class BluetoothViewModel extends AndroidViewModel {
    private final BluetoothDeviceManager bluetoothDeviceManager;
    @Nullable
    private ConnectRequest connectRequest;
    private BluetoothDevice device;


    public BluetoothViewModel(@NonNull Application application) {
        super(application);
        bluetoothDeviceManager = new BluetoothDeviceManager(getApplication());
    }

    public void connect(@NonNull final BluetoothDevice target) {
        device = target;
        connectRequest = bluetoothDeviceManager.connect(device)
                .retry(3, 100)
                .useAutoConnect(false)
                .then(d -> connectRequest = null)
                .done(d -> {
                    bluetoothDeviceManager.setIsDeviceConnected(true);
                })
                .fail((d, s) -> {
                    BluetoothUtils.bluetoothConnectToastFail(getApplication());
                });
        connectRequest.enqueue();
    }

    public void disconnect() {
        device = null;
        if (connectRequest != null) {
            connectRequest.cancelPendingConnection();
        } else if (bluetoothDeviceManager.isConnected()) {
            bluetoothDeviceManager.disconnect().enqueue();
        }
        bluetoothDeviceManager.setIsDeviceConnected(false);
    }

    public void write(byte[] data){
        bluetoothDeviceManager.write(data);
    }

    public LiveData<Boolean> isConnected() {
        return bluetoothDeviceManager.isDeviceConnected;
    }

}
