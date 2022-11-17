package com.example.l3d_cube.gesture;

public class Gesture {
    protected static final byte opcode = 0x02;

    enum GestureType {
        fling,
        scroll,
        longPress,
        doubleTap
    }

    private GestureType gesture;

    public Gesture(GestureType gesture){
        this.gesture = gesture;
    }

    public byte[] toByteArray() {
        byte[] data = new byte[2];
        data[0] = opcode;
        data[1] = getGestureCode();

        return data;
    }

    protected byte getGestureCode(){
        switch (gesture){
            case fling:
                return 0x01;
            case scroll:
                return 0x02;
            case doubleTap:
                return 0x03;
            case longPress:
                return 0x04;
        }
        return 0x00;
    }

}
