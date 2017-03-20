package com.littleinferno.flowchart;

import android.os.Environment;

import com.annimon.stream.Stream;

import java.io.File;
import java.util.List;

public class Files {


    static List<String> getProjects() {
        final File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "flowchart_projects");

        File[] listOfFiles = file.listFiles();

        return Stream.of(listOfFiles).filter(File::isDirectory).map(File::getName).toList();
    }
}
