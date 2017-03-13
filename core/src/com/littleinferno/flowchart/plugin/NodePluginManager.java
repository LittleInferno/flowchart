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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NodePluginManager {

    private Map<String, PluginNodeHandle> handles;
    private PluginNode startNode;

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

        NativeObject pins = (NativeObject) object.get("pins");

        ScriptFun nodeFun = new ScriptFun((Function) object.get("gen"));

        Pin[] nodePins = Stream.of(pins.keySet())
                .map(String.class::cast)
                .map(name -> objectToPin(name, (ScriptableObject) pins.get(name)))
                .toArray(Pin[]::new);

        Boolean programStart = (Boolean) object.get("programstart");

        if (programStart != null && programStart) {
            startNode = new PluginNode(new PluginNodeHandle(nodeName, nodeTitle, nodePins, nodeFun));
            return;
        }

        handles.put(nodeName, new PluginNodeHandle(nodeName, nodeTitle, nodePins, nodeFun));
    }

    private Pin objectToPin(String name, ScriptableObject object) {
        Connection connection = (Connection) object.get("connection");
        Object type = object.get("type");

        DataType[] types = new DataType[1];
        if (type instanceof DataType)
            types[0] = (DataType) type;
        else {
            NativeArray na = (NativeArray) type;
            types = Stream.of(na.toArray()).toArray(DataType[]::new);
        }

        return new Pin(null, name, connection, types);
    }

    public Set<String> getNodeList() {
        return handles.keySet();
    }

    public Node createNode(String type) {
        return new PluginNode(new PluginNodeHandle(handles.get(type)));
    }

    static Scriptable getScope() {
        return scope;
    }

    public static Context getContext() {
        return rhino;
    }

    public PluginNode getStartNode() {
        return startNode;
    }

    public static class PluginNodeHandle {
        public String name;
        public String title;
        public Pin[] pins;
        public ScriptFun function;

        public PluginNodeHandle(String name, String title, Pin[] pins, ScriptFun function) {
            this.name = name;
            this.title = title;
            this.pins = pins;
            this.function = function;
        }

        public PluginNodeHandle(PluginNodeHandle other) {
            this.name = other.name;
            this.title = other.title;
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
