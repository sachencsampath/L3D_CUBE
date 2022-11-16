package com.example.l3d_cube.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.l3d_cube.databinding.FragmentDashboardBinding;

import es.dmoral.toasty.Toasty;

public class DashboardFragment extends Fragment{

    private FragmentDashboardBinding binding;

    private GestureDetectorCompat motionDetector;
    private Context context;

    private float scrollstartX1, scrollStartY1;

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
            if(Math.abs(velocityX) > 1000 || Math.abs(velocityY) > 1000){
                Toasty.info(context, "Detected Fling with x-velocity: " + velocityX + " and y-velocity " + velocityY, Toast.LENGTH_SHORT, true).show();
            }
            return super.onFling(event1, event2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2,
                                float distanceX, float distanceY) {

            if(scrollstartX1 != event1.getX() || scrollStartY1 != event1.getY()) {
                scrollstartX1 = event1.getX();
                scrollStartY1 = event1.getY();
                Toasty.info(context, "Detected Scroll", Toast.LENGTH_SHORT, true).show();
            }
            return super.onScroll(event1, event2, distanceX, distanceY);
        }

        @Override
        public void onLongPress(MotionEvent event){
            Toasty.info(context, "Detected Long Press", Toast.LENGTH_SHORT, true).show();
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            Toasty.info(context, "Detected Double Tap", Toast.LENGTH_SHORT, true).show();
            return super.onDoubleTap(event);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}