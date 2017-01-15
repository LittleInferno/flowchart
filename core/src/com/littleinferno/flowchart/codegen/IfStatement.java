package com.littleinferno.flowchart.codegen;


public class IfStatement implements Statement {
    private Expression condition;
    private Statement ifStatement;
    private Statement elseStatement;

    public IfStatement(Expression condition, Statement ifStatement, Statement elseStatement) {
        this.condition = condition;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void execute() {
        if (condition.eval().asBool()) {
            ifStatement.execute();
        } else if (elseStatement != null) {
            elseStatement.execute();
        }
    }
}
