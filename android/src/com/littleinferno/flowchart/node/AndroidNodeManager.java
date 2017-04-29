package com.littleinferno.flowchart.node;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.plugin.AndroidNodePluginHandle;
import com.littleinferno.flowchart.scene.SceneType;

import java.util.ArrayList;
import java.util.List;

public class AndroidNodeManager implements Parcelable {

    public static final String TAG = "NODE_MANAGER";

    final private List<AndroidNode> nodes;
    private final SceneType sceneType;
    private AndroidFunction function;

    public AndroidNodeManager(SceneType sceneType, AndroidFunction function) {
        this.sceneType = sceneType;
        this.function = function;
        nodes = new ArrayList<>();
    }

    public Optional<AndroidNode> createNode(@NonNull String nodeName) {
        AndroidNodePluginHandle.NodeHandle nodeHandle = function
                .getProject()
                .getPluginManager()
                .getNode(nodeName)
                .orElseThrow(() -> new RuntimeException("cannot find node: " + nodeName));

        SceneType s = nodeHandle.getAttribute("sceneType")
                .map(String.class::cast).map(SceneType::valueOf).orElse(SceneType.ANY);

        if (s == SceneType.ANY || s == sceneType) {
            AndroidNode node = new AndroidNode(function.getProject().getContext(), nodeHandle);
            nodes.add(node);
            function.nodeAdded(node);
            return Optional.of(node);
        }

        return Optional.empty();
    }

    public List<AndroidNode> getNodes() {
        return nodes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
