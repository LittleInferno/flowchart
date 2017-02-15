package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JSCodeGenerator implements BaseCodeGenerator {

    private Map<String, Integer> values;

    private Stack<String> previousExpressions;

    public JSCodeGenerator() {
        values = new HashMap<>();
        previousExpressions = new Stack<>();
    }

    @Override
    public String makeString(String str) {
        return new StringBuilder().append("\n").append(str).append("\"").toString();
    }

    @Override
    public String makeExpr(String str) {
        previousExpressions.push(str);
        return str;
    }


    @Override
    public String makeVariable(String variableName, DataType type, boolean isArray) {
        return makeStatement("var " + makeAssign(variableName,
                isArray ? "[]" : type.getDefaultValue()));
    }

    @Override
    public String makeVariable(final String name, final String value) {
        return makeStatement("var " + makeAssign(name, value));
    }

    @Override
    public String makeAdd(Pin left, Pin right) {
        gen(left);
        gen(right);

        return createBinary("+");
    }

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
    public String makeAssign(String left, String right) {
        return left + " = " + right;
    }

    @Override
    public String makeFunction(final String name, final String params, final BaseBlock body) {
        return String.format("function %s(%s)%s", name, params, body);
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
        List<String> packBuild = new ArrayList<>(ret.size());

        for (Map.Entry<String, String> i : ret.entrySet()) {

            returnBuilder.append(makeVariable(i.getKey(), i.getValue()));

            packBuild.add(i.getKey());
        }

        return returnBuilder.append(packParameters(packBuild)).toString();
    }

    @Override
    public String makeReturn(String ret) {
        return makeStatement("return " + ret);
    }

    private String makeReturn(BaseBlock block) {
        return makeReturn(block.toString());
    }

    @Override
    public String makeCall(final String function, final List<String> params, final String result) {
        String parameters = makeParams(params);
        String funCall = String.format("%s(%s)", function, parameters);
        return makeVariable(result, funCall);

    }

    @Override
    public String makeNamedValue(final String name) {
        if (values.containsKey(name)) {
            Integer integer = values.get(name);
            values.put(name, ++integer);
            return name + integer;
        }

        values.put(name, 0);
        return name + "0";
    }

    @Override
    public String makeArray(final ArrayList<Pin> values) {

        StringBuilder arrayBuilder = new StringBuilder();
        arrayBuilder.append('[');

        for (int i = 0; i < values.size() - 1; i++)
            arrayBuilder.append(values.get(i)).append(',');

        arrayBuilder.append(values.get(values.size() - 1));

        arrayBuilder.append(']');

        return makeExpr(arrayBuilder.toString());
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
    public String makeAddArrayItem(Pin array, Pin value) {
        gen(value);
        gen(array);

        return previousExpressions.pop() + ".push(" + previousExpressions.pop() + ")";
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

    @Override
    public String makeStatement(String expression) {
        return expression + ";\n";
    }

    @Override
    public String makeIf(String condition, BaseBlock block) {
        return "if(" + condition + ')' + block;
    }

    @Override
    public String makeElse(BaseBlock block) {
        return "else " + block;
    }

    @Override
    public String makeElseIf(String condition, BaseBlock block) {
        return "else " + makeIf(condition, block);
    }

    @Override
    public String makeIfElse(String condition, BaseBlock ifBlock, BaseBlock elseBlock) {
        return makeIf(condition, ifBlock) + makeElse(elseBlock);
    }

    private String createBinary(final String left, final String right, final String operator) {
        String s = "(" + left + operator + right + ")";

        previousExpressions.push(s);

        return s;
    }

    private String createBinary(final String operator) {
        String s = "(" + previousExpressions.pop() +
                operator + previousExpressions.pop() + ")";

        previousExpressions.push(s);

        return s;
    }

    private String packParameters(final List<String> parameters) {
        StringBuilder parameterPack = new StringBuilder();

        for (int i = 0; i < parameters.size() - 1; i++) {
            String str = parameters.get(i);
            parameterPack.append(str).append(':').append(str).append(',');
        }

        String str = parameters.get(parameters.size() - 1);
        parameterPack.append(str).append(':').append(str);


        return makeReturn(makeBlock(parameterPack.toString()));
    }

    public JSBlock makeBlock(String body) {
        return new JSBlock(body);
    }

    private static class JSBlock extends BaseBlock {

        private final String body;

        JSBlock(String body) {
            this.body = "{\n" + body + "}\n";
        }

        @Override
        public String getBody() {
            return body;
        }
    }


    private void gen(final Pin pin) {
        Pin connectedPin = pin.getConnectedPin();
        connectedPin.getNode().gen(this, connectedPin);
    }
}
