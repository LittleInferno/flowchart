package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.Scene;
import com.littleinferno.flowchart.plugin.AndroidNodePluginHandle;

public class AndroidNode extends BaseNode {


    private final AndroidNodePluginHandle.NodeHandle nodeHandle;

    public AndroidNode(Scene scene, AndroidNodePluginHandle.NodeHandle nodeHandle) {
        super(scene);

        this.nodeHandle = nodeHandle;
        this.nodeHandle.getInit().call(this);
    }
}
