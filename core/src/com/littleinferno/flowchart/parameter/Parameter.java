package com.littleinferno.flowchart.parameter;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Function;

public abstract class Parameter {
    Function function;
    String name;
    DataType valueType;

    public Parameter(Function function,String name,DataType type){
        this.function = function;
        this.name = name;
        this.valueType = type;
    }

    public String getName() {
        return name;
    }

    public DataType getValueType() {
        return valueType;
    }

    public abstract void setName(String name);

    public abstract void setValueType(DataType valueType);
}
