package com.littleinferno.flowchart.util;


import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.variable.Variable;

public interface Details {
    abstract class VariableHelper {
        public abstract void call(Variable variable);
    }

    abstract class FunctionHelper {
        public abstract void call(Function variable);
    }
}
