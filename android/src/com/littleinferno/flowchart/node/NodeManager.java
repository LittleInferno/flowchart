package com.littleinferno.flowchart.node;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.plugin.PluginHandle;
import com.littleinferno.flowchart.scene.AndroidSceneLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NodeManager implements Parcelable {
    private final HashMap<UUID, Function> parent = new HashMap<>();

    public static final String TAG = "NODE_MANAGER";

    private final List<AndroidNode> nodes = new ArrayList<>();
    private final Function function;
    private final UUID id;

    public NodeManager(Function function) {
        id = UUID.randomUUID();
        this.function = function;
    }

    private NodeManager(Parcel in) {
        id = UUID.fromString(in.readString());
        function = parent.remove(id);
    }

    public static final Creator<NodeManager> CREATOR = new Creator<NodeManager>() {
        @Override
        public NodeManager createFromParcel(Parcel in) {
            return new NodeManager(in);
        }

        @Override
        public NodeManager[] newArray(int size) {
            return new NodeManager[size];
        }
    };

    public AndroidNode createNode(@NonNull String nodeName, float x, float y) {
        PluginHandle.NodeHandle nodeHandle = function
                .getProject()
                .getNodeHandle(nodeName);

        AndroidNode node = new AndroidNode(function, nodeHandle);
        nodes.add(node);
        function.nodeAdded(node);

        node.getNodeHandle().getAttribute("functionInit")
                .ifPresent(o -> node.getNodeHandle()
                        .getPluginHandle()
                        .createScriptFun((org.mozilla.javascript.Function) o)
                        .call(node, function));

        node.setX(x);
        node.setY(y);

        if (nodeName.equals("function return node")) {
            List<AndroidNode> nodes = getNodes("function return node");
            if (nodes.size() == 1) node.setClosable(View.INVISIBLE);
            else if (nodes.size() == 2) {
                Stream.of(nodes).forEach(n -> n.setClosable(View.VISIBLE));
            }
        }

        return node;
    }

    public AndroidNode createNode(@NonNull String nodeName) {
        return createNode(nodeName, 0.f, 0.f);
    }

    public AndroidNode createNode(AndroidNode.SimpleObject savedInfo) {
        AndroidNode node = createNode(savedInfo.name, savedInfo.x, savedInfo.y);

        node.getNodeHandle().getAttribute("load")
                .ifPresent(o ->
                        node.getNodeHandle()
                        .getPluginHandle()
                        .createScriptFun((org.mozilla.javascript.Function) o)
                        .call(node, savedInfo.attributes));

        return node;
    }

    public List<AndroidNode> getNodes() {
        return nodes;
    }

    public AndroidNode getNode(String name) {
        return Stream.of(nodes)
                .filter(n -> n.getNodeHandle().getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find node"));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
    }

    public List<AndroidNode> getNodes(String name) {
        return Stream.of(nodes)
                .filter(n -> n.getNodeHandle().getName().equals(name))
                .toList();
    }

    public void removeNode(AndroidNode node) {
        AndroidSceneLayout parent = (AndroidSceneLayout) node.getParent();
        if (parent != null)
            parent.removeView(node);
        nodes.remove(node);

        if (node.getNodeHandle().getName().equals("function return node")) {
            List<AndroidNode> nodes = getNodes("function return node");
            if (nodes.size() == 1) node.setClosable(View.INVISIBLE);
        }
    }
}
