package com.littleinferno.flowchart.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.littleinferno.flowchart.plugin.AndroidBasePluginHandle;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;
import com.littleinferno.flowchart.plugin.PluginHelper;
import com.littleinferno.flowchart.project.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipFile;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Files {

    public static String projectLocation = "/flowchart_projects/projects";

    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    static List<String> getProjects() {
        File file = new File(Environment.getExternalStorageDirectory().toString() + projectLocation);

        File[] listOfFiles = file.listFiles();

        return Stream.of(listOfFiles).filter(File::isDirectory).map(File::getName).toList();
    }

    public static File newProjectFolder(String name) {
        File file = new File(Environment.getExternalStorageDirectory().toString() + projectLocation + "/" + name);
        file.mkdir();
        return file;
    }

    public static String inputStreamToString(final InputStream inputStream) {
        Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public static String readToString(final File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return Files.inputStreamToString(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeToFile(Context context, @NonNull final String fileName, @NonNull final String content) {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.print(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        updateAndroidFS(context, fileName);
    }


    public static String projectNameToSaveName(String name) {
        return name + ".json";
    }

    public static String saveNameToProjectName(String name) {
        return name.substring(0, name.lastIndexOf('.'));
    }


    public static String getSavesLocation() {
        return Environment.getExternalStorageDirectory().toString() + "/flowchart_projects/saves/";
    }

    public static String getSaveLocation(String projectName) {
        return getSavesLocation() + "/" + projectNameToSaveName(projectName);
    }

    public static String getPLuginsLocation() {
        return Environment.getExternalStorageDirectory().toString() + "/flowchart_projects/plugins/";
    }

    public static String getGenerateLocation() {
        return Environment.getExternalStorageDirectory().toString() + "/flowchart_projects/plugins/";
    }

    public static void copyFile(Context context, File source, File dest) {
        try (FileChannel inputChannel = new FileInputStream(source).getChannel();
             FileChannel outputChannel = new FileOutputStream(dest).getChannel()) {
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateAndroidFS(context, dest.getName());
    }

    private static void updateAndroidFS(Context context, String... paths) {
        MediaScannerConnection.scanFile(context, paths, null, null);
    }

    public static void saveProject(Project project) {
        Observable.just(project)
                .map(Project::getSaveInfo)
                .map(gson::toJson)
                .map(s -> {
                    Files.writeToFile(project.getContext(),
                            getSaveLocation(project.getName()), s);
                    return 1;
                })
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                });
    }

    public static void loadProject(Project project) {
        String string = readToString(new File(getSaveLocation(project.getName())));

        Project.SimpleObject s = gson.fromJson(string, Project.SimpleObject.class);
        try {
            project.init(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(Context context, String pluginName) {
        new File(pluginName).delete();
        updateAndroidFS(context, pluginName);
    }

    public static void saveGen(Project project, String generate) {
        Observable.just(generate)
                .map(s -> {
                    Files.writeToFile(project.getContext(),
                            getGenerateLocation() + project.getName(), generate);
                    return 1;
                })
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                });
    }

    public static AndroidPluginHandle loadPlugin(String plugin) throws Exception {
        ZipFile zip = new ZipFile(Files.getPLuginsLocation() + "/" + plugin);
        String index = Files.inputStreamToString(zip.getInputStream(zip.getEntry(PluginHelper.indexFile)));
        String pl = Files.inputStreamToString(zip.getInputStream(zip.getEntry(PluginHelper.pluginFile)));

        return new AndroidPluginHandle(new Gson().fromJson(index, AndroidBasePluginHandle.PluginParams.class), pl);
    }

}
