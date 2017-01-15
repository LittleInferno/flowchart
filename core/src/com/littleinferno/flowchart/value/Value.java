package com.littleinferno.flowchart.value;

public abstract class Value {
    public enum Type {
        EXECUTION,
        BOOL,
        INT,
        FLOAT,
        STRING,
    }

    public abstract Type type();

    public abstract boolean asBool();

    public abstract int asInt();

    public abstract float asFloat();

    public abstract String asString();

    static public Value add(Value left, Value right) {
        switch (left.type()) {
            case INT:
                return new IntegerValue(left.asInt() + right.asInt());
            case FLOAT:
                return new FloatValue(left.asFloat() + right.asFloat());
            case STRING:
                return new StringValue(left.asString() + right.asString());
        }
        return null;
    }

    static public Value sub(Value left, Value right) {
        switch (left.type()) {
            case INT:
                return new IntegerValue(left.asInt() - right.asInt());
            case FLOAT:
                return new FloatValue(left.asFloat() - right.asFloat());
        }
        return null;
    }

    static public Value mul(Value left, Value right) {
        switch (left.type()) {
            case INT:
                return new IntegerValue(left.asInt() * right.asInt());
            case FLOAT:
                return new FloatValue(left.asFloat() * right.asFloat());
        }
        return null;
    }

    static public Value div(Value left, Value right) {
        switch (left.type()) {
            case INT:
                return new IntegerValue(left.asInt() / right.asInt());
            case FLOAT:
                return new FloatValue(left.asFloat() / right.asFloat());
        }
        return null;
    }

    static public Value equals(Value left, Value right) {
        switch (left.type()) {
            case BOOL:
                return new BoolValue(left.asBool() == right.asBool());
            case INT:
                return new BoolValue(left.asInt() == right.asInt());
            case FLOAT:
                return new BoolValue(left.asFloat() == right.asFloat());
            case STRING:
                return new BoolValue(left.asString() == right.asString());
        }
        return null;
    }

    static public Value less(Value left, Value right) {
        switch (left.type()) {
            case INT:
                return new BoolValue(left.asInt() < right.asInt());
            case FLOAT:
                return new BoolValue(left.asFloat() < right.asFloat());
            case STRING:
                return new BoolValue(left.asString().compareTo(right.asString()) < 0);
        }
        return null;
    }

    static public Value great(Value left, Value right) {
        switch (left.type()) {
            case INT:
                return new BoolValue(left.asInt() > right.asInt());
            case FLOAT:
                return new BoolValue(left.asFloat() > right.asFloat());
            case STRING:
                return new BoolValue(left.asString().compareTo(right.asString()) > 0);
        }
        return null;
    }

}
