package com.example.l3d_cube.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.l3d_cube.ui.FragmentDataTransfer;
import com.example.l3d_cube.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private EditText mathEquation;
    private FloatingActionButton sendButton;

    FragmentDataTransfer fragmentDataTransfer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mathEquation = binding.mathEquation;
        sendButton = binding.sendButton;
        sendButton.setOnClickListener(view -> {
            sendToBluetooth(mathEquation.getText().toString());
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentDataTransfer = (FragmentDataTransfer) context;
    }

    public void sendToBluetooth(String data) {
        fragmentDataTransfer.fragmentToBluetooth(data);
    }
}