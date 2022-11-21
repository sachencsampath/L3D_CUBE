package com.example.l3d_cube.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import com.example.l3d_cube.gesture.GestureUtils;
import com.example.l3d_cube.databinding.FragmentDashboardBinding;
import com.example.l3d_cube.ui.FragmentDataTransfer;

public class DashboardFragment extends Fragment{

    private FragmentDashboardBinding binding;
    private Context context;

    FragmentDataTransfer fragmentDataTransfer;

    private GestureDetectorCompat motionDetector;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getActivity();

        motionDetector = new GestureDetectorCompat(context, new GestureListener());

        binding.layout.setOnTouchListener((view, motionEvent) -> {
            motionDetector.onTouchEvent(motionEvent);
            return true;
        });

        return root;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if(GestureUtils.isAboveVThreshold(velocityX, velocityY)) {
                sendToBluetooth(GestureUtils.handleFling(context, velocityX, velocityY));
            }
            return super.onFling(event1, event2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2,
                                float distanceX, float distanceY) {
            if(GestureUtils.shouldSendScroll()){
                sendToBluetooth(GestureUtils.handleScroll(context, event1, distanceX, distanceY));
            }
            return super.onScroll(event1, event2, distanceX, distanceY);
        }

        @Override
        public void onLongPress(MotionEvent event){
            sendToBluetooth(GestureUtils.handleLongPress(context));
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            sendToBluetooth(GestureUtils.handleDoubleTap(context));
            return super.onDoubleTap(event);
        }
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

    public void sendToBluetooth(byte[] data) {
        fragmentDataTransfer.fragmentToBluetooth(data);
    }
}