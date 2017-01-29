package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


class ControlTable extends Table {
    ControlTable(Skin skin) {

        setWidth(200);
        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("VarTable.png")), 1, 1, 1, 1);
      //  setBackground(new NinePatchDrawable(patch));

        top();
        Table tabTable = new Table();

        final Button components = new TextButton("Components", skin);
        final Button variables = new TextButton("Variables", skin);
        final Button functions = new TextButton("Functions", skin);

        ButtonGroup<Button> tabs = new ButtonGroup<Button>();
        tabs.setMinCheckCount(1);
        tabs.setMaxCheckCount(1);
        tabs.add(components);
        tabs.add(variables);
        tabs.add(functions);

        tabTable.add(components).fill().expand();
        tabTable.add(variables).fill().expand();
        tabTable.add(functions).fill().expand();

        add(tabTable).fillX().expandX().height(30);
        row();

        Stack mainContainer = new Stack();
        final NodeTable componentsTable = new NodeTable(skin);

        mainContainer.add(componentsTable);

        final Table container = new Table();

        Button createNewElement = new TextButton("Create new", skin);
        container.add(createNewElement).fillX().expandX().height(30).row();

        mainContainer.add(container);

        final VariableTable variableTable = new VariableTable(skin);
        final com.littleinferno.flowchart.ui.FunctionTable functionTable = new com.littleinferno.flowchart.ui.FunctionTable(skin);

        Stack contents = new Stack();
        contents.add(variableTable);
        contents.add(functionTable);

        container.add(contents).fill().expand();
        mainContainer.add(container);
        add(mainContainer).fill().expand();

        ChangeListener tabListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                componentsTable.setVisible(components.isChecked());
                container.setVisible(!components.isChecked());

                variableTable.setVisible(variables.isChecked());
                functionTable.setVisible(functions.isChecked());
            }
        };

        components.addListener(tabListener);
        variables.addListener(tabListener);
        functions.addListener(tabListener);

        createNewElement.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (variables.isChecked()) {
                    variableTable.addComponent();
                } else if (functions.isChecked()) {
                    functionTable.addComponent();
                }
            }
        });

        variables.setChecked(true);
        functionTable.setVisible(false);

    }
}
