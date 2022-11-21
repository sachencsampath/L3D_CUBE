package com.example.l3d_cube.gesture;

public class Scroll extends Gesture{

    private int distanceX;
    private int distanceY;
    public Scroll(int distanceX, int distanceY) {
        super(GestureType.scroll);

        this.distanceX = distanceX;
        this.distanceY = distanceY;
    }

    @Override
    public byte[] toByteArray() {
        byte[] data = new byte[10];
        data[0] = opcode;
        data[1] = getGestureCode();

        data[2] = (byte) (distanceX >> 24);
        data[3] = (byte) (distanceX >> 16);
        data[4] = (byte) (distanceX >> 8);
        data[5] = (byte) (distanceX);

        data[6] = (byte) (distanceY >> 24);
        data[7] = (byte) (distanceY >> 16);
        data[8] = (byte) (distanceY >> 8);
        data[9] = (byte) (distanceY);

        return data;
    }
}
