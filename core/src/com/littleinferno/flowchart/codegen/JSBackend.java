package com.littleinferno.flowchart.codegen;

import com.badlogic.gdx.utils.StringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSBackend implements BaseBackendGenerator {

    private static int valueCounter = 0;

    @Override
    public String makeAdd(final String left, final String right) {
        return createBinary(left, right, "+");
    }

    @Override
    public String makeSub(final String left, final String right) {
        return createBinary(left, right, "-");
    }

    @Override
    public String makeMul(final String left, final String right) {
        return createBinary(left, right, "*");
    }

    @Override
    public String makeDiv(final String left, final String right) {
        return createBinary(left, right, "/");
    }

    @Override
    public String makeEq(final String left, final String right) {
        return createBinary(left, right, "==");
    }

    @Override
    public String makeLt(final String left, final String right) {
        return createBinary(left, right, "<");
    }

    @Override
    public String makeGt(final String left, final String right) {
        return createBinary(left, right, ">");
    }

    @Override
    public String makeLtEq(final String left, final String right) {
        return createBinary(left, right, "<=");
    }

    @Override
    public String makeGtEq(final String left, final String right) {
        return createBinary(left, right, ">=");
    }

    @Override
    public String makeFunction(final String name, final String params, final String body) {
        return String.format("function %s(%s){\n%s\n}", name, params, body);
    }

    @Override
    public String makeParams(final List<String> params) {
        StringBuilder parameterBuilder = new StringBuilder();
        for (int i = 0, paramsSize = params.size(); i < paramsSize; i++) {
            parameterBuilder.append(params.get(i));

            if (i != paramsSize - 1) parameterBuilder.append(',');
        }
        return parameterBuilder.toString();
    }

    @Override
    public String makeReturn(final Map<String, String> ret) {

        StringBuilder returnBuilder = new StringBuilder();
        List<String> packBuild = new ArrayList<String>(ret.size());

        for (Map.Entry<String, String> i : ret.entrySet()) {

            returnBuilder.append(makeVariable(i.getKey(), i.getValue()));

            packBuild.add(i.getKey());
        }

        return returnBuilder.append(packParameters(packBuild)).toString();
    }

    @Override
    public String makeCall(final String function, final List<String> params, final String result) {
        String parameters = makeParams(params);
        String funCall = String.format("%s(%s)", function, parameters);
        return makeVariable(result, funCall);

    }

    @Override
    public String makeVariable(final String name, final String defaultValue) {
        return String.format("var %s = %s;\n", name, defaultValue);
    }

    @Override
    public String makeNamedValue(final String name) {
        return name + valueCounter++;
    }

    @Override
    public String makeArray(List<String> values) {

        StringBuilder arrayBuilder = new StringBuilder();
        arrayBuilder.append('[');

        for (int i = 0, valuesSize = values.size(); i < valuesSize; i++) {

            arrayBuilder.append(values.get(i));

            if (i != valuesSize - 1) arrayBuilder.append(',');
        }

        arrayBuilder.append(']');

        return arrayBuilder.toString();
    }

    @Override
    public String makeAddArrayItem(final String array, final String value) {
        return String.format("%s.push(%s);\n", array, value);
    }

    @Override
    public String makeGetArrayLength(final String array) {
        return String.format("%s.length", array);
    }

    @Override
    public String makeGetArrayItem(final String array, final String item) {
        return String.format("%s[%s]", array, item);
    }

    private String createBinary(final String left, final String right, final String operator) {
        return String.format("(%s %s %s)", left, operator, right);
    }

    private String packParameters(final List<String> parameters) {
        StringBuilder parameterPack = new StringBuilder();
        parameterPack.append("return {");

        for (String i : parameters) parameterPack.append(i).append(':').append(i);

        return parameterPack.append("};\n").toString();
    }
}
