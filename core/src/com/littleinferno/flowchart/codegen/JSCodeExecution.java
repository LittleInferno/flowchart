package com.littleinferno.flowchart.codegen;


import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class JSCodeExecution extends BaseCodeExecution{

    private Context rhino;

    @Override
    public void init() {
        rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
    }

    @Override
    public void run() {
        Scriptable scope = rhino.initStandardObjects();
        rhino.evaluateString(scope, getCode(), "JavaScript", 1, null);

    }

    @Override
    public void stop() {
        Context.exit();
    }
}