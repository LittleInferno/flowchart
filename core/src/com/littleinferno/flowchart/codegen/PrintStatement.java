package com.littleinferno.flowchart.codegen;


import com.littleinferno.flowchart.ui.Main;

public class PrintStatement implements Statement {

    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }


    @Override
    public void execute() {
        Main.console.appendText(expression.eval().asString() + " ");
    }
}
