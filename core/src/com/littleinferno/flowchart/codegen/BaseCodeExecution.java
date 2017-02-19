package com.littleinferno.flowchart.codegen;

public abstract class BaseCodeExecution {
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public abstract void init();

    public abstract void run();

    public abstract void stop();
}
