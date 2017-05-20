package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PluginHandle extends BasePluginHandle {

    private List<NodeHandle> handles;
    private RuleHandle ruleHandle;

    public PluginHandle(PluginParams pluginParams, String plugin) throws Exception {
        super(pluginParams, plugin);

        handles = new ArrayList<>();

        ruleHandle = readRules((ScriptableObject) createScriptFun("exportRules").call());
        registerNodes((NativeArray) createScriptFun("exportNodes").call());
    }
//        if (params.getApiVersion() != getApiVersion())
//            throw new RuntimeException("PLUGIN_FILE api version(" + params.getApiVersion() + ") != api version" + getApiVersion());

    private RuleHandle readRules(ScriptableObject exportRules) throws Exception {
        final String words[] = (String[]) Context.jsToJava(exportRules.get("keyWords"), String[].class);
        final Boolean variableAvailable = (Boolean) exportRules.get("variableIsAvailable");
        final Boolean functionAvailable = (Boolean) exportRules.get("functionIsAvailable");

        final String pattern = (String) exportRules.get("pattern");

        final String entryPoint = (String) exportRules.get("entryPoint");

        checkRule(words, variableAvailable, functionAvailable, pattern, entryPoint);
        return new RuleHandle(words, variableAvailable, functionAvailable, Pattern.compile(pattern), entryPoint);
    }

    private void checkRule(String[] words, Boolean variableAvailable, Boolean functionAvailable, String pattern, String entryPoint) throws Exception {
        String error = "";
        if (words == null)
            error = "keywords must be defined";
        else if (variableAvailable == null)
            error = "variableAvailable must be defined";
        else if (functionAvailable == null)
            error = "functionAvailable must be defined";
        else if (pattern == null)
            error = "pattern must be defined";
        else if (entryPoint == null)
            error = "entryPoint must be defined";

        if (!error.isEmpty())
            throw new Exception(error);
    }

    private void registerNodes(NativeArray object) throws Exception {
        for (Object i : object) registerNode((ScriptableObject) i);
    }

    private void registerNode(ScriptableObject object) throws Exception {

        String name = (String) object.get("name");
        String title = (String) object.get("title");
        String category = (String) object.get("category");

        ScriptFun codegen = createScriptFun((Function) object.get("gen"));
        ScriptFun init = createScriptFun((Function) object.get("init"));

        checkNode(name, title, category, codegen, init);

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

    private void checkNode(String name, String title, String category, ScriptFun codegen, ScriptFun init) throws Exception {
        String error = "";
        if (name == null)
            error = "name must be defined";
        else if (title == null)
            error = "title must be defined";
        else if (category == null)
            error = "category must be defined";
        else if (codegen == null)
            error = "codegen function must be defined";
        else if (init == null)
            error = "init function must be defined";

        if (!error.isEmpty())
            throw new Exception(error);
    }

    @Override
    public void onUnload() {

    }

    @Override
    public int getApiVersion() {
        return 500;
    }

    public List<NodeHandle> getNodes() {
        return handles;
    }

    public RuleHandle getRules() {
        return ruleHandle;
    }

    public static class NodeHandle {
        final String name;
        final String title;
        final String category;
        final ScriptFun codegen;
        final ScriptFun init;
        final PluginHandle nodePluginHandle;
        private Map<String, Object> attributes;


        NodeHandle(PluginHandle nodePluginHandle, final String name, final String title,
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

        public String getCategory() {
            return category;
        }

        public ScriptFun getCodegen() {
            return codegen;
        }

        public ScriptFun getInit() {
            return init;
        }

        public PluginHandle getPluginHandle() {
            return nodePluginHandle;
        }
    }

    public static class RuleHandle {
        final String[] keyWords;
        final boolean variableIsAvailable;
        final boolean functionIsAvailable;
        final Pattern pattern;
        final String entryPoint;

        RuleHandle(String[] keyWords, boolean variableIsAvailable, boolean functionIsAvailable, Pattern pattern, String entryPoint) {
            this.keyWords = keyWords;
            this.variableIsAvailable = variableIsAvailable;
            this.functionIsAvailable = functionIsAvailable;
            this.pattern = pattern;
            this.entryPoint = entryPoint;
        }

        public boolean containsWord(final String word) {
            return Stream.of(keyWords).anyMatch(word::equals);
        }

        public boolean checkPattern(final String word) {
            return pattern.matcher(word).matches();
        }

        public String getEntryPoint() {
            return entryPoint;
        }
    }
}
