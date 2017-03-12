package com.littleinferno.flowchart.plugin;

public interface BasePlugin {

    String name = "";
    String description = "";
    String version = "";

    boolean init();
    boolean terminate();
}
