package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Stream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AndroidPluginManager {


    private List<AndroidNodePluginHandle> nodePluginHandles;
    private int pluginsCount;

    public AndroidPluginManager() {
        nodePluginHandles = new ArrayList<>();
    }

    public void registerFolder(File pluginsLocation) {
        File[] files = pluginsLocation.listFiles();
        pluginsCount = files.length;
    }

    public void loadNodePlugins(File pluginsLocation) {

        File[] files = pluginsLocation.listFiles();


    }


    public void loadNodePlugin(File file){
        nodePluginHandles.add(new AndroidNodePluginHandle(file));
    }

    public void unloadNodePlugin(String pluginName) {
        AndroidNodePluginHandle handle = Stream.of(nodePluginHandles)
                .filter(val -> val.getPluginParams().getPluginName().equals(pluginName))
                .findSingle()
                .orElseThrow(() -> new RuntimeException("cannot find plugin: " + pluginName));

        handle.onUnload();
        nodePluginHandles.remove(handle);
    }

    public AndroidNodePluginHandle getNodePlugin(String pluginName) {
        return Stream.of(nodePluginHandles)
                .filter(val -> val.getPluginParams().getPluginName().equals(pluginName))
                .findSingle()
                .orElseThrow(() -> new RuntimeException("cannot find plugin: " + pluginName));
    }

    public List<AndroidNodePluginHandle.NodeHandle> getLoadedNodeHandles() {
        return Stream.of(getLoadedNodePlugins())
                .flatMap(nph -> Stream.of(nph.getNodes()))
                .toList();
    }

    public List<AndroidNodePluginHandle> getLoadedNodePlugins() {
        return nodePluginHandles;
    }

    public int getPluginsCount() {
        return pluginsCount;
    }
}
