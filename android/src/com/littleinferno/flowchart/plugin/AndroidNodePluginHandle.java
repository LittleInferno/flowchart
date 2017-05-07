package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Optional;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidNodePluginHandle extends AndroidBasePluginHandle {

    private final List<NodeHandle> handles;

    AndroidNodePluginHandle(String plugin) {
        super(plugin);
        handles = new ArrayList<>();

        registerNodes((NativeArray) createScriptFun("exportNodes").call());
    }

    private void registerNodes(NativeArray object) {
        for (Object i : object) registerNode((ScriptableObject) i);
    }

    private void registerNode(ScriptableObject object) {

        String name = (String) object.get("name");
        String title = (String) object.get("title");
        String category = (String) object.get("category");

        ScriptFun codegen = createScriptFun((Function) object.get("gen"));
        ScriptFun init = createScriptFun((Function) object.get("init"));

        NodeHandle nodeHandle = new NodeHandle(this, name, title, category, codegen, init);

        NativeArray attributes = (NativeArray) object.get("attributes");

        if (attributes != null) {
            for (Object o : attributes) {
                addAttrib(nodeHandle, (NativeObject) o);
            }
        }
        nodeHandle.getAttribute("closable")
                .executeIfAbsent(() -> nodeHandle.addAttribute("closable", true));

        handles.add(nodeHandle);
    }

    private void addAttrib(NodeHandle nodeHandle, NativeObject object) {
        for (Map.Entry<Object, Object> e : object.entrySet()) {
            nodeHandle.addAttribute((String) e.getKey(), e.getValue());
        }
    }

    @Override
    public void onUnload() {

    }

    @Override
    public int getApiVersion() {
        return 400;
    }

    List<NodeHandle> getNodes() {
        return handles;
    }

    public static class NodeHandle {
        final String name;
        final String title;
        final String category;
        final ScriptFun codegen;
        final ScriptFun init;
        final AndroidNodePluginHandle nodePluginHandle;
        private Map<String, Object> attributes;

        NodeHandle(AndroidNodePluginHandle nodePluginHandle, final String name, final String title,
                   final String category, final ScriptFun codegen, final ScriptFun init) {
            this.nodePluginHandle = nodePluginHandle;
            this.name = name;
            this.title = title;
            this.category = category;

            this.codegen = codegen;
            this.init = init;
            attributes = new HashMap<>();
        }

        void addAttribute(final String key, final Object attr) {
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

        public AndroidNodePluginHandle getPluginHandle() {
            return nodePluginHandle;
        }
    }
}
