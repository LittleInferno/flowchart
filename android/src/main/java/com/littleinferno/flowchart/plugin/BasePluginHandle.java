package com.littleinferno.flowchart.plugin;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public abstract class BasePluginHandle {

    private PluginParams pluginParams;
    private Context rhino;
    private Scriptable scope;

    BasePluginHandle(PluginParams params, String plugin) {
        this.pluginParams = params;
        rhino = ContextFactory.getGlobal().enterContext();
        rhino.setOptimizationLevel(-1);

        scope = rhino.initStandardObjects();
        rhino.evaluateString(scope, plugin, "JavaScript", 1, null);
    }

    public PluginParams getPluginParams() {
        return pluginParams;
    }

    public void unload() {
        onUnload();
        Context.exit();
    }

    protected abstract void onUnload();

    public abstract int getApiVersion();

    public ScriptFun createScriptFun(String name) {
        return new ScriptFun(rhino, scope, name);
    }

    public ScriptFun createScriptFun(Function function) {
        return new ScriptFun(rhino, scope, function);
    }

    public static class PluginParams {
        private final String pluginName;
        private final String pluginDescription;
        private final String pluginVersion;
        private final String apiVersion;

        public PluginParams(String pluginName, String pluginDescription, String pluginVersion, String apiVersion) {
            this.pluginName = pluginName;
            this.pluginDescription = pluginDescription;
            this.pluginVersion = pluginVersion;
            this.apiVersion = apiVersion;
        }

        public String getPluginName() {
            return pluginName;
        }

        public String getPluginDescription() {
            return pluginDescription;
        }

        public String getPluginVersion() {
            return pluginVersion;
        }

        public String getApiVersion() {
            return apiVersion;
        }
    }

    public static class ScriptFun {
        private final Context context;
        private final Scriptable scope;
        private final Function function;

        public ScriptFun(final Context context, final Scriptable scope, final String functionName) {
            this.context = context;
            this.scope = scope;
            function = (Function) scope.get(functionName, scope);
        }

        ScriptFun(final Context context, final Scriptable scope, final Function function) {
            this.context = context;
            this.scope = scope;
            this.function = function;
        }

        public <T> T call(Class<T> type, Object... args) {
            return type.cast(Context.jsToJava(function.call(context, scope, scope, args), type));
        }

        public Object call(Object... args) {
            return Context.jsToJava(function.call(context, scope, scope, args), Object.class);
        }
    }
}
