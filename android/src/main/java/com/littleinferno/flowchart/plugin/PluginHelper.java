package com.littleinferno.flowchart.plugin;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.littleinferno.flowchart.util.Files;

import java.io.IOException;

public class PluginHelper {

    public static final String INDEX_FILE = "index.json";
    public static final String PLUGIN_FILE = "plugin.js";
    public static final String STANDART_PLUGIN = "standart plugin";


    public static BasePluginHandle.PluginParams getStandartPluginParams(AssetManager assetManager) throws IOException {
        return new Gson()
                .fromJson(Files.inputStreamToString(assetManager.open(INDEX_FILE)),
                        BasePluginHandle.PluginParams.class);
    }

    public static String getStandartPluginContent(AssetManager assetManager) throws IOException {
        return Files.inputStreamToString(assetManager.open(PLUGIN_FILE));
    }

    public static PluginHandle getStandartPlugin(Context context) throws Exception {
        return new PluginHandle(getStandartPluginParams(context.getAssets()),
                getStandartPluginContent(context.getAssets()));
    }

}
