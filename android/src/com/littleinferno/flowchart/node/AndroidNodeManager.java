package com.littleinferno.flowchart.node;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.plugin.AndroidNodePluginHandle;
import com.littleinferno.flowchart.scene.SceneType;

import org.mozilla.javascript.Function;

import java.util.ArrayList;
import java.util.List;

public class AndroidNodeManager implements Parcelable {

    public static final String TAG = "NODE_MANAGER";

    private final List<AndroidNode> nodes;
    private final SceneType sceneType;
    private final AndroidFunction function;

    public AndroidNodeManager(SceneType sceneType, AndroidFunction function) {
        this.sceneType = sceneType;
        this.function = function;
        nodes = new ArrayList<>();
    }

    private AndroidNodeManager(Parcel in) {
        function = in.readParcelable(AndroidFunction.class.getClassLoader());
        sceneType = SceneType.valueOf(in.readString());
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

    public AndroidNode createNode(@NonNull String nodeName) {
        AndroidNodePluginHandle.NodeHandle nodeHandle = function
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

        return node;
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
        dest.writeParcelable(function, flags);
        dest.writeString(sceneType.toString());
        dest.writeList(nodes);
    }
}
