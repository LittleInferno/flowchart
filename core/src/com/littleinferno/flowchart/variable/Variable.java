package com.littleinferno.flowchart.variable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.util.ArrayChangedListener;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.NameChangedListener;
import com.littleinferno.flowchart.util.TypeChangedListener;

import java.util.ArrayList;
import java.util.List;

public class Variable{

    private DataType dataType;
    private String name;
    private boolean isArray;

    private List<NameChangedListener> nameChangedListeners;
    private List<TypeChangedListener> typeChangedListeners;
    private List<ArrayChangedListener> arrayChangedListeners;
    private List<DestroyListener> destroyListeners;

    private VariableDetailsTable variableDetailsTable;

    Variable(String name, DataType type, boolean isArray) {
        this.name = name;
        this.dataType = type;
        this.isArray = isArray;

        this.nameChangedListeners = new ArrayList<>();
        this.typeChangedListeners = new ArrayList<>();
        this.arrayChangedListeners = new ArrayList<>();
        this.destroyListeners = new ArrayList<>();

        this.variableDetailsTable = new VariableDetailsTable(this);
    }

    public Variable() {
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType newType) {
        this.dataType = newType;

        notifyListenersTypeChanged(newType);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;

        notifyListenersNameChanged(newName);
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean isArray) {
        this.isArray = isArray;

        notifyListenersIsArrayChanged(isArray);
    }

    public String gen(BaseCodeGenerator builder) {
        return builder.makeVariable(getName(), getDataType(), isArray());
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

    public void addListener(DestroyListener listener) {
        destroyListeners.add(listener);
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

    private void notifyListenersDestroed() {
        Stream.of(destroyListeners).forEach(DestroyListener::destroyed);
    }

    public VariableDetailsTable getTable() {
        return variableDetailsTable;
    }

    void destroy() {
        notifyListenersDestroed();
    }
}
