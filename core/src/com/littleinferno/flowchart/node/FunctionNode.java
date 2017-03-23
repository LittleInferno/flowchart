package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.project.Project;

import java.util.HashMap;

public abstract class FunctionNode extends Node {

    protected Function function;

    FunctionNode(FunctionNodeHandle nodeHandle) {
        super(nodeHandle);
        this.function = Project.instance().getFunctionManager().getFunction(nodeHandle.funName);
    }

    public Function getFunction() {
        return function;
    }

    static class FunctionNodeHandle extends NodeParams {
        public String funName;

        public FunctionNodeHandle() {
        }

        public FunctionNodeHandle(String name, boolean closable, String funName) {
            super(name, closable);
            this.funName = funName;
        }

        public FunctionNodeHandle(float x, float y, String className, String id, String funName) {
            super(x, y, className, id, true, new HashMap<>());
            this.funName = funName;
        }
    }
}
