package com.littleinferno.flowchart.value;

public class IntegerValue implements Value {
    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public Type type() {
        return Type.INT;
    }

    @Override
    public boolean asBool() {
        return value == 1;
    }

    @Override
    public int asInt() {
        return value;
    }

    @Override
    public float asFloat() {
        return (float) value;
    }

    @Override
    public String asString() {
        return Integer.toString(value);
    }

    private int value;
}
