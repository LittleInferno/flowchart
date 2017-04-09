package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Optional;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidNodePluginHandle extends AndroidBasePluginHandle {

    private final List<NodeHandle> handles;

    public AndroidNodePluginHandle(File pluginFile) {
        super(pluginFile);
        handles = new ArrayList<>();

        registerNodes((NativeArray) createScriptFun("exportNodes").call());
    }

    private void registerNodes(NativeArray object) {
        for (Object i : object) registerNode((ScriptableObject) i);
    }

    public void registerNode(ScriptableObject object) {

        String name = (String) object.get("name");
        String title = (String) object.get("title");
        String category = (String) object.get("category");

        ScriptFun codegen = createScriptFun((Function) object.get("gen"));
        ScriptFun init = createScriptFun((Function) object.get("init"));

        NodeHandle nodeHandle = new NodeHandle(name, title, category, codegen, init);

        NativeArray attributes = (NativeArray) object.get("attributes");

        if (attributes != null) {
            for (Object o : attributes) {
                addAttrib(nodeHandle, (NativeObject) o);
            }
        }

        handles.add(nodeHandle);
    }

    private void addAttrib(NodeHandle nodeHandle, NativeObject object) {
        String name = (String) object.get(0);
        Object attr = object.get(1);
        nodeHandle.addAttribute(name, attr);
    }

    @Override
    public void onUnload() {
        Context.exit();
    }

    @Override
    public int getApiVersion() {
        return 400;
    }

    public List<NodeHandle> getNodes() {
        return handles;
    }

    public static class NodeHandle {
        final String name;
        final String title;
        final String category;
        final ScriptFun codegen;
        final ScriptFun init;

        private Map<String, Object> attributes;

        NodeHandle(final String name, final String title, final String category, final ScriptFun codegen, final ScriptFun init) {
            this.name = name;
            this.title = title;
            this.category = category;

            this.codegen = codegen;
            this.init = init;
            attributes = new HashMap<>();
        }

        public void addAttribute(final String key, final Object attr) {
            attributes.put(key, attr);
        }

        public Optional<Object> getAttribute(final String key) {
            return containsAttribute(key) ? Optional.of(attributes.get(key)) : Optional.empty();
        }

        public boolean containsAttribute(final String key) {
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
}
