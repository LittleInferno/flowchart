package com.littleinferno.flowchart;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;
import com.littleinferno.flowchart.codegen.JSCodeExecution;
import com.littleinferno.flowchart.codegen.JSCodeGenerator;
import com.littleinferno.flowchart.project.Project;


public class Flowchart extends Game {

    private Screen scene;
    private static boolean change;

    @Override
    public void create() {

        Project project = Project.createProject("test", "test/", new JSCodeGenerator(), new JSCodeExecution());

        scene = project.getProjectScreen();
        setScreen(scene);
    }

    @Override
    public void dispose() {

        scene.dispose();

        VisUI.dispose();
    }
}
