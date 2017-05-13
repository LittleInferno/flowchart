package com.littleinferno.flowchart.function;

public class MainFunction extends AndroidFunction {

    MainFunction(AndroidFunctionManager functionManager) {
        super(functionManager,
                functionManager.getProject().getPluginManager().getRules().getEntryPoint());
    }
}
