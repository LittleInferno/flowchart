package com.littleinferno.flowchart.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.littleinferno.flowchart.Application;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 900;
        config.height = 600;
        config.samples = 4;
        new LwjglApplication(new Application(null, null), config);
    }
}
