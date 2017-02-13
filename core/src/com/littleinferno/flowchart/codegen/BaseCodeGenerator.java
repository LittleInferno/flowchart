package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.DataType;

import java.util.List;
import java.util.Map;

public interface BaseCodeGenerator {

    String makeString(String str);

    String makeVariable(String variableName, DataType type, boolean isArray);

    String makeVariable(final String name, String value);

    String makeAdd(final String left, final String right);

    String makeSub(final String left, final String right);

    String makeMul(final String left, final String right);

    String makeDiv(final String left, final String right);

    String makeEq(final String left, final String right);

    String makeLt(final String left, final String right);

    String makeGt(final String left, final String right);

    String makeLtEq(final String left, final String right);

    String makeGtEq(final String left, final String right);

    String makeAssign(String left, String right);

    String makeFunction(final String name, final String params, final BaseBlock body);

    String makeParams(final List<String> params);

    String makeReturn(final Map<String, String> ret);

    String makeReturn(final String ret);

    String makeCall(final String function, final List<String> params, final String result);

    String makeNamedValue(final String name);

    String makeArray(final List<String> values);

    String makeAddArrayItem(final String array, final String value);

    String makeGetArrayLength(final String array);

    String makeGetArrayItem(final String array, final String item);

    String makeStatement(String expression);

    String makeIf(String condition, BaseBlock block);

    String makeElse(BaseBlock block);

    String makeElseIf(String condition, BaseBlock block);

    String makeIfElse(String condition, BaseBlock ifBlock, BaseBlock elseBlock);

    BaseBlock makeBlock(String body);

    abstract class BaseBlock {
        abstract String getBody();

        @Override
        public String toString() {
            return getBody();
        }
    }
}
