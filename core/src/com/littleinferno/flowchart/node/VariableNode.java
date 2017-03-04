package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.variable.Variable;

public abstract class VariableNode extends Node {

    protected final Variable variable;

    VariableNode(VariableNodeHandle nodeHandle) {
        super(nodeHandle);
        this.variable = Project.instance().getVariableManager().getVariable(nodeHandle.varName);
    }

    static class VariableNodeHandle extends NodeHandle {
        public String varName;

        public VariableNodeHandle() {
        }

        public VariableNodeHandle(String name, boolean closable, String varName) {
            super(name, closable);
            this.varName = varName;
        }

        public VariableNodeHandle(float x, float y, String className, String id, String varName) {
            super(x, y, className, id, true);
            this.varName = varName;
        }
    }
}
