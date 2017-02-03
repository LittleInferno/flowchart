package com.littleinferno.flowchart.parameter;

import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.VariableChangedListener;

import java.util.ArrayList;
import java.util.List;

public class Parameter {
    private String name;
    private DataType dataType;
    private Connection connection;
    private boolean isArray;

    private List<VariableChangedListener> parameterChangedListeners;

    public Parameter(String name, DataType type, Connection connection, boolean isArray) {
        this.name = name;
        this.dataType = type;
        this.connection = connection;
        this.isArray = isArray;
        parameterChangedListeners = new ArrayList<VariableChangedListener>();
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setName(String newName) {
        this.name = newName;
        notifyListenersNameChanged(newName);
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
        notifyListenersTypeChanged(dataType);
    }

    public void setArray(boolean array) {
        isArray = array;
        notifyListenersIsArrayChanged(array);
    }

    public void addListener(VariableChangedListener listener) {
        parameterChangedListeners.add(listener);
    }

    private void notifyListenersNameChanged(String newName) {
        for (VariableChangedListener listener : parameterChangedListeners) {
            listener.nameChanged(newName);
        }
    }

    private void notifyListenersTypeChanged(DataType newName) {
        for (VariableChangedListener listener : parameterChangedListeners) {
            listener.typeChanged(newName);
        }
    }

    private void notifyListenersIsArrayChanged(boolean isArray) {
        for (VariableChangedListener listener : parameterChangedListeners) {
            listener.isArrayChanged(isArray);
        }
    }
}
