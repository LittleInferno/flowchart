package com.littleinferno.flowchart.function;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.util.gui.ArrayChangedListener;
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

    public FunctionParameter(FunctionParameterHandle functionParameterHandle) {
        this.name = functionParameterHandle.name;
        this.dataType = functionParameterHandle.dataType;
        this.connection = functionParameterHandle.connection;
        this.isArray = functionParameterHandle.isArray;

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

    public FunctionParameterHandle getHandle() {
        return new FunctionParameterHandle(name, dataType, connection, isArray);
    }

    static public class FunctionParameterHandle {
        public String name;
        public DataType dataType;
        public Connection connection;
        public boolean isArray;

        public FunctionParameterHandle() {
        }

        public FunctionParameterHandle(String name, DataType dataType, Connection connection, boolean isArray) {
            this.name = name;
            this.dataType = dataType;
            this.isArray = isArray;
            this.connection = connection;
        }
    }


}
