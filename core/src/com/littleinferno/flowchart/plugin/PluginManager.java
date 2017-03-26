package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Stream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {

    private List<NodePluginHandle> nodePluginHandles;

    public PluginManager() {
        nodePluginHandles = new ArrayList<>();
    }

    public void loadNodePlugins(File pluginsLocation) {

        File[] files = pluginsLocation.listFiles();

        if (files != null) {
            Stream.of(files).forEach(this::loadNodePlugin);
        }
    }

    public void loadNodePlugin(File file) {
        NodePluginHandle handle = new NodePluginHandle(file);
        handle.onLoad();
        nodePluginHandles.add(handle);
    }

    public void unloadNodePlugin(String pluginName) {
        NodePluginHandle handle = Stream.of(nodePluginHandles)
                .filter(val -> val.getPluginParams().getPluginName().equals(pluginName))
                .findSingle()
                .orElseThrow(() -> new RuntimeException("cannot find plugin: " + pluginName));

        handle.onUnload();
        nodePluginHandles.remove(handle);
    }

    public NodePluginHandle getNodePlugin(String pluginName) {
        return Stream.of(nodePluginHandles)
                .filter(val -> val.getPluginParams().getPluginName().equals(pluginName))
                .findSingle()
                .orElseThrow(() -> new RuntimeException("cannot find plugin: " + pluginName));
    }

    public List<NodePluginHandle> getLoadedNodePlugins() {
        return nodePluginHandles;
    }
}
