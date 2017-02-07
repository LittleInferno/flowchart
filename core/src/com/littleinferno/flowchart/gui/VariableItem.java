package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.littleinferno.flowchart.variable.Variable;
import com.littleinferno.flowchart.node.VariableGetNode;
import com.littleinferno.flowchart.node.VariableSetNode;
import com.littleinferno.flowchart.ui.Main;

public class VariableItem extends PopupMenu {
    private final Variable variable;

    public VariableItem(Variable var) {
        this.variable = var;

        addItem(new MenuItem("set", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                VariableSetNode setNode = new VariableSetNode(variable, Main.skin);
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
