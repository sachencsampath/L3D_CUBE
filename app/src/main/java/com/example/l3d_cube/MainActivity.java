package com.example.l3d_cube;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.example.l3d_cube.bluetooth.BluetoothUtils;
import com.example.l3d_cube.bluetooth.BluetoothViewModel;
import com.example.l3d_cube.ui.FragmentDataTransfer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.l3d_cube.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements FragmentDataTransfer {

    private ActivityMainBinding binding;
    private BluetoothViewModel bluetoothViewModel;

    // Animation Variables
    private LottieAnimationView animationView;
    private final int animationDelay = 3300;
//    private final int animationDelay = 0;

    // Toolbar
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private Menu menu;

    // Bluetooth Variables
    private final String BTAG = "Bluetooth: ";
    private ActivityResultLauncher<Intent> btActivityResultLauncher;
    private ActivityResultLauncher<String> btRequestPermission;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        animationView = binding.animationView;
        startAsyncAnimation();

        appBarLayout = binding.appbarLayout;
        toolbar = binding.toolbar;
        setSupportActionBar(binding.toolbar);


        BottomNavigationView navView = binding.bottomNavMenu;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        // Configure the view model.
        bluetoothViewModel = new ViewModelProvider(this).get(BluetoothViewModel.class);

        bluetoothViewModel.isConnected().observe(this, isConnected -> {
            String bluetoothState = BluetoothUtils.getBluetoothState(isConnected);
            BluetoothUtils.bluetoothConnectToast(this, isConnected);
            menu.findItem(R.id.bluetoothStatus).setTitle(bluetoothState);
        });

        registerBTActivity();
        registerBTPermissionRequest();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void startAsyncAnimation() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            appBarLayout.setVisibility(View.VISIBLE);
            binding.bottomNavMenu.setVisibility(View.VISIBLE);

            animationView.cancelAnimation();
            animationView.setVisibility(View.GONE);
        }, animationDelay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bluetoothSettings) {
            Log.i(BTAG, "button pressed");
            connectToBluetooth();
        }
        return true;
    }

    public void registerBTActivity(){
        btActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        connectToBluetooth();
                    }
                });
    }

    public void registerBTPermissionRequest(){
        btRequestPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                        result -> {
                            if (result) {
                                connectToBluetooth();
                            }
                        }
                );
    }

    @SuppressLint("MissingPermission")
    public void connectToBluetooth() {
        if(Boolean.TRUE.equals(bluetoothViewModel.isConnected().getValue())){
            bluetoothViewModel.disconnect();
            return;
        }

        if (bluetoothAdapter == null) {
            Log.e(BTAG, "bluetooth adapter not available");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Log.e(BTAG, "bluetooth adapter not enabled");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            btActivityResultLauncher.launch(enableBtIntent);
            return;
        }

        if(!BluetoothUtils.isBluetoothPermissionGranted(this)){
            Log.e(BTAG, "bluetooth permissions not satisfied");
            if(BluetoothUtils.isSorAbove()) {
                btRequestPermission.launch(Manifest.permission.BLUETOOTH_CONNECT);
            }
            else{
                btRequestPermission.launch(Manifest.permission.BLUETOOTH);
            }
            return;
        }

        showSelectionDialog(bluetoothAdapter.getBondedDevices());
    }

    private void showSelectionDialog(Set<BluetoothDevice> pairedDevices) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        List<BluetoothDevice> devices = new ArrayList<>(pairedDevices);
        List<String> deviceNameAddress = BluetoothUtils.getBluetoothNamesAndAddresses(devices);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.select_dialog_singlechoice,
                deviceNameAddress);

        alertDialog.setSingleChoiceItems(adapter, -1,
                (dialog, which) -> {
                    dialog.dismiss();
                    int position = ((AlertDialog) dialog)
                            .getListView()
                            .getCheckedItemPosition();
                    BluetoothDevice selectedDevice = devices.get(position);
                    bluetoothViewModel.connect(selectedDevice);
                });
        alertDialog.setTitle("Select a Bluetooth Module");
        alertDialog.show();
    }

    public BluetoothViewModel getBluetoothViewModel(){
        return bluetoothViewModel;
    }

    @Override
    public void fragmentToBluetooth(String data) {
        if(Boolean.TRUE.equals(bluetoothViewModel.isConnected().getValue())){
            bluetoothViewModel.write(data.getBytes());
        } else {
            BluetoothUtils.noBluetoothDeviceConnectedToast(this);
        }
    }

    @Override
    public void fragmentToBluetooth(byte[] data) {
        if(Boolean.TRUE.equals(bluetoothViewModel.isConnected().getValue())){
            bluetoothViewModel.write(data);
        } else {
            BluetoothUtils.noBluetoothDeviceConnectedToast(this);
        }
    }
}