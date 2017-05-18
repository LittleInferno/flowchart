package com.littleinferno.flowchart.plugin;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.littleinferno.flowchart.Files;

import java.io.IOException;

public class PluginHelper {

    static final String standartPlugin = "new.fcp";
    public static final String indexFile = "index.json";
    public static final String pluginFile = "plugin.js";

    public static AndroidBasePluginHandle.PluginParams getStandartPluginParams(AssetManager assetManager) throws IOException {

        return new Gson()
                .fromJson(Files.readZipFromInputStream(assetManager.open(standartPlugin), indexFile),
                        AndroidBasePluginHandle.PluginParams.class);
    }

    public static String getStandartPluginContent(AssetManager assetManager) throws IOException {
        return Files.readZipFromInputStream(assetManager.open(standartPlugin), pluginFile);
    }
}
