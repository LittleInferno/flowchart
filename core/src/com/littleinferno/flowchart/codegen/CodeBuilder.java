package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.ui.FunctionTable;

import java.util.List;
import java.util.Map;

public class CodeBuilder {

    private BaseBackendGenerator codeBuilderBackend;

    public CodeBuilder(BaseBackendGenerator codeBuilderBackend) {
        this.codeBuilderBackend = codeBuilderBackend;
    }

    public BaseBackendGenerator getCodeBuilderBackend() {
        return codeBuilderBackend;
    }

    public String createAdd(String left, String right) {
        return codeBuilderBackend.makeAdd(left, right);
    }

    public String createSub(String left, String right) {
        return codeBuilderBackend.makeSub(left, right);
    }

    public String createMul(String left, String right) {
        return codeBuilderBackend.makeMul(left, right);
    }

    public String createDiv(String left, String right) {
        return codeBuilderBackend.makeDiv(left, right);
    }

    public String createEq(String left, String right) {
        return codeBuilderBackend.makeEq(left, right);
    }

    public String createLt(String left, String right) {
        return codeBuilderBackend.makeLt(left, right);
    }

    public String createGt(String left, String right) {
        return codeBuilderBackend.makeGt(left, right);
    }

    public String createLtEq(final String left, final String right) {
        return codeBuilderBackend.makeLtEq(left, right);
    }

    public String createGtEq(final String left, final String right) {
        return codeBuilderBackend.makeGtEq(left, right);
    }

    public String createParams(final List<String> params) {
        return codeBuilderBackend.makeParams(params);
    }

    public String createFunction(final String name, final String params, final String body) {
        return codeBuilderBackend.makeFunction(name, params, body);
    }

    public String createReturn(final Map<String, String> ret) {
        return codeBuilderBackend.makeReturn(ret);
    }

    public String createNamedValue(final String name) {
        return codeBuilderBackend.makeNamedValue(name);
    }

    public String createCall(String function, List<String> params, String result) {
        return codeBuilderBackend.makeCall(function, params, result);
    }

    public String createArray(final List<String> values) {
        return codeBuilderBackend.makeArray(values);
    }

    public String createAddItemToArray(final String array, final String value) {
        return codeBuilderBackend.makeAddArrayItem(array, value);
    }

    public String createGetArrayLength(final String array) {
        return codeBuilderBackend.makeGetArrayLength(array);
    }

    public String createGetArrayItem(final String array, final String item) {
        return codeBuilderBackend.makeGetArrayItem(array, item);
    }

    public String genFun() {
        return FunctionTable.gen(this);
    }
}
