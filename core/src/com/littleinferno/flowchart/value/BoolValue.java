package com.littleinferno.flowchart.value;

public class BoolValue extends Value {
    public BoolValue(boolean value) {
        this.value = value;
    }

    @Override
    public Type type() {
        return Type.BOOL;
    }

    @Override
    public boolean asBool() {
        return value;
    }

    @Override
    public int asInt() {
        return (value) ? 1 : 0;
    }

    @Override
    public float asFloat() {
        return (value) ? 1.f : 0.f;
    }

    @Override
    public String asString() {
        return Boolean.toString(value);
    }

    private boolean value;
}
