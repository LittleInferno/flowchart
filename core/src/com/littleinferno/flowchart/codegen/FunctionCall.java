package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.value.Value;

import java.util.List;

public class FunctionCall implements Expression, Statement {

    private List<Expression> args;
    private String name;

    public FunctionCall(String name) {
        this.name = name;
        this.args = null;
    }

    public FunctionCall(String name, List<Expression> args) {
        this.args = args;
        this.name = name;
    }

    @Override
    public Value eval() {

        Value[] values = new Value[args.size()];
        for (int i = 0; i < args.size(); ++i) {
            values[i] = args.get(i).eval();
        }

        return Functions.get(name).execute(values);
    }

    @Override
    public void execute() {
        eval();
    }
}
