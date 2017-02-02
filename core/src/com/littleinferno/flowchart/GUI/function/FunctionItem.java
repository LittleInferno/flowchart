package com.littleinferno.flowchart.gui.function;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.node.FunctionCallNode;
import com.littleinferno.flowchart.node.FunctionReturnNode;

class FunctionItem extends PopupMenu {

    private final Function function;

    FunctionItem(Function fun) {
        function = fun;

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
