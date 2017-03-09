package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.List;

public interface BaseCodeGenerator {

    String EMPTY_EXPRESSION = "";

    String makeString(String str);

    String makeExpr(String str);

    String makeVariable(String variableName, DataType type, boolean isArray);

    String makeVariable(final String name, String value);

    String makeVariable(final String variable, final Pin value);

    String makeAdd(final Pin left, final Pin right);

    String makeSub(final Pin left, final Pin right);

    String makeMul(final Pin left, final Pin right);

    String makeDiv(final Pin left, final Pin right);

    String makeEq(final Pin left, final Pin right);

    String makeLt(final Pin left, final Pin right);

    String makeGt(final Pin left, final Pin right);

    String makeLtEq(final Pin left, final Pin right);

    String makeGtEq(final Pin left, final Pin right);

    String makeAssign(final Pin left, final Pin right);

    String makeAssign(String left, String right);

    String makeFunction(final String name, final String params, final BaseBlock body);

    String makeParams(final List<String> params);

    String makeReturn(List<Pin> pins);

    String makeReturn(final Pin ret);

    String makeCall(final String function, final List<String> params, final String result);

    String makeNamedValue(final String name);

    String makeArray(final ArrayList<Pin> values);

    String makeAddArrayItem(final Pin array, final Pin value);

    String makeGetArrayLength(final String array);

    String makeGetArrayItem(final String array, final String item);

    String makeSetArrayItem(final String array, final String item, String index);

    String makeStatement(String expression);

    String makeIf(Pin condition, Pin block, Pin next);

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
