package com.littleinferno.flowchart.util.managers;

import com.littleinferno.flowchart.Application;


public abstract class AbstactManager extends BaseManager {

    private final Application app;

    public AbstactManager(Application app){
        this.app = app;
    }

    public Application getApp() {
        return app;
    }
}
