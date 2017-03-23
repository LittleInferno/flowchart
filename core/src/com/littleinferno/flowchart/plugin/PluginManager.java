package com.littleinferno.flowchart.plugin;

import java.io.File;
import java.util.List;

public class PluginManager {

    private List<NodePluginHandle> nodePluginHandles;


    void loadNodePlugins() {

    }

    void loadNodePlugin(File file) {
        NodePluginHandle nodePluginHandle = new NodePluginHandle(file);
        nodePluginHandle.onLoad();
    }


}
