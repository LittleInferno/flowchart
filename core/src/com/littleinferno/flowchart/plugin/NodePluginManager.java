package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Stream;
import com.badlogic.gdx.files.FileHandle;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.node.PluginNode;
import com.littleinferno.flowchart.pin.Pin;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NodePluginManager {

    private Map<String, PluginNodeHandle> handles;

    private static Context rhino;

    private static Scriptable scope;

    public NodePluginManager() {
        handles = new HashMap<>();

        rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
    }

    public void addPlugins(FileHandle pluginsDir) {
        Stream.of(pluginsDir.list()).forEach(this::addPlugin);
    }

    public void addPlugin(FileHandle file) {
        scope = rhino.initStandardObjects();
        rhino.evaluateString(scope, file.readString(), "JavaScript", 1, null);

        Function fct = (Function) scope.get("exportNodes", scope);
        NativeArray result = (NativeArray) fct.call(rhino, scope, scope, new Object[]{});
        registerNodes(result);
    }

    private void registerNodes(NativeArray object) {
        for (Object i : object) registerNode((ScriptableObject) i);
    }

    public void registerNode(ScriptableObject object) {
        String nodeName = (String) object.get("name");
        String nodeTitle = (String) object.get("title");

        Boolean start = (Boolean) object.get("programstart");
        boolean nodeStart = (start != null) ? start : false;

        Boolean single = (Boolean) object.get("single");
        boolean nodeSingle = (single != null) ? single : false;

        String sceneType = (String) object.get("sceneType");
        if (sceneType == null) sceneType = "any";

        ScriptFun nodeFun = new ScriptFun((Function) object.get("gen"));

        //noinspection unchecked
        List<NativeObject> pins = (List<NativeObject>) object.get("pins");

        Pin[] nodePins = Stream.of(pins).map(this::objectToPin).toArray(Pin[]::new);

        handles.put(nodeName,
                new PluginNodeHandle(nodeName, nodeTitle, nodePins, nodeFun, nodeStart, nodeSingle, sceneType, true));
    }

    private Pin objectToPin(NativeObject object) {
        String name = (String) object.get("name");
        Connection connection = (Connection) object.get("connection");
        Object type = object.get("type");

        DataType[] types = null;
        if (type instanceof DataType)
            types = new DataType[]{(DataType) type};
        else {
            NativeArray na = (NativeArray) type;
            types = Arrays.copyOf(na.toArray(), na.size(), DataType[].class);
        }

        return new Pin(null, name, connection, types);
    }

    public Set<String> getNodeList() {
        return handles.keySet();
    }

    public Node createNode(String type) {
        return new PluginNode(new PluginNodeHandle(handles.get(type)));
    }

    public PluginNodeHandle getNodeHandle(String type) {
        return new PluginNodeHandle(handles.get(type));
    }

    private static Scriptable getScope() {
        return scope;
    }

    private static Context getContext() {
        return rhino;
    }


    public static class PluginNodeHandle {
        public final String title;
        public final String name;
        public final Pin[] pins;
        public final ScriptFun function;

        public final boolean start;
        public final boolean single;
        public final String sceneType;
        public final boolean closable;

        public PluginNodeHandle(String name, String title, Pin[] pins, ScriptFun function, boolean start, boolean single, String sceneType, boolean closable) {
            this.name = name;
            this.title = title;
            this.pins = pins;
            this.function = function;
            this.start = start;
            this.single = single;
            this.sceneType = sceneType;
            this.closable = closable;
        }

        public PluginNodeHandle(PluginNodeHandle other) {
            this.name = other.name;
            this.title = other.title;
            this.start = other.start;
            this.single = other.single;
            this.sceneType = other.sceneType;
            this.closable = other.closable;

            this.pins = new Pin[other.pins.length];

            for (int i = 0; i < pins.length; ++i) {
                pins[i] = new Pin(other.pins[i]);
            }

            this.function = other.function;
        }
    }

    public static class ScriptFun {
        private Function function;

        ScriptFun(Function function) {
            this.function = function;
        }

        public Object call(Object... args) {
            return Context.jsToJava(function.call(getContext(), getScope(), getScope(), args), Object.class);
        }
    }

}
