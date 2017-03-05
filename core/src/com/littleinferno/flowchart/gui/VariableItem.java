package com.littleinferno.flowchart.gui;

import com.kotcrab.vis.ui.widget.MenuItem;
import com.littleinferno.flowchart.node.VariableGetNode;
import com.littleinferno.flowchart.node.VariableSetNode;
import com.littleinferno.flowchart.util.EventWrapper;
import com.littleinferno.flowchart.variable.Variable;

public class VariableItem extends DropItem {
    private final Variable variable;

    public VariableItem(Variable var) {
        this.variable = var;
    }

    @Override
    public void init(com.littleinferno.flowchart.scene.Scene scene) {
        scene.addActor(this);

        addItem(new MenuItem("set", new EventWrapper((event, actor) -> getStage()
                .getNodeManager()
                .createNode(VariableSetNode.class, variable)
                .setPosition(getX(), getY())
        )));

        addSeparator();

        addItem(new MenuItem("get", new EventWrapper((event, actor) -> getStage()
                .getNodeManager()
                .createNode(VariableGetNode.class, variable)
                .setPosition(getX(), getY())
        )));
    }
}
