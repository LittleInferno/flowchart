package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Stream;
import com.badlogic.gdx.files.FileHandle;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.pin.Pin;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodePluginManager {

    private Map<String, PluginHandle> handles;

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
        registerNodes(file.nameWithoutExtension(), result);
    }

    private void registerNodes(String pluginName, NativeArray object) {
        for (Object i : object) registerNode(pluginName, (ScriptableObject) i);
    }

    public void registerNode(String pluginName, ScriptableObject object) {
        PluginNodeHandle nodeHandle = new PluginNodeHandle();

        nodeHandle.name = (String) object.get("name");
        nodeHandle.title = (String) object.get("title");

        Boolean start = (Boolean) object.get("programstart");
        nodeHandle.start = (start != null) ? start : false;

        Boolean single = (Boolean) object.get("single");
        nodeHandle.single = (single != null) ? single : false;

        Boolean closable = (Boolean) object.get("closable");
        nodeHandle.closable = (closable != null) ? closable : true;

        String sceneType = (String) object.get("sceneType");
        if (sceneType == null) sceneType = "any";
        nodeHandle.sceneType = sceneType;

        nodeHandle.codegen = new ScriptFun((Function) object.get("gen"));
        nodeHandle.init = new ScriptFun((Function) object.get("init"));

        //noinspection unchecked
        List<NativeObject> pins = (List<NativeObject>) object.get("pins");

        nodeHandle.pins = Stream.of(pins).map(this::objectToPin).toArray(Pin[]::new);

        if (handles.keySet().contains(pluginName))
            handles.get(pluginName).handles.add(nodeHandle);
        else {
            PluginHandle pluginHandle = new PluginHandle(pluginName);
            pluginHandle.handles.add(nodeHandle);
            handles.put(pluginName, pluginHandle);

        }

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

    public String[] getNodeList() {
        return Stream.of(handles)
                .map(Map.Entry::getValue)
                .flatMap(handle -> Stream.of(handle.getNodeList("any")))
                .toArray(String[]::new);
    }

    public List<PluginHandle> getHandles() {
        return Stream.of(handles).map(Map.Entry::getValue).toList();
    }

    public PluginNodeHandle getNodeHandle(String nodeType, String sceneType) {
        return new PluginNodeHandle(Stream.of(handles)
                .map(Map.Entry::getValue)
                .flatMap(handle -> Stream.of(handle.handles))
                .filter(handle -> handle.name.equals(nodeType))
                .filter(handle -> handle.sceneType.equals(sceneType) || handle.sceneType.equals("any"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannoot find node:" + nodeType + " scene type:" + sceneType)));
    }

    private static Scriptable getScope() {
        return scope;
    }

    private static Context getContext() {
        return rhino;
    }

    public static class PluginHandle {
        final String name;
        final List<PluginNodeHandle> handles;

        PluginHandle(String name) {
            this.name = name;
            this.handles = new ArrayList<>();
        }

        public String[] getNodeList(String sceneType) {
            return Stream.of(handles)
                    .filter(value -> value.sceneType.equals(sceneType))
                    .map(value -> value.name)
                    .toArray(String[]::new);
        }

        public String getName() {
            return name;
        }

        public List<PluginNodeHandle> getHandles(String sceneType) {
            return Stream.of(handles)
                    .filter(value -> value.sceneType.equals(sceneType))
                    .toList();
        }
    }

    public static class PluginNodeHandle {
        public String title;
        public String name;
        public Pin[] pins;
        public ScriptFun codegen;
        public ScriptFun init;

        public boolean start;
        public boolean single;
        public String sceneType;
        public boolean closable;

        PluginNodeHandle() {
        }

        PluginNodeHandle(PluginNodeHandle other) {
            this.name = other.name;
            this.title = other.title;
            this.start = other.start;
            this.single = other.single;
            this.sceneType = other.sceneType;
            this.closable = other.closable;
            this.init = other.init;

            this.pins = new Pin[other.pins.length];

            for (int i = 0; i < pins.length; ++i) {
                pins[i] = new Pin(other.pins[i]);
            }

            this.codegen = other.codegen;
        }
    }

    public static class ScriptFun {
        private Function function;

        ScriptFun(Function function) {
            this.function = function;
        }

        public Object call(Object... args) {
            if (function != null)
                return Context.jsToJava(function.call(getContext(), getScope(), getScope(), args), Object.class);
            return null;
        }
    }

}
