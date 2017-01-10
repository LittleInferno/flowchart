package com.littleinferno.flowchart.value;

/**
 * Created by danil on 07.01.2017.
 */

public interface Value {
    enum Type {
        EXECUTION,
        BOOL,
        INT,
        FLOAT,
        STRING,
    }

    Type type();

    boolean asBool();

    int asInt();

    float asFloat();

    String asString();

}
