package com.littleinferno.flowchart.codegen;


import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class CodeExecution {

    public void run(String code) {



        Context rhino = Context.enter();

        rhino.setOptimizationLevel(-1);
        String result;
        try {
            Scriptable scope = rhino.initStandardObjects();
            rhino.evaluateString(scope, code, "JavaScript", 1, null);

        } finally {
            Context.exit();
        }
    }

}
