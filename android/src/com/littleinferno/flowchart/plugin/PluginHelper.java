package com.littleinferno.flowchart.plugin;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.littleinferno.flowchart.util.Files;

import java.io.IOException;

public class PluginHelper {

    public static final String indexFile = "index.json";
    public static final String pluginFile = "plugin.js";

    public static AndroidBasePluginHandle.PluginParams getStandartPluginParams(AssetManager assetManager) throws IOException {
        return new Gson()
                .fromJson(Files.inputStreamToString(assetManager.open(indexFile)),
                        AndroidBasePluginHandle.PluginParams.class);
    }

    public static String getStandartPluginContent(AssetManager assetManager) throws IOException {
        return Files.inputStreamToString(assetManager.open(pluginFile));
    }
}
