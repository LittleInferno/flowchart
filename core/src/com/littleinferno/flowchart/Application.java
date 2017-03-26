package com.littleinferno.flowchart;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;
import com.littleinferno.flowchart.codegen.BaseJSEngine;
import com.littleinferno.flowchart.codegen.JSCodeExecution;
import com.littleinferno.flowchart.codegen.JSCodeGenerator;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.screen.ScreenManager;
import com.littleinferno.flowchart.util.Details;


public class Application extends Game {

    private Screen scene;
    private static boolean change;

    private Project project;
    private ScreenManager sm;
    private final float SCALE = 1.f;
    public static BaseJSEngine jsEngine = null;

    public static Details.VariableHelper variableHelper;
    public static Details.FunctionHelper functionHelper;

    public Application(Details.VariableHelper variableHelper, Details.FunctionHelper functionHelper) {
        Application.variableHelper = variableHelper;
  //      Application.functionHelper = functionHelper;
    }

    @Override
    public void create() {

        Project.createProject("test", Gdx.files.external("flowchart_projects"), new JSCodeGenerator(), new JSCodeExecution());

       // Project project =
//        Project.load("test", Gdx.files.external("flowchart_projects"));
        setScreen(Project.instance().getProjectScreen());

        // sm = new ScreenManager(this);
    }

    //   @Override
    // public void render() {
    // scene.render(Gdx.graphics.getDeltaTime());
//    }


    @Override
    public void dispose() {
        super.dispose();
//        scene.dispose();
        if (sm != null)
            sm.dispose();
        Project.instance().dispose();
        VisUI.dispose();
    }

    void loadUI() {
        if (!VisUI.isLoaded()) {
            if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android)
                VisUI.load("X2/uiskin.json");
            else
                VisUI.load("X1/uiskin.json");
        }
    }


}
