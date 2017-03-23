package com.littleinferno.flowchart.gui.menu;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.littleinferno.flowchart.gui.menu.openproject.ProjectsTable;
import com.littleinferno.flowchart.util.EventWrapper;

public class Menu extends Stage {

    private final VisTable main;

    private final VisTextButton createProject;
    private final VisTextButton loadProject;
    private final VisTextButton plugins;

    public Menu() {

        VisUI.load("X1/uiskin.json");

        main = new VisTable();
        main.setFillParent(true);

        createProject = new VisTextButton("create new");
        loadProject = new VisTextButton("load");
        plugins = new VisTextButton("plugins");

        ButtonGroup<VisTextButton> buttonGroup = new ButtonGroup<>(createProject, loadProject);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(createProject);
        verticalGroup.addActor(loadProject);
        verticalGroup.addActor(plugins);

        main.add(verticalGroup).left().growY();//.row();

        addActor(main);

        Stack stack = new Stack();
        ProjectsTable projectsTable = new ProjectsTable();
        projectsTable.setVisible(false);
        stack.add(projectsTable);

        main.add(stack).grow();

        createProject.addListener(new EventWrapper((event, actor) -> {

        }));


        loadProject.addListener(new EventWrapper((event, actor) -> {
            projectsTable.setVisible(loadProject.isChecked());

        }));

    }


    @Override
    public void dispose() {
        super.dispose();
    }
}
