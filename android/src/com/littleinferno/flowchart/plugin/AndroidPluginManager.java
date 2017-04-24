package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AndroidPluginManager {


    private List<AndroidNodePluginHandle> nodePluginHandles;
    private CodeGeneratorPluginHandle codeGeneratorPluginHandle;

    private int pluginsCount;

    public AndroidPluginManager() {
        nodePluginHandles = new ArrayList<>();
    }

    public void registerFolder(final File pluginsLocation) {
        File[] files = pluginsLocation.listFiles();
        pluginsCount = files.length;
    }

    public void loadNodePlugins(final String[] plugins) {
        Stream.of(plugins).forEach(this::loadNodePlugin);
    }

    public void loadNodePlugin(final String plugin) {
        nodePluginHandles.add(new AndroidNodePluginHandle(plugin));
    }

    public void loadCodeGeneratorPlugin(final String plugin) {
        if (codeGeneratorPluginHandle != null)
            unloadCodeGeneratorPlugin(codeGeneratorPluginHandle);
        codeGeneratorPluginHandle = new CodeGeneratorPluginHandle(plugin);
    }

    private void unloadCodeGeneratorPlugin(CodeGeneratorPluginHandle codeGeneratorPluginHandle) {
        codeGeneratorPluginHandle.unload();
    }

    public void unloadNodePlugin(final String pluginName) {
        AndroidNodePluginHandle handle = Stream.of(nodePluginHandles)
                .filter(val -> val.getPluginParams().getPluginName().equals(pluginName))
                .findSingle()
                .orElseThrow(() -> new RuntimeException("cannot find plugin: " + pluginName));

        handle.unload();
        nodePluginHandles.remove(handle);
    }

    public void unloadNodePlugins() {
        Stream.of(nodePluginHandles).forEach(AndroidBasePluginHandle::unload);
    }

    public AndroidNodePluginHandle getNodePlugin(final String pluginName) {
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

    public Optional<AndroidNodePluginHandle.NodeHandle> getNode(final String nodeName) {
        return Stream.of(getLoadedNodePlugins())
                .flatMap(nph -> Stream.of(nph.getNodes()))
                .filter(value -> value.getName().equals(nodeName))
                .findFirst();
    }

    public CodeGeneratorPluginHandle.CodeGenerator getCodeGenerator() {
        return codeGeneratorPluginHandle.getCodeGenerator();
    }

    public int getPluginsCount() {
        return pluginsCount;
    }
}
