package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.ui.FunctionTable;
import com.littleinferno.flowchart.ui.VariableTable;

public class Builder {

    Builder() {
    }

    static public String createAdd(String left, String right) {
        return create(left, right, "+");
    }

    static public String createSub(String left, String right) {
        return create(left, right, "-");
    }

    static public String createMul(String left, String right) {
        return create(left, right, "*");
    }

    static public String createDiv(String left, String right) {
        return create(left, right, "/");
    }

    static public String createEq(String left, String right) {
        return create(left, right, "==");
    }

    static public String createLt(String left, String right) {
        return create(left, right, "<");
    }

    static public String createGt(String left, String right) {
        return create(left, right, ">");
    }

    static private String create(String left, String right, String operator) {
        return String.format("%s %s %s", left, operator, right);
    }


    static public String genFun() {
        return FunctionTable.gen();
    }
    static public String genVar() {
        return VariableTable.gen();
    }

}
