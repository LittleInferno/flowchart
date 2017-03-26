package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Optional;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodePluginHandle extends BasePluginHandle {

    private final Context rhino;
    private final Scriptable scope;
    private final List<PluginNodeHandle> handles;

    public final int apiVersion = 200;

    public NodePluginHandle(File pluginFile) {
        super(pluginFile);

        if (getPluginParams().getApiVersion() < apiVersion)
            throw new RuntimeException("plugin api(" +
                    getPluginParams().getApiVersion() +
                    ") < plugin handle api(" + apiVersion + ")");

        rhino = Context.enter();
        rhino.setOptimizationLevel(-1);

        scope = rhino.initStandardObjects();

        handles = new ArrayList<>();
    }

    @Override
    public void onLoad() {

        String str = null;
        try {
            str = readFile(getPluginParams().getSrcFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        rhino.evaluateString(scope, str, "JavaScript", 1, null);
        Function fct = (Function) scope.get("exportNodes", scope);
        NativeArray result = (NativeArray) fct.call(rhino, scope, scope, new Object[]{});
        registerNodes(result);
    }

    private void registerNodes(NativeArray object) {
        for (Object i : object) registerNode((ScriptableObject) i);
    }

    public void registerNode(ScriptableObject object) {
        PluginNodeHandle nodeHandle = new PluginNodeHandle(this);

        nodeHandle.name = (String) object.get("name");
        nodeHandle.title = (String) object.get("title");

        nodeHandle.codegen = new ScriptFun(rhino, scope, (Function) object.get("gen"));
        nodeHandle.init = new ScriptFun(rhino, scope, (Function) object.get("init"));

        //noinspection unchecked
        NativeArray attributes = (NativeArray) object.get("attributes");

        if (attributes != null) {
            for (Object o : attributes) {
                addAttrib(nodeHandle, (NativeObject) o);
            }
        }

        handles.add(nodeHandle);
    }

    private void addAttrib(PluginNodeHandle pluginNodeHandle, NativeObject object) {
        String name = (String) object.get(0);
        Object attr = object.get(1);
        pluginNodeHandle.addAttribute(name, attr);
    }

    @Override
    public void onUnload() {
        Context.exit();
    }

    public List<PluginNodeHandle> getNodes() {
        return handles;
    }

    public static class PluginNodeHandle {
        String title;
        String name;
        ScriptFun codegen;
        ScriptFun init;

        private Map<String, Object> attributes;

        private final NodePluginHandle nodePluginHandle;

        PluginNodeHandle(NodePluginHandle nodePluginHandle) {
            this.nodePluginHandle = nodePluginHandle;
            this.attributes = new HashMap<>();
        }

        public void addAttribute(String key, Object attr) {
            attributes.put(key, attr);
        }

        public Optional<Object> getAttribute(String key) {
            return containsAttribute(key) ? Optional.of(attributes.get(key)) : Optional.empty();
        }

        public boolean containsAttribute(String key) {
            return attributes.containsKey(key);
        }

        public String getTitle() {
            return title;
        }

        public String getName() {
            return name;
        }

        public ScriptFun getCodegen() {
            return codegen;
        }

        public ScriptFun getInit() {
            return init;
        }
    }

    public static class ScriptFun {
        private final Context context;
        private final Scriptable scope;
        private final Function function;

        ScriptFun(Context context, Scriptable scope, Function function) {
            this.context = context;
            this.scope = scope;
            this.function = function;
        }

        public Object call(Object... args) {
            if (function != null)
                return Context.jsToJava(function.call(context, scope, scope, args), Object.class);
            return null;
        }
    }
}
