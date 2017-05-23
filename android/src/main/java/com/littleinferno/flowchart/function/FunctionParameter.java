package com.littleinferno.flowchart.function;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.util.ArrayChangedListener;
import com.littleinferno.flowchart.util.Connection;
import com.littleinferno.flowchart.util.DataType;
import com.littleinferno.flowchart.util.NameChangedListener;
import com.littleinferno.flowchart.util.TypeChangedListener;

import java.util.ArrayList;
import java.util.List;

public class FunctionParameter implements Parcelable {

    public static final String TAG = "FUNCTION_PARAMETER";

    private final Function function;
    private String name;
    private DataType dataType;
    private boolean isArray;
    private Connection connection;

    private final List<NameChangedListener> nameChangedListeners;
    private final List<TypeChangedListener> typeChangedListeners;
    private final List<ArrayChangedListener> arrayChangedListeners;

    FunctionParameter(Function function, Connection connection, DataType dataType, String name, boolean isArray) {
        this.function = function;
        this.connection = connection;
        this.dataType = dataType;
        this.name = name;
        this.isArray = isArray;

        nameChangedListeners = new ArrayList<>();
        typeChangedListeners = new ArrayList<>();
        arrayChangedListeners = new ArrayList<>();
    }

    private FunctionParameter(Parcel in) {
        function = in.readParcelable(Function.class.getClassLoader());
        name = in.readString();
        isArray = in.readByte() != 0;
        dataType = DataType.valueOf(in.readString());
        connection = Connection.valueOf(in.readString());

        nameChangedListeners = new ArrayList<>();
        in.readList(nameChangedListeners, NameChangedListener.class.getClassLoader());

        typeChangedListeners = new ArrayList<>();
        in.readList(typeChangedListeners, TypeChangedListener.class.getClassLoader());

        arrayChangedListeners = new ArrayList<>();
        in.readList(arrayChangedListeners, ArrayChangedListener.class.getClassLoader());
    }

    public static final Creator<FunctionParameter> CREATOR = new Creator<FunctionParameter>() {
        @Override
        public FunctionParameter createFromParcel(Parcel in) {
            return new FunctionParameter(in);
        }

        @Override
        public FunctionParameter[] newArray(int size) {
            return new FunctionParameter[size];
        }
    };

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;

        notifyListenersNameChanged(newName);
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;

        notifyListenersTypeChanged(dataType);
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;

        notifyListenersIsArrayChanged(array);
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
    public void removeNameChange(NameChangedListener listener) {
        nameChangedListeners.remove(listener);
    }

    @SuppressWarnings("unused")
    public void removeTypeChange(TypeChangedListener listener) {
        typeChangedListeners.remove(listener);
    }

    @SuppressWarnings("unused")
    public void removeArrayChange(ArrayChangedListener listener) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(function, flags);
        dest.writeString(name);
        dest.writeByte((byte) (isArray ? 1 : 0));
        dest.writeString(dataType.toString());
        dest.writeString(connection.toString());
    }

    public boolean isUse() {
        return !nameChangedListeners.isEmpty() ||
                !typeChangedListeners.isEmpty() ||
                !arrayChangedListeners.isEmpty();
    }

    public SimpleObject getSaveInfo() {
        return new SimpleObject(name, dataType, connection, isArray);
    }

    @SuppressWarnings("WeakerAccess")
    public interface Add {
        void add(FunctionParameter parameter);
    }

    @SuppressWarnings("WeakerAccess")
    public interface Remove {
        void remove(FunctionParameter parameter);
    }

    public static class CallbackPair {
        public final Add add;
        public final Remove remove;

        CallbackPair(Add add, Remove remove) {
            this.add = add;
            this.remove = remove;
        }

        public Add getAdd() {
            return add;
        }

        public Remove getRemove() {
            return remove;
        }
    }


    public static class SimpleObject {
        final String name;
        final String type;
        final String connection;
        final boolean array;

        public SimpleObject(String name, DataType type, Connection connection, boolean array) {
            this.name = name;
            this.type = type.toString();
            this.connection = connection.toString();
            this.array = array;
        }
    }
}
