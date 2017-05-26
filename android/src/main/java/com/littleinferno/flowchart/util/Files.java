package com.littleinferno.flowchart.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.littleinferno.flowchart.plugin.BasePluginHandle;
import com.littleinferno.flowchart.plugin.PluginHandle;
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
import java.util.Scanner;
import java.util.zip.ZipFile;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Files {

    public static String getFlowchrtLocation() {
        return Environment.getExternalStorageDirectory().toString() + "/flowchart_projects";
    }

    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

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
        return getFlowchrtLocation() + "/saves";
    }

    public static String getSaveLocation(String projectName) {
        return getSavesLocation() + "/" + projectNameToSaveName(projectName);
    }

    public static String getPLuginsLocation() {
        return getFlowchrtLocation() + "/plugins";
    }

    public static String getGeneratesLocation() {
        return getFlowchrtLocation() + "/generations";
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

    public static Project.SimpleObject loadProjectSave(String name) {
        String string = readToString(new File(getSaveLocation(name)));
        return gson.fromJson(string, Project.SimpleObject.class);
    }

    public static void delete(Context context, String name) {
        new File(name).delete();
        updateAndroidFS(context, name);
    }

    public static void saveGen(Project project, String generate) {
        Observable.just(generate)
                .map(s -> {
                    Files.writeToFile(project.getContext(),
                            getGeneratesLocation() + "/" + project.getName() + ".txt", generate);
                    return 1;
                })
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                });
    }

    public static PluginHandle loadPlugin(Context context, String plugin) throws Exception {
        if (plugin.equals(PluginHelper.STANDART_PLUGIN))
            return PluginHelper.getStandartPlugin(context);

        ZipFile zip = new ZipFile(getPLuginLocation(plugin));
        String index = Files.inputStreamToString(zip.getInputStream(zip.getEntry(PluginHelper.INDEX_FILE)));
        String pl = Files.inputStreamToString(zip.getInputStream(zip.getEntry(PluginHelper.PLUGIN_FILE)));

        return new PluginHandle(new Gson().fromJson(index, BasePluginHandle.PluginParams.class), pl);
    }

    public static BasePluginHandle.PluginParams loadPluginParams(Context context, String plugin) throws IOException {
        if (plugin.equals(PluginHelper.STANDART_PLUGIN))
            return PluginHelper.getStandartPluginParams(context.getAssets());

        ZipFile zipFile = new ZipFile(getPLuginLocation(plugin));
        InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(PluginHelper.INDEX_FILE));

        return gson.fromJson(inputStreamToString(inputStream), BasePluginHandle.PluginParams.class);
    }

    public static String getPLuginLocation(String plugin) {
        return getPLuginsLocation() + "/" + plugin;
    }

    public static void initFolder(Context context) {
        File file = new File(getFlowchrtLocation());


        if (!file.exists()) {
            file.mkdir();

            File f1 = new File(getSavesLocation());
            File f2 = new File(getPLuginsLocation());
            File f3 = new File(getGeneratesLocation());

            f1.mkdirs();
            f2.mkdirs();
            f3.mkdirs();
        }
    }
}
