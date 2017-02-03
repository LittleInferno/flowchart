package com.littleinferno.flowchart.codegen;

import java.util.List;
import java.util.Map;

public interface BaseBackendGenerator {

    String makeAdd(final String left, final String right);

    String makeSub(final String left, final String right);

    String makeMul(final String left, final String right);

    String makeDiv(final String left, final String right);

    String makeEq(final String left, final String right);

    String makeLt(final String left, final String right);

    String makeGt(final String left, final String right);

    String makeLtEq(final String left, final String right);

    String makeGtEq(final String left, final String right);

    String makeFunction(final String name, final String params, final String body);

    String makeParams(final List<String> params);

    String makeReturn(final Map<String, String> ret);

    String makeCall(final String function, final List<String> params, final String result);

    String makeVariable(final String name, String defaultValue);

    String makeNamedValue(final String name);

    String makeArray(final List<String> values);

    String makeAddArrayItem(final String array, final String value);

    String makeGetArrayLength(final String array);

    String makeGetArrayItem(final String array, final String item);
}
