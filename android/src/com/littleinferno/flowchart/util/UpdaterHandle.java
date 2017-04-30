package com.littleinferno.flowchart.util;

import android.os.Parcel;
import android.os.Parcelable;

public class UpdaterHandle implements Parcelable {

    public static final String TAG = "UPDATER";

    private final Updater updater;

    public UpdaterHandle(final Updater updater) {
        this.updater = updater;
    }

    protected UpdaterHandle(Parcel in) {
        updater = (Updater) in.readArray(Object[].class.getClassLoader())[0];
    }

    public static final Creator<UpdaterHandle> CREATOR = new Creator<UpdaterHandle>() {
        @Override
        public UpdaterHandle createFromParcel(Parcel in) {
            return new UpdaterHandle(in);
        }

        @Override
        public UpdaterHandle[] newArray(int size) {
            return new UpdaterHandle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(new Object[]{updater});
    }

    public void update() {
        updater.update();
    }

    public interface Updater {
        void update();
    }
}
