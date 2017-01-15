package com.littleinferno.flowchart.codegen;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class Context {

    ArrayList<Statement> statements;

    public Context() {

    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public void run() {
        for (Statement statement : statements) {
            statement.execute();
        }
    }

}
