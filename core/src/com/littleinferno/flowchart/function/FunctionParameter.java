package com.littleinferno.flowchart.function;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.util.ArrayChangedListener;
import com.littleinferno.flowchart.util.NameChangedListener;
import com.littleinferno.flowchart.util.TypeChangedListener;

import java.util.ArrayList;
import java.util.List;

public class FunctionParameter {
    private String name;
    private DataType dataType;
    private boolean isArray;
    private Connection connection;

    private List<NameChangedListener> nameChangedListeners;
    private List<TypeChangedListener> typeChangedListeners;
    private List<ArrayChangedListener> arrayChangedListeners;

    public FunctionParameter() {
    }

    public FunctionParameter(String name, DataType type, Connection connection, boolean isArray) {
        this.name = name;
        this.dataType = type;
        this.connection = connection;
        this.isArray = isArray;

        this.nameChangedListeners = new ArrayList<>();
        this.typeChangedListeners = new ArrayList<>();
        this.arrayChangedListeners = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public boolean isArray() {
        return isArray;
    }

    public Connection getConnection() {
        return connection;
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

    public void addListener(NameChangedListener listener) {
        nameChangedListeners.add(listener);
    }

    public void addListener(TypeChangedListener listener) {
        typeChangedListeners.add(listener);
    }

    public void addListener(ArrayChangedListener listener) {
        arrayChangedListeners.add(listener);
    }

    private void notifyListenersNameChanged(String newName) {
        Stream.of(nameChangedListeners).forEach(var -> var.changed(newName));
    }

    private void notifyListenersTypeChanged(DataType newtype) {
        Stream.of(typeChangedListeners).forEach(var -> var.changed(newtype));
    }

    private void notifyListenersIsArrayChanged(boolean isArray) {
        Stream.of(arrayChangedListeners).forEach(var -> var.changed(isArray));
    }

    public interface Added {
        void add(FunctionParameter parameter);
    }

    public interface Removed {
        void remove(FunctionParameter parameter);
    }
}
