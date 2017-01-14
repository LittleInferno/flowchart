package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.littleinferno.flowchart.node.BeginNode;
import com.littleinferno.flowchart.node.IntegerNode;
import com.littleinferno.flowchart.node.LogNode;
import com.littleinferno.flowchart.node.StringNode;


public class NodeTable extends Table {

    NodeTable(final Skin skin) {


        final List<String> list = new List(skin);

        add(list).expand().fillX().top();
        list.setItems("Integer", "String", "Log");

        Main.addSource(new DragAndDrop.Source(list) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                String item = list.getSelected();

                if (item.equals("Integer")) payload.setObject(new IntegerNode());
                else if (item.equals("String")) payload.setObject(new StringNode());
                else if (item.equals("Log")) payload.setObject(new LogNode());

                payload.setDragActor(new Label(item, skin));

                return payload;
            }
        });
    }
}
