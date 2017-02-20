package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.littleinferno.flowchart.node.VariableGetNode;
import com.littleinferno.flowchart.node.VariableSetNode;
import com.littleinferno.flowchart.variable.Variable;

public class VariableItem extends DropItem {
    private final Variable variable;

    public VariableItem(Variable var) {
        this.variable = var;
    }

    @Override
    void init(Scene scene) {
        scene.addActor(this);

        addItem(new MenuItem("set", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                VariableSetNode setNode = new VariableSetNode(variable);
                setNode.setPosition(VariableItem.this.getX(), VariableItem.this.getY());
                getStage().addActor(setNode);
            }
        }));

        addSeparator();

        addItem(new MenuItem("get", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                VariableGetNode getNode = new VariableGetNode(variable);
                getNode.setPosition(VariableItem.this.getX(), VariableItem.this.getY());
                getStage().addActor(getNode);
            }
        }));
    }
}
