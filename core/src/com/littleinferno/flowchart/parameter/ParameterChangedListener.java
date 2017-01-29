package com.littleinferno.flowchart.parameter;

import com.littleinferno.flowchart.DataType;

public interface ParameterChangedListener {
    void nameChanged(String newName);

    void typeChanged(DataType newType);

    void isArrayChanged(boolean isArray);
}
