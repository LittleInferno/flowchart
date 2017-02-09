package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.littleinferno.flowchart.gui.function.FunctionTable;
import com.littleinferno.flowchart.variable.VariableManager;


public class ControlTable extends VisTable {

    static private ConsoleTable consoleTable;

    public ControlTable() {
        setTouchable(Touchable.enabled);

        final VisTable nodeTable = new NodeTable();
        final VisTable variableTable = new VisTable();

        VisTextButton createVariable = new VisTextButton("create");
        createVariable.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                VariableManager.instance.createVariable();
            }
        });
        variableTable.add(createVariable).growX().row();
        variableTable.add(VariableManager.instance.getVarTable()).grow().row();
        variableTable.addSeparator();
        variableTable.add(VariableManager.instance.getDetailsTable()).growX().row();
        variableTable.addSeparator();


        final VisTable functionTable = new FunctionTable();
        consoleTable = new ConsoleTable();

        final VisTextButton nodes = new VisTextButton("node", "toggle");
        final VisTextButton variables = new VisTextButton("variable", "toggle");
        final VisTextButton functions = new VisTextButton("function", "toggle");
        final VisTextButton console = new VisTextButton("console", "toggle");

        ButtonGroup<VisTextButton> tabs = new ButtonGroup<VisTextButton>();
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
