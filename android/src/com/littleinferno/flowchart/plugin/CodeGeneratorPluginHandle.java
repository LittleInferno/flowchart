package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Stream;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

public class CodeGeneratorPluginHandle extends AndroidBasePluginHandle {

    private CodeGenerator codeGenerator;

    public CodeGeneratorPluginHandle(final String plugin) {
        super(plugin);

        readRules((ScriptableObject) createScriptFun("exportRules").call());
    }

    private void readRules(ScriptableObject exportRules) {
        String words[] = (String[]) Context.jsToJava(exportRules.get("keyWords"), String[].class);
        boolean variableAvailable = (boolean) exportRules.get("variableIsAvailable");
        boolean functionAvailable = (boolean) exportRules.get("functionIsAvailable");

        codeGenerator = new CodeGenerator(words, variableAvailable, functionAvailable);
    }

    @Override
    public void onUnload() {

    }

    @Override
    public int getApiVersion() {
        return 100;
    }

    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    public static class CodeGenerator {
        final String[] keyWords;
        final boolean variableIsAvailable;
        final boolean functionIsAvailable;

        public CodeGenerator(String[] keyWords, boolean variableIsAvailable, boolean functionIsAvailable) {
            this.keyWords = keyWords;
            this.variableIsAvailable = variableIsAvailable;
            this.functionIsAvailable = functionIsAvailable;
        }

        public boolean containsWord(final String word) {
            return Stream.of(keyWords).anyMatch(word::equals);
        }
    }

}
