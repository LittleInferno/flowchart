package com.littleinferno.flowchart.util;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.littleinferno.flowchart.Setting;

import java.util.ArrayList;

public class Files {

    public static ArrayList<FileHandle> getProjects() {

        ArrayList<FileHandle> projects = Stream.of(Gdx.files.local(Setting.projectsLocation).list())
                .collect(Collectors.toCollection(ArrayList::new));

        if (Gdx.files.isExternalStorageAvailable()) {
            projects.addAll(Stream.of(Gdx.files.external(Setting.projectsLocation).list())
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        return projects;
    }

}
