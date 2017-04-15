package com.littleinferno.flowchart.node;

import android.support.annotation.NonNull;

import com.littleinferno.flowchart.Scene;
import com.littleinferno.flowchart.plugin.AndroidNodePluginHandle;

public class AndroidNode extends BaseNode {

    private final AndroidNodePluginHandle.NodeHandle nodeHandle;

    public AndroidNode(@NonNull Scene scene, @NonNull AndroidNodePluginHandle.NodeHandle nodeHandle) {
        super(scene);
        this.nodeHandle = nodeHandle;
        this.nodeHandle.getInit().call(this);

        layout.nodeTitle.setText(this.nodeHandle.getTitle());
    }

    public AndroidNodePluginHandle.NodeHandle getNodeHandle() {
        return nodeHandle;
    }

}
