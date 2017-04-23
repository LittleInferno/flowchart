package com.littleinferno.flowchart.variable;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.NameChangedListener;
import com.littleinferno.flowchart.util.TypeChangedListener;
import com.littleinferno.flowchart.util.gui.ArrayChangedListener;

import java.util.ArrayList;
import java.util.List;

public class AndroidVariable implements ProjectModule, Parcelable {

    public static final String TAG = "VARIABLE";

    private final AndroidVariableManager variableManager;

    private DataType dataType;
    private String name;
    private boolean isArray;

    private final List<NameChangedListener> nameChangedListeners;
    private final List<TypeChangedListener> typeChangedListeners;
    private final List<ArrayChangedListener> arrayChangedListeners;
    private final List<DestroyListener> destroyListeners;

    public AndroidVariable(final AndroidVariableManager variableManager, final DataType dataType, final String name, final boolean isArray) {
        this.variableManager = variableManager;

        this.dataType = dataType;
        this.name = name;
        this.isArray = isArray;

        this.nameChangedListeners = new ArrayList<>();
        this.typeChangedListeners = new ArrayList<>();
        this.arrayChangedListeners = new ArrayList<>();
        this.destroyListeners = new ArrayList<>();
    }

    protected AndroidVariable(Parcel in) {
        variableManager = in.readParcelable(AndroidVariableManager.class.getClassLoader());

        name = in.readString();
        isArray = in.readByte() != 0;
        dataType = DataType.valueOf(in.readString());

        this.nameChangedListeners = new ArrayList<>();
        this.typeChangedListeners = new ArrayList<>();
        this.arrayChangedListeners = new ArrayList<>();
        this.destroyListeners = new ArrayList<>();
    }

    public static final Creator<AndroidVariable> CREATOR = new Creator<AndroidVariable>() {
        @Override
        public AndroidVariable createFromParcel(Parcel in) {
            return new AndroidVariable(in);
        }

        @Override
        public AndroidVariable[] newArray(int size) {
            return new AndroidVariable[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(variableManager, flags);
        dest.writeString(name);
        dest.writeByte((byte) (isArray ? 1 : 0));
        dest.writeString(dataType.toString());
    }

    @Override
    public FlowchartProject getProject() {
        return variableManager.getProject();
    }
}
