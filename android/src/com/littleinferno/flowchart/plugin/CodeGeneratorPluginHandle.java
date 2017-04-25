package com.littleinferno.flowchart.plugin;

import com.annimon.stream.Stream;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.util.regex.Pattern;

public class CodeGeneratorPluginHandle extends AndroidBasePluginHandle {

    private CodeGenerator codeGenerator;

    public CodeGeneratorPluginHandle(final String plugin) {
        super(plugin);

        readRules((ScriptableObject) createScriptFun("exportRules").call());
    }

    private void readRules(ScriptableObject exportRules) {
        final String words[] = (String[]) Context.jsToJava(exportRules.get("keyWords"), String[].class);
        final boolean variableAvailable = (boolean) exportRules.get("variableIsAvailable");
        final boolean functionAvailable = (boolean) exportRules.get("functionIsAvailable");

        final String p = (String) exportRules.get("pattern");
        final Pattern pattern = Pattern.compile(p);

        codeGenerator = new CodeGenerator(words, variableAvailable, functionAvailable, pattern);
    }

    @Override
    public void onUnload() {

    }

    @Override
    public int getApiVersion() {
        return 200;
    }

    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    public static class CodeGenerator {
        final String[] keyWords;
        final boolean variableIsAvailable;
        final boolean functionIsAvailable;
        final Pattern pattern;

        public CodeGenerator(String[] keyWords, boolean variableIsAvailable, boolean functionIsAvailable, Pattern pattern) {
            this.keyWords = keyWords;
            this.variableIsAvailable = variableIsAvailable;
            this.functionIsAvailable = functionIsAvailable;
            this.pattern = pattern;
        }

        public boolean containsWord(final String word) {
            return Stream.of(keyWords).anyMatch(word::equals);
        }

        public boolean checkPattern(final String word) {
            return pattern.matcher(word).matches();
        }
    }

}
