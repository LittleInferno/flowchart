package com.littleinferno.flowchart.node;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;

import org.mozilla.javascript.Function;

import java.util.ArrayList;
import java.util.List;

public class AndroidNodeManager implements Parcelable {

    public static final String TAG = "NODE_MANAGER";

    private final List<AndroidNode> nodes;
    private final AndroidFunction function;

    public AndroidNodeManager(AndroidFunction function) {
        this.function = function;
        nodes = new ArrayList<>();
    }

    private AndroidNodeManager(Parcel in) {
        function = in.readParcelable(AndroidFunction.class.getClassLoader());
        nodes = new ArrayList<>();
        in.readList(nodes, AndroidNode.class.getClassLoader());
    }

    public static final Creator<AndroidNodeManager> CREATOR = new Creator<AndroidNodeManager>() {
        @Override
        public AndroidNodeManager createFromParcel(Parcel in) {
            return new AndroidNodeManager(in);
        }

        @Override
        public AndroidNodeManager[] newArray(int size) {
            return new AndroidNodeManager[size];
        }
    };

    public AndroidNode createNode(@NonNull String nodeName, float x, float y) {
        AndroidPluginHandle.NodeHandle nodeHandle = function
                .getProject()
                .getPluginManager()
                .getNode(nodeName)
                .orElseThrow(() -> new RuntimeException("cannot find node: " + nodeName));

        AndroidNode node = new AndroidNode(function, nodeHandle);
        nodes.add(node);
        function.nodeAdded(node);

        node.getNodeHandle().getAttribute("functionInit")
                .ifPresent(o -> node.getNodeHandle()
                        .getPluginHandle()
                        .createScriptFun((Function) o)
                        .call(node, function));

        node.setX(x);
        node.setY(y);

        return node;
    }

    public AndroidNode createNode(@NonNull String nodeName) {
        return createNode(nodeName, 0.f, 0.f);
    }

    public AndroidNode createNode(AndroidNode.SimpleObject savedInfo) {
        return createNode(savedInfo.name, savedInfo.x, savedInfo.y);
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
        dest.writeParcelable(function, flags);
        dest.writeList(nodes);
    }
}
