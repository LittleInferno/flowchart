package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.node.VariableGetNode;
import com.littleinferno.flowchart.node.VariableSetNode;
import com.littleinferno.flowchart.value.Value;


class ControlTable extends Table {

    private class VariableItem extends Table {
        VariableItem(String varName, final Skin skin) {

            variable = new Variable(varName);
            variable.setValueType(Value.Type.BOOL);


            Table title = new Table();

            final Label name = new Label(varName, skin);
            name.setEllipsis(true);
            title.add(name).expand().fill()
                    .width(151);// TODO WTF? Fix it

            final Button set = new TextButton("set", skin);
            title.add(set).size(30).right();

            final Button get = new TextButton("get", skin);
            title.add(get).size(30).right();

            Table property = new Table();

            property.add(new Label("Var name", skin)).expandX().fillX().height(30);
            final TextField nameField = new TextField("", skin);
            property.add(nameField).expandX().fillX().height(30).row();

            property.add(new Label("Data type", skin)).expandX().fillX().height(30);

            final SelectBox<Value.Type> type = new SelectBox<Value.Type>(skin);
            type.setItems(Value.Type.BOOL,
                    Value.Type.FLOAT,
                    Value.Type.INT,
                    Value.Type.STRING);

            property.add(type).expandX().fillX().height(30);

            Tree tree = new Tree(skin);

            Tree.Node titleNode = new Tree.Node(title);
            titleNode.setSelectable(false);
            Tree.Node propertyNode = new Tree.Node(property);
            propertyNode.setSelectable(false);
            titleNode.add(propertyNode);

            tree.add(titleNode);
            add(tree).fillX().expandX();

            nameField.setTextFieldListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
                    variable.setName(nameField.getText());
                    name.setText(nameField.getText());
                }
            });

            type.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    variable.setValueType(type.getSelected());
                }
            });

            Main.getDND().addSource(new DragAndDrop.Source(get) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setObject(new VariableGetNode(new Vector2(200, 200), variable));

                    payload.setDragActor(new Label(String.format("Get %s", variable.getName()), skin));

                    return payload;
                }
            });

            Main.getDND().addSource(new DragAndDrop.Source(set) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setObject(new VariableSetNode(new Vector2(200, 300), variable));

                    payload.setDragActor(new Label(String.format("Set %s", variable.getName()), skin));

                    return payload;
                }
            });

        }

        Variable variable;
    }

    private class FunctionItem extends Table {

        class Parameter extends Table {

            public Parameter(String varName, Function.ParameterType parameterType, Skin skin) {
                final Function.Parameter parameter = function.new Parameter(parameterType, Value.Type.BOOL, varName);
                function.addParameter(parameter);

                add(new Label("name", skin)).expandX().fillX().height(30);
                final TextField nameField = new TextField("", skin);
                add(nameField).expandX().fillX().height(30).row();

                add(new Label("Data type", skin)).expandX().fillX().height(30);

                final SelectBox<Value.Type> type = new SelectBox<Value.Type>(skin);
                type.setItems(Value.Type.BOOL,
                        Value.Type.FLOAT,
                        Value.Type.INT,
                        Value.Type.STRING);

                add(type).expandX().fillX().height(30);

                nameField.setTextFieldListener(new TextField.TextFieldListener() {
                    @Override
                    public void keyTyped(TextField textField, char c) {
                        parameter.setName(nameField.getText());
                    }
                });

                type.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        parameter.setValueType(type.getSelected());
                    }
                });
            }
        }

        FunctionItem(String funName, final Skin skin) {

            function = new Function(funName);

            FunctionWindow functionWindow = new FunctionWindow(funName, skin);
            functionWindow.setPosition(100,100);
            function.setWindow(functionWindow);
            Main.getActivity().addActor(functionWindow);

            Table title = new Table();

            final Label name = new Label(funName, skin);
            name.setEllipsis(true);
            title.add(name).expand().fill()
                    .width(151);// TODO WTF? Fix it

            final Button edit = new TextButton("ed", skin);
            title.add(edit).size(30).right();

            final Button add = new TextButton("add", skin);
            title.add(add).size(30).right();


            Tree tree = new Tree(skin);

            Tree.Node titleNode = new Tree.Node(title);
            titleNode.setSelectable(false);


            Table inputTitle = new Table();

            inputTitle.add(new Label("input", skin)).expand().fill()
                    .width(181);// TODO WTF? Fix it

            final Button addInput = new TextButton("add", skin);
            inputTitle.add(addInput).size(30).right();

            Tree.Node inputTitleNode = new Tree.Node(inputTitle);
            inputTitleNode.setSelectable(false);

            final VerticalGroup inputList = new VerticalGroup();
            Tree.Node inputNode = new Tree.Node(inputList);
            inputNode.setSelectable(false);

            inputTitleNode.add(inputNode);


            Table outputTitle = new Table();

            outputTitle.add(new Label("output", skin)).expand().fill()
                    .width(181);// TODO WTF? Fix it

            final Button addOutput = new TextButton("add", skin);
            outputTitle.add(addOutput).size(30).right();

            Tree.Node outputTitleNode = new Tree.Node(outputTitle);
            outputTitleNode.setSelectable(false);

            final VerticalGroup outputList = new VerticalGroup();
            Tree.Node outputNode = new Tree.Node(outputList);
            outputNode.setSelectable(false);

            outputTitleNode.add(outputNode);


            addInput.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    inputList.addActor(new Parameter(Integer.toString(counter++), Function.ParameterType.INPUT, skin));
                }
            });

            addOutput.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    outputList.addActor(new Parameter(Integer.toString(counter++), Function.ParameterType.OUTPUT, skin));
                }
            });

            titleNode.add(inputTitleNode);
            titleNode.add(outputTitleNode);
            tree.add(titleNode);
            add(tree).fillX().expandX();


        }

        private int counter = 1;
        Function function;
    }

    private static class ComponentsTable extends Table {
        ComponentsTable(Skin skin) {

            List<String> list = new List<String>(skin);


            //  list.setItems();

        }


    }

    private class VariableTable extends Table {
        VariableTable(Skin skin) {
            this.skin = skin;

            items = new Table();
            scroll = new ScrollPane(items);
            Table container = new Table();
            container.add(scroll).expand().fillX().top();
            add(container).fill().expand();
        }

        void addVariable() {
            VariableItem var = new VariableItem(Integer.toString(counter++), skin);
            items.add(var).fillX().expandX();
            items.row();

            scroll.layout();
            scroll.scrollTo(0, 0, 0, 0);
        }

        private Table items;
        private ScrollPane scroll;
        private Skin skin;
        private int counter = 1;
    }

    private class FunctionTable extends Table {
        FunctionTable(Skin skin) {
            this.skin = skin;

            items = new Table();
            scroll = new ScrollPane(items);
            Table container = new Table();
            container.add(scroll).expand().fillX().top();
            add(container).fill().expand();
        }

        void addFunction() {
            FunctionItem var = new FunctionItem(Integer.toString(counter++), skin);
            items.add(var).fillX().expandX();
            items.row();

            scroll.layout();
            scroll.scrollTo(0, 0, 0, 0);
        }

        private Table items;
        private ScrollPane scroll;
        private Skin skin;
        private int counter = 1;
    }

    ControlTable(Skin skin) {

        setWidth(200);
        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("VarTable.png")), 1, 1, 1, 1);
        setBackground(new NinePatchDrawable(patch));

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
        final ComponentsTable componentsTable = new ComponentsTable(skin);

        mainContainer.add(componentsTable);

        final Table container = new Table();

        Button createNewElement = new TextButton("Create new", skin);
        container.add(createNewElement).fillX().expandX().height(30).row();

        mainContainer.add(container);

        final VariableTable variableTable = new VariableTable(skin);
        final FunctionTable functionTable = new FunctionTable(skin);

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
                    variableTable.addVariable();
                } else if (functions.isChecked()) {
                    functionTable.addFunction();
                }
            }
        });

        variables.setChecked(true);
        functionTable.setVisible(false);

    }
}
