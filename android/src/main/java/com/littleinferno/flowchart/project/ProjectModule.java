package com.littleinferno.flowchart.project;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.UUID;

@SuppressLint("ParcelCreator")
public class ProjectModule implements Parcelable {
    private final HashMap<UUID, Project> parents = new HashMap<>();
    private final Project project;
    private final UUID id;

    public ProjectModule(Project project) {
        id = UUID.randomUUID();
        this.project = project;
    }

    protected ProjectModule(Parcel in) {
        id = UUID.fromString(in.readString());
        project = parents.remove(id);
    }

    public Project getProject() {
        return project;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        parents.put(getId(), project);
    }

    public UUID getId() {
        return id;
    }
}
