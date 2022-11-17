package com.example.l3d_cube.gesture;

public class Fling extends Gesture{

    private int velocityX;
    private int velocityY;
    public Fling(int velocityX, int velocityY) {
        super(GestureType.fling);

        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    @Override
    public byte[] toByteArray() {
        byte[] data = new byte[10];
        data[0] = opcode;
        data[1] = getGestureCode();

        data[2] = (byte) (velocityX >> 24);
        data[3] = (byte) (velocityX >> 16);
        data[4] = (byte) (velocityX >> 8);
        data[5] = (byte) (velocityX);

        data[6] = (byte) (velocityY >> 24);
        data[7] = (byte) (velocityY >> 16);
        data[8] = (byte) (velocityY >> 8);
        data[9] = (byte) (velocityY);

        return data;
    }
}
