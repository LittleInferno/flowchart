package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.littleinferno.flowchart.node.BoolNode;
import com.littleinferno.flowchart.node.FloatNode;
import com.littleinferno.flowchart.node.IfNode;
import com.littleinferno.flowchart.node.IntegerNode;
import com.littleinferno.flowchart.node.LogNode;
import com.littleinferno.flowchart.node.StringNode;
import com.littleinferno.flowchart.node.math.AddNode;
import com.littleinferno.flowchart.node.math.CompareNode;
import com.littleinferno.flowchart.node.math.DivNode;
import com.littleinferno.flowchart.node.math.EqualsNode;
import com.littleinferno.flowchart.node.math.GreatNode;
import com.littleinferno.flowchart.node.math.LessNode;
import com.littleinferno.flowchart.node.math.MulNode;
import com.littleinferno.flowchart.node.math.SubNode;
import com.littleinferno.flowchart.value.Value;


public class NodeTable extends Table {

    NodeTable(final Skin skin) {


        final List<String> list = new List(skin);

        add(list).expand().fillX().top();
        list.setItems("Bool", "Integer", "Float", "String", "Log",
                "add int", "add float", "add string",
                "sub int", "sub float",
                "mul int", "mul float",
                "div int", "div float",
                "equqls int", "equqls float", "equqls string",
                "less int", "less float", "less string",
                "great int", "great float", "great string",
                "compare int", "compare float", "compare string",
                "if"
        );

        Main.addSource(new DragAndDrop.Source(list) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                String item = list.getSelected();
                int itemI = list.getSelectedIndex();

                switch (itemI) {
                    case 0:
                        payload.setObject(new BoolNode(skin));
                        break;
                    case 1:
                        payload.setObject(new IntegerNode(skin));
                        break;
                    case 2:
                        payload.setObject(new FloatNode(skin));
                        break;
                    case 3:
                        payload.setObject(new StringNode(skin));
                        break;
                    case 4:
                        payload.setObject(new LogNode(skin));
                        break;
                    case 5:
                        payload.setObject(new AddNode(Value.Type.INT, skin));
                        break;
                    case 6:
                        payload.setObject(new AddNode(Value.Type.FLOAT, skin));
                        break;
                    case 7:
                        payload.setObject(new AddNode(Value.Type.STRING, skin));
                        break;
                    case 8:
                        payload.setObject(new SubNode(Value.Type.INT, skin));
                        break;
                    case 9:
                        payload.setObject(new SubNode(Value.Type.FLOAT, skin));
                        break;
                    case 10:
                        payload.setObject(new MulNode(Value.Type.INT, skin));
                        break;
                    case 11:
                        payload.setObject(new MulNode(Value.Type.FLOAT, skin));
                        break;
                    case 12:
                        payload.setObject(new DivNode(Value.Type.INT, skin));
                        break;
                    case 13:
                        payload.setObject(new DivNode(Value.Type.FLOAT, skin));
                        break;
                    case 14:
                        payload.setObject(new EqualsNode(Value.Type.INT, skin));
                        break;
                    case 15:
                        payload.setObject(new EqualsNode(Value.Type.FLOAT, skin));
                        break;
                    case 16:
                        payload.setObject(new EqualsNode(Value.Type.STRING, skin));
                        break;
                    case 17:
                        payload.setObject(new LessNode(Value.Type.INT, skin));
                        break;
                    case 18:
                        payload.setObject(new LessNode(Value.Type.FLOAT, skin));
                        break;
                    case 19:
                        payload.setObject(new LessNode(Value.Type.STRING, skin));
                        break;
                    case 20:
                        payload.setObject(new GreatNode(Value.Type.INT, skin));
                        break;
                    case 21:
                        payload.setObject(new GreatNode(Value.Type.FLOAT, skin));
                        break;
                    case 22:
                        payload.setObject(new GreatNode(Value.Type.STRING, skin));
                        break;
                    case 23:
                        payload.setObject(new CompareNode(Value.Type.INT, skin));
                        break;
                    case 24:
                        payload.setObject(new CompareNode(Value.Type.FLOAT, skin));
                        break;
                    case 25:
                        payload.setObject(new CompareNode(Value.Type.STRING, skin));
                        break;
                    case 26:
                        payload.setObject(new IfNode(skin));
                        break;

                }
                payload.setDragActor(new Label(item, skin));

                return payload;
            }
        });
    }
}
