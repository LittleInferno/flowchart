package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.value.Value;

public class Function {

    protected Statement body;

    public Function(Statement body) {
        this.body = body;
    }

    public Value execute(Value... input){
        body.execute();

        return null;
    }
}
