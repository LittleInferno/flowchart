package com.littleinferno.flowchart.codegen;


public class StatementNext implements Statement {

    private Statement current;
    private Statement next;

    public StatementNext(Statement current, Statement next) {
        this.current = current;
        this.next = next;
    }

    @Override
    public void execute() {
        current.execute();
        next.execute();
    }
}
