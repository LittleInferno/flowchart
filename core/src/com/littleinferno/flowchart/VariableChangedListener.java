package com.littleinferno.flowchart;

public interface VariableChangedListener {
    void nameChanged(String newName);

    void typeChanged(DataType newType);

    void isArrayChanged(boolean isArray);

    void destroed();
}
