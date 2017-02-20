package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.node.FunctionCallNode;
import com.littleinferno.flowchart.node.FunctionReturnNode;

public class FunctionItem extends DropItem {

    private final Function function;

    public FunctionItem(Function fun) {
        function = fun;
    }

    @Override
    void init(Scene scene) {

        if (scene != function.getScene()) {
            FunctionCallNode callNode = new FunctionCallNode(function);
            callNode.setPosition(FunctionItem.this.getX(), FunctionItem.this.getY());
            scene.addActor(callNode);
            return;
        }

        scene.addActor(this);

        addItem(new MenuItem("call", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FunctionCallNode callNode = new FunctionCallNode(function);
                callNode.setPosition(FunctionItem.this.getX(), FunctionItem.this.getY());
                getStage().addActor(callNode);
            }
        }));

        addSeparator();

        addItem(new MenuItem("return", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FunctionReturnNode returnNode = new FunctionReturnNode(function);
                returnNode.setPosition(FunctionItem.this.getX(), FunctionItem.this.getY());
                getStage().addActor(returnNode);
            }
        }));

    }
}
