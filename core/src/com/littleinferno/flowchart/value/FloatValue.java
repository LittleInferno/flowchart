package com.littleinferno.flowchart.value;

public class FloatValue implements Value {
    FloatValue(float value) {
        this.value = value;
    }

    @Override
    public Value.Type type() {
        return Type.FLOAT;
    }

    @Override
    public boolean asBool() {
        return value == 1;
    }

    @Override
    public int asInt() {
        return (int) value;
    }

    @Override
    public float asFloat() {
        return value;
    }

    @Override
    public String asString() {
        return Float.toString(value);
    }

    private float value;
}
