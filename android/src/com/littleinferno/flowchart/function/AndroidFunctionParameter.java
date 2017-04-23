package com.littleinferno.flowchart.function;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.util.NameChangedListener;
import com.littleinferno.flowchart.util.TypeChangedListener;
import com.littleinferno.flowchart.util.gui.ArrayChangedListener;

import java.util.ArrayList;
import java.util.List;

public class AndroidFunctionParameter implements Parcelable {

    public static final String TAG = "FUNCTION_PARAMETER";

    private final AndroidFunction function;
    private String name;
    private DataType dataType;
    private boolean isArray;
    private Connection connection;

    private final List<NameChangedListener> nameChangedListeners;
    private final List<TypeChangedListener> typeChangedListeners;
    private final List<ArrayChangedListener> arrayChangedListeners;

    public AndroidFunctionParameter(AndroidFunction function, Connection connection, DataType dataType, String name, boolean isArray) {
        this.function = function;
        this.connection = connection;
        this.dataType = dataType;
        this.name = name;
        this.isArray = isArray;

        nameChangedListeners = new ArrayList<>();
        typeChangedListeners = new ArrayList<>();
        arrayChangedListeners = new ArrayList<>();
    }

    protected AndroidFunctionParameter(Parcel in) {
        function = in.readParcelable(AndroidFunction.class.getClassLoader());
        name = in.readString();
        isArray = in.readByte() != 0;
        dataType = DataType.valueOf(in.readString());
        connection = Connection.valueOf(in.readString());

        nameChangedListeners = new ArrayList<>();
        typeChangedListeners = new ArrayList<>();
        arrayChangedListeners = new ArrayList<>();
    }

    public static final Creator<AndroidFunctionParameter> CREATOR = new Creator<AndroidFunctionParameter>() {
        @Override
        public AndroidFunctionParameter createFromParcel(Parcel in) {
            return new AndroidFunctionParameter(in);
        }

        @Override
        public AndroidFunctionParameter[] newArray(int size) {
            return new AndroidFunctionParameter[size];
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
        dest.writeParcelable(function, flags);
        dest.writeString(name);
        dest.writeByte((byte) (isArray ? 1 : 0));
        dest.writeString(dataType.toString());
        dest.writeString(connection.toString());
    }
}
