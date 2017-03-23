package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Stream;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class BasePluginHandle {

    private final PluginParams pluginParams;
    private ZipFile file;

    public static String pluginJson = "plugin.json";

    public BasePluginHandle(File pluginFile) {
        try {
            file = new ZipFile(pluginFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pluginParams = pluginParamsFromJson();
    }

    public String readFile(String fileName) throws IOException {
        //noinspection unchecked
        List<ZipEntry> entries = Collections.list((Enumeration<ZipEntry>) file.entries());

        ZipEntry entry = Stream.of(entries)
                .filter(value -> value.getName().equals(pluginJson))
                .findSingle()
                .orElseThrow(() -> new FileNotFoundException("cannot find:" + pluginJson));

        Scanner scanner = new java.util.Scanner(file.getInputStream(entry)).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private PluginParams pluginParamsFromJson() {
        Gson gson = new Gson();
        String string = null;
        try {
            string = readFile(pluginJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gson.fromJson(string, PluginParams.class);
    }

    public PluginParams getPluginParams() {
        return pluginParams;
    }


    public abstract void onLoad();
    public abstract void onUnload();

    public static class PluginParams {
        // Core
        private final String pluginName;
        private final String pluginDescription;
        private final String pluginVersion;
        private final int apiVersion;

        private final String srcFileName;
        private final String[] drawableFiles;

        public PluginParams(String pluginName, String pluginDescription, String pluginVersion, int apiVersion, String srcFileName, String[] drawableFiles) {
            this.pluginName = pluginName;
            this.pluginDescription = pluginDescription;
            this.pluginVersion = pluginVersion;
            this.apiVersion = apiVersion;
            this.srcFileName = srcFileName;
            this.drawableFiles = drawableFiles;
        }

        public String getPluginName() {
            return pluginName;
        }

        public String getPluginDescription() {
            return pluginDescription;
        }

        public String getPluginVersion() {
            return pluginVersion;
        }

        public int getApiVersion() {
            return apiVersion;
        }

        public String getSrcFileName() {
            return srcFileName;
        }

        public String[] getDrawableFiles() {
            return drawableFiles;
        }
    }
}
