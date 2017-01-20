package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.node.VariableGetNode;
import com.littleinferno.flowchart.node.VariableSetNode;
import com.littleinferno.flowchart.value.Value;


class VariableItem extends Item {

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

        final Button del = new TextButton("del", skin);
        title.add(del).size(30).right();


        final PropertyTable property = new PropertyTable(varName, skin);

        Tree tree = new Tree(skin);

        Tree.Node titleNode = new Tree.Node(title);
        titleNode.setSelectable(false);
        Tree.Node propertyNode = new Tree.Node(property);
        propertyNode.setSelectable(false);
        titleNode.add(propertyNode);

        tree.add(titleNode);
        add(tree).fillX().expandX();

        property.addTextEnteredListener(new PropertyTable.TextEntered() {
            @Override
            public void entered(String text) {
                variable.setName(text);
                name.setText(text);
            }
        });

        property.addTypeSelectedListener(new PropertyTable.TypeSelected() {
            @Override
            public void select(Value.Type type) {
                variable.setValueType(type);
            }
        });


        Main.addSource(new DragAndDrop.Source(set) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(new VariableSetNode(variable, skin));

                payload.setDragActor(new Label(String.format("Set %s", variable.getName()), skin));

                return payload;
            }
        });

        Main.addSource(new DragAndDrop.Source(get) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(new VariableGetNode(variable, skin));

                payload.setDragActor(new Label(String.format("Get %s", variable.getName()), skin));

                return payload;
            }
        });

        del.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //VariableItem.this.getParent().removeActor(VariableItem.this);
            }
        });

    }

    Variable variable;

}
