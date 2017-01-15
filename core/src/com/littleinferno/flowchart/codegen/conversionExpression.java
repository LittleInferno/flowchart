package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.value.Value;

public class ConversionExpression implements Expression {

    Expression expression;

    public ConversionExpression(Expression expression) {
        this.expression = expression;
    }

    @Override

    public Value eval() {
        return expression.eval();
    }
}
