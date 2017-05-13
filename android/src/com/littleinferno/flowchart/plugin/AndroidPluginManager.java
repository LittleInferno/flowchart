package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class AndroidPluginManager {

    private AndroidPluginHandle pluginHandle;

    private int pluginsCount;

    public AndroidPluginManager() {
    }

    public void registerFolder(final File pluginsLocation) {
        File[] files = pluginsLocation.listFiles();
        pluginsCount = files.length;
    }

    public void loadPlugin(final String plugin) {
        pluginHandle = new AndroidPluginHandle(plugin);
    }

    public void unloadPlugin() {
        pluginHandle.unload();
        pluginHandle = null;
    }

    public AndroidPluginHandle getPlugin() {
        return pluginHandle;
    }

    public List<AndroidPluginHandle.NodeHandle> getLoadedNodeHandles() {
        return pluginHandle != null ? pluginHandle.getNodes() : Collections.EMPTY_LIST;
    }

    public Optional<AndroidPluginHandle.NodeHandle> getNode(final String nodeName) {
        return Stream.of(pluginHandle.getNodes())
                .filter(value -> value.getName().equals(nodeName))
                .findFirst();
    }

    public int getPluginsCount() {
        return pluginsCount;
    }

    public AndroidPluginHandle.RuleHandle getRules() {
        return pluginHandle.getRules();
    }
}
