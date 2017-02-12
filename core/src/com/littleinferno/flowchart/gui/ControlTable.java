package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;


public class ControlTable extends VisTable {

    static private ConsoleTable consoleTable;

    public ControlTable(Scenetmp sceneUi) {
        setTouchable(Touchable.enabled);

        final VisTable nodeTable = new NodeTable(sceneUi);


        final VisTable variableTable = new VisTable();

        VisTextButton createVariable = new VisTextButton("create");
        createVariable.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sceneUi.getVariableManager().createVariable();
            }
        });
        variableTable.add(createVariable).growX().row();
        variableTable.add(sceneUi.getVariableManager().getVarTable()).grow().row();
        variableTable.addSeparator();
        variableTable.add(sceneUi.getVariableManager().getDetailsTable()).growX().row();
        variableTable.addSeparator();


        final VisTable functionTable = new VisTable();

        VisTextButton createFunction = new VisTextButton("create");
        createFunction.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sceneUi.getFunctionManager().createFunction();
            }
        });
        functionTable.add(createFunction).growX().row();
        functionTable.add(sceneUi.getFunctionManager().getFunTable()).grow().row();
        functionTable.addSeparator();
        functionTable.add(sceneUi.getFunctionManager().getDetailsTable()).growX().height(250).row();
        functionTable.addSeparator();

        consoleTable = new ConsoleTable();

        final VisTextButton nodes = new VisTextButton("node", "toggle");
        final VisTextButton variables = new VisTextButton("variable", "toggle");
        final VisTextButton functions = new VisTextButton("function", "toggle");
        final VisTextButton console = new VisTextButton("console", "toggle");

        ButtonGroup<VisTextButton> tabs = new ButtonGroup<>();
        tabs.setMinCheckCount(1);
        tabs.setMaxCheckCount(1);
        tabs.add(nodes, variables, functions, console);

        ChangeListener tabListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                nodeTable.setVisible(nodes.isChecked());
                variableTable.setVisible(variables.isChecked());
                functionTable.setVisible(functions.isChecked());
                consoleTable.setVisible(console.isChecked());
            }
        };

        nodes.addListener(tabListener);
        variables.addListener(tabListener);
        functions.addListener(tabListener);
        console.addListener(tabListener);

        variables.setChecked(true);

        Stack container = new Stack();
        container.add(nodeTable);
        container.add(variableTable);
        container.add(functionTable);
        container.add(consoleTable);

        VisTable tabTable = new VisTable(true);
        tabTable.add(nodes).grow();
        tabTable.add(variables).grow();
        tabTable.add(functions).grow();
        tabTable.add(console).grow();

        add(tabTable).growX().row();
        addSeparator();
        add(container).grow();

        setBackground(VisUI.getSkin().getDrawable("window-bg"));
    }

    public static ConsoleTable getConsole() {
        return consoleTable;
    }
}
