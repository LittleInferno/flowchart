package com.littleinferno.flowchart.value;

public class StringValue implements Value {
    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public Type type() {
        return Type.STRING;
    }

    @Override
    public boolean asBool() {
        return Boolean.valueOf(value);
    }

    @Override
    public int asInt() {
        return Integer.valueOf(value);
    }

    @Override
    public float asFloat() {
        return Float.valueOf(value);
    }

    @Override
    public String asString() {
        return value;
    }

    private String value;
}
