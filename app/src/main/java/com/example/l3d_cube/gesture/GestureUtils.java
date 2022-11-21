package com.example.l3d_cube.gesture;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Arrays;

import es.dmoral.toasty.Toasty;

public class GestureUtils {
    private static final int vThreshold = 2000;
    private static int scrollCounter = 0;
    private static final int scrollLimiter = 4;

    private static float scrollstartX1;
    private static float scrollStartY1;

    public static byte[] handleFling(Context context, float velocityX, float velocityY) {
        int vx = Math.round(velocityX);
        int vy = Math.round(velocityY);

        Fling fling = new Fling(Math.round(velocityX), Math.round(velocityY));
        byte[] data = fling.toByteArray();

        String msg = "Fling: " + Arrays.toString(data);
        Toasty.info(context, msg, Toast.LENGTH_SHORT, true).show();

        return data;
    }

    public static boolean isAboveVThreshold(float velocityX, float velocityY){
        return ((Math.abs(velocityX) > vThreshold) || (Math.abs(velocityY) > vThreshold));
    }

    public static boolean shouldSendScroll(){
        if(scrollCounter >= scrollLimiter){
            scrollCounter = 0;
            return true;
        } else {
            scrollCounter++;
            return false;
        }
    }

//    private static String getFlingDirection(float velocityX, float velocityY) {
//        if(Math.abs(velocityX) > Math.abs((velocityY))){
//            if(velocityX >= 0.0){
//                return "right";
//            } else {
//                return "left";
//            }
//        }
//        else if(Math.abs(velocityX) < Math.abs((velocityY))) {
//            if(velocityY >= 0.0){
//                return "down";
//            } else {
//                return "up";
//            }
//        }
//        return null;
//    }

    public static byte[] handleScroll(Context context, MotionEvent event1, float distanceX, float distanceY) {
        int dx = Math.round(distanceX);
        int dy = Math.round(distanceY);

        Scroll scroll = new Scroll(dx, dy);
        byte[] data = scroll.toByteArray();

        if(!isSameScroll(event1)){
            String msg = "Scroll";
            Toasty.info(context, msg, Toast.LENGTH_SHORT, true).show();
        }

        return data;
    }

    private static boolean isSameScroll(MotionEvent event1) {
        if(scrollstartX1 != event1.getX() || scrollStartY1 != event1.getY()) {
            scrollstartX1 = event1.getX();
            scrollStartY1 = event1.getY();
            return false;
        }
        return true;
    }

    public static byte[] handleLongPress(Context context) {
        Gesture longPress = new Gesture(Gesture.GestureType.longPress);
        byte[] data = longPress.toByteArray();

        String msg = "Long-Press: " + Arrays.toString(data);
        Toasty.info(context, msg, Toast.LENGTH_SHORT, true).show();

        return data;
    }

    public static byte[] handleDoubleTap(Context context) {
        Gesture doubleTap = new Gesture(Gesture.GestureType.doubleTap);
        byte[] data = doubleTap.toByteArray();

        String msg = "Double-Tap: " + Arrays.toString(data);
        Toasty.info(context, msg, Toast.LENGTH_SHORT, true).show();

        return data;
    }
}
