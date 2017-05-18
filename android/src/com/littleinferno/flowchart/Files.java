package com.littleinferno.flowchart;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Files {

    public static String projectLocation = "/flowchart_projects/projects";

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

    public static void writeToFile(@NonNull final String fileName, @NonNull final String content) {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.print(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static String projectNameToSaveName(String name) {
        return name + ".json";
    }


    public static String getSavesLocation() {
        return Environment.getExternalStorageDirectory().toString() + "/flowchart_projects/saves/";
    }

    public static String getPLuginsLocation() {
        return Environment.getExternalStorageDirectory().toString() + "/flowchart_projects/plugins/";
    }

    public static void copyFile(File source, File dest) {
        try (FileChannel inputChannel = new FileInputStream(source).getChannel();
             FileChannel outputChannel = new FileOutputStream(dest).getChannel()) {
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readZipFromInputStream(InputStream inputStream, String filename) throws IOException {

        ZipInputStream zip = new ZipInputStream(inputStream);
        ZipEntry ze;

        String content = null;

        while ((ze = zip.getNextEntry()) != null) {

            if (ze.getName().equals(filename)) {
                StringBuilder s = new StringBuilder();
                byte[] buffer = new byte[1024];
                int read = 0;

                while ((read = zip.read(buffer, 0, 1024)) >= 0) {
                    s.append(new String(buffer, 0, read));
                }
                content = s.toString();
            }
        }
        return content;
    }

}
