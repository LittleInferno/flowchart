package com.littleinferno.flowchart.codegen;


import java.util.HashMap;
import java.util.Map;

public class PluginCodeGenerator {

    private Map<String, BinaryOperator> binaryOperators = new HashMap<>();

    public void addBinaryOperator(BinaryOperator operator) {
        binaryOperators.put(operator.getOperator(), operator);
    }


    String generateBinaryExpression(String leftExpr, String rightExpr, String operator) {
        if (binaryOperators.containsKey(operator))
            throw new IllegalArgumentException("cannot find operator:" + operator);

        return binaryOperators.get(operator).call(leftExpr, rightExpr);
    }

    String generateExpression(String expr) {
        return expr;
    }

    private static String parenthesize(String text) {
        return "(" + text + ")";
    }

    static String space = " ";

    public static class BinaryOperator {

        private final String operator;

        BinaryOperator(String operator) {
            this.operator = operator;
        }

        String call(String left, String right) {
            return parenthesize(left + space + operator + space + right);
        }

        public String getOperator() {
            return operator;
        }
    }
}
