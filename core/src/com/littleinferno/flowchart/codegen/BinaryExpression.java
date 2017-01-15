package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.value.Value;

public class BinaryExpression implements Expression {

    private Expression left;
    private Expression right;
    private Operator operator;

    public enum Operator {
        add,
        sub,
        mul,
        div,
        eq,
        lt,
        gt,
    }

    public BinaryExpression(Expression left, Expression right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }


    @Override
    public Value eval() {

        Value leftValue = left.eval();
        Value rightValue = right.eval();

        switch (operator) {
            case add:
                return Value.add(leftValue, rightValue);
            case sub:
                return Value.sub(leftValue, rightValue);
            case mul:
                return Value.mul(leftValue, rightValue);
            case div:
                return Value.div(leftValue, rightValue);
            case eq:
                return Value.equals(leftValue, rightValue);
            case lt:
                return Value.less(leftValue, rightValue);
            case gt:
                return Value.great(leftValue, rightValue);
        }

        throw new RuntimeException("unknown operator");
    }
}
