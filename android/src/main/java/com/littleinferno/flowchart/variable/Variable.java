package com.littleinferno.flowchart.variable;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.util.DataType;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.NameChangedListener;
import com.littleinferno.flowchart.util.TypeChangedListener;
import com.littleinferno.flowchart.util.ArrayChangedListener;

import java.util.ArrayList;
import java.util.List;

public class Variable extends ProjectModule implements Parcelable {

    public static final String TAG = "VARIABLE";

    private final VariableManager variableManager;

    private DataType dataType;
    private String name;
    private boolean isArray;

    private final List<NameChangedListener> nameChangedListeners;
    private final List<TypeChangedListener> typeChangedListeners;
    private final List<ArrayChangedListener> arrayChangedListeners;
    private final List<DestroyListener> destroyListeners;

    public Variable(final VariableManager variableManager, final DataType dataType, final String name, final boolean isArray) {
        super(variableManager.getProject());
        this.variableManager = variableManager;

        this.dataType = dataType;
        this.name = name;
        this.isArray = isArray;

        this.nameChangedListeners = new ArrayList<>();
        this.typeChangedListeners = new ArrayList<>();
        this.arrayChangedListeners = new ArrayList<>();
        this.destroyListeners = new ArrayList<>();
    }

    protected Variable(Parcel in) {
        super(in);
        variableManager = in.readParcelable(VariableManager.class.getClassLoader());

        name = in.readString();
        isArray = in.readByte() != 0;
        dataType = DataType.valueOf(in.readString());

        this.nameChangedListeners = new ArrayList<>();
        this.typeChangedListeners = new ArrayList<>();
        this.arrayChangedListeners = new ArrayList<>();
        this.destroyListeners = new ArrayList<>();
    }

    public static final Creator<Variable> CREATOR = new Creator<Variable>() {
        @Override
        public Variable createFromParcel(Parcel in) {
            return new Variable(in);
        }

        @Override
        public Variable[] newArray(int size) {
            return new Variable[size];
        }
    };

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

    @SuppressWarnings("unused")
    public void onNameChange(NameChangedListener listener) {
        nameChangedListeners.add(listener);
    }

    @SuppressWarnings("unused")
    public void onTypeChange(TypeChangedListener listener) {
        typeChangedListeners.add(listener);
    }

    @SuppressWarnings("unused")
    public void onArrayChange(ArrayChangedListener listener) {
        arrayChangedListeners.add(listener);
    }

    @SuppressWarnings("unused")
    public void removeNameChangeListener(NameChangedListener listener) {
        nameChangedListeners.remove(listener);
    }

    @SuppressWarnings("unused")
    public void removeTypeChangeListener(TypeChangedListener listener) {
        typeChangedListeners.remove(listener);
    }

    @SuppressWarnings("unused")
    public void removeArrayChangeListener(ArrayChangedListener listener) {
        arrayChangedListeners.remove(listener);
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

    public boolean isUse() {
        return !nameChangedListeners.isEmpty() ||
                !typeChangedListeners.isEmpty() ||
                !arrayChangedListeners.isEmpty();
    }

    public SimpleObject getSaveInfo() {
        return new SimpleObject(dataType, name, isArray);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(variableManager, flags);
        dest.writeString(name);
        dest.writeByte((byte) (isArray ? 1 : 0));
        dest.writeString(dataType.toString());
    }

    public static class SimpleObject {
        final DataType dataType;
        final String name;
        final boolean isArray;

        public SimpleObject(DataType dataType, String name, boolean isArray) {
            this.dataType = dataType;
            this.name = name;
            this.isArray = isArray;
        }
    }
}
