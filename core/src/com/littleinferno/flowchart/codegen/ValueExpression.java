package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.value.Value;

public class ValueExpression implements Expression {

    private Value value;

    public ValueExpression(Value value) {
        this.value = value;
    }


    @Override
    public Value eval() {
        return value;
    }
}
