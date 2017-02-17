package com.littleinferno.flowchart.codegen;

import com.annimon.stream.Stream;
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
        return makeExpr("\n" + str + "\"");
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
    public String makeVariable(String variable, Pin value) {
        generate(value);

        return makeVariable(variable, getPreviousExpression());
    }

    @Override
    public String makeAdd(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary("+");
    }

    @Override
    public String makeSub(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary("+");
    }

    @Override
    public String makeMul(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary("*");
    }

    @Override
    public String makeDiv(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary("/");
    }

    @Override
    public String makeEq(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary("==");
    }

    @Override
    public String makeLt(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary("<");
    }

    @Override
    public String makeGt(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary(">");
    }

    @Override
    public String makeLtEq(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary("<=");
    }

    @Override
    public String makeGtEq(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary(">=");
    }

    @Override
    public String makeAssign(Pin left, Pin right) {
        generate(left);
        generate(right);

        return createBinary("=");
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
    public String makeReturn(List<Pin> pins) {

        Stream.of(pins).forEach(pin -> makeVariable(pin.getName(), pin));

        return packReturn(pins);
    }

    @Override
    public String makeReturn(Pin ret) {
        generate(ret);

        return makeStatement("return " + getPreviousExpression());
    }

    private String makeReturn() {
        return makeStatement("return " + getPreviousExpression());
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

        for (int i = 0; i < values.size() - 1; i++) {
            generate(values.get(i));
            arrayBuilder.append(getPreviousExpression()).append(',');
        }

        generate(values.get(values.size() - 1));
        arrayBuilder.append(getPreviousExpression());

        arrayBuilder.append(']');

        return makeExpr(arrayBuilder.toString());
    }

    @Override
    public String makeAddArrayItem(Pin array, Pin value) {
        generate(value);
        generate(array);

        return makeStatement(getPreviousExpression() + ".push(" + getPreviousExpression() + ")");
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
    public String makeIf(Pin condition, Pin block, Pin next) {
        generate(condition);
        generate(block);

        return null;
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

    private String createBinary(final String operator) {
        String s = "(" + getPreviousExpression() +
                operator + getPreviousExpression() + ")";

        previousExpressions.push(s);

        return s;
    }

    private String packReturn(List<Pin> pins) {

        ReturnBlock returnBlock = new ReturnBlock();

        for (Pin pin : pins) {
            generate(pin);
            returnBlock.addParameter(pin.getName(), getPreviousExpression());
        }

        makeExpr(returnBlock.getBody());

        return makeReturn();
    }

    public JSBlock makeBlock(String body) {
        return new JSBlock(body);
    }


    private void generate(final Pin pin) {
        Pin connectedPin = pin.getConnectedPin();
        connectedPin.getNode().gen(this, connectedPin);
    }

    private String getPreviousExpression() {
        return previousExpressions.pop();
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

    private static class ReturnBlock extends BaseBlock {

        StringBuilder builder;

        ReturnBlock() {
            builder = new StringBuilder();
            builder.append("{");
        }

        void addParameter(String name, String value) {
            if (builder.length() != 1) builder.append(',');

            builder.append(name).append(":").append(value);
        }

        @Override
        String getBody() {
            return builder.append('}').toString();
        }
    }
}
