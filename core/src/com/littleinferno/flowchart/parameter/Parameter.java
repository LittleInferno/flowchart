package com.littleinferno.flowchart.parameter;

import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.value.Value;

public abstract class Parameter {
    Function function;
    String name;
    Value.Type valueType;

    public Parameter(Function function,String name,Value.Type type){
        this.function = function;
        this.name = name;
        this.valueType = type;
    }

    public String getName() {
        return name;
    }

    public Value.Type getValueType() {
        return valueType;
    }

    public abstract void setName(String name);

    public abstract void setValueType(Value.Type valueType);
}
