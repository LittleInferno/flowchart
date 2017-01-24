package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.node.BoolNode;
import com.littleinferno.flowchart.node.FloatNode;
import com.littleinferno.flowchart.node.IfNode;
import com.littleinferno.flowchart.node.IntegerNode;
import com.littleinferno.flowchart.node.LogNode;
import com.littleinferno.flowchart.node.StringNode;
import com.littleinferno.flowchart.node.array.ArrayAddNode;
import com.littleinferno.flowchart.node.array.ArrayGetNode;
import com.littleinferno.flowchart.node.array.MakeArrayNode;
import com.littleinferno.flowchart.node.math.AddNode;
import com.littleinferno.flowchart.node.math.CompareNode;
import com.littleinferno.flowchart.node.math.DivNode;
import com.littleinferno.flowchart.node.math.EqualNode;
import com.littleinferno.flowchart.node.math.GreatNode;
import com.littleinferno.flowchart.node.math.LessNode;
import com.littleinferno.flowchart.node.math.MulNode;
import com.littleinferno.flowchart.node.math.SubNode;


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
                "if",
                "make Bool array", "make Integer array", "make Float array", "make String array",
                "add to Bool array", "add to Integer array", "add to Float array", "add to String array",
                "get from Bool array", "get from Integer array", "get from Float array", "get from String array"
        );

        ScrollPane scroll = new ScrollPane(list);
        Table container = new Table();
        container.add(scroll).expand().fillX().top();
        add(container).fill().expand();

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
                        payload.setObject(new AddNode(DataType.INT, skin));
                        break;
                    case 6:
                        payload.setObject(new AddNode(DataType.FLOAT, skin));
                        break;
                    case 7:
                        payload.setObject(new AddNode(DataType.STRING, skin));
                        break;
                    case 8:
                        payload.setObject(new SubNode(DataType.INT, skin));
                        break;
                    case 9:
                        payload.setObject(new SubNode(DataType.FLOAT, skin));
                        break;
                    case 10:
                        payload.setObject(new MulNode(DataType.INT, skin));
                        break;
                    case 11:
                        payload.setObject(new MulNode(DataType.FLOAT, skin));
                        break;
                    case 12:
                        payload.setObject(new DivNode(DataType.INT, skin));
                        break;
                    case 13:
                        payload.setObject(new DivNode(DataType.FLOAT, skin));
                        break;
                    case 14:
                        payload.setObject(new EqualNode(DataType.INT, skin));
                        break;
                    case 15:
                        payload.setObject(new EqualNode(DataType.FLOAT, skin));
                        break;
                    case 16:
                        payload.setObject(new EqualNode(DataType.STRING, skin));
                        break;
                    case 17:
                        payload.setObject(new LessNode(DataType.INT, skin));
                        break;
                    case 18:
                        payload.setObject(new LessNode(DataType.FLOAT, skin));
                        break;
                    case 19:
                        payload.setObject(new LessNode(DataType.STRING, skin));
                        break;
                    case 20:
                        payload.setObject(new GreatNode(DataType.INT, skin));
                        break;
                    case 21:
                        payload.setObject(new GreatNode(DataType.FLOAT, skin));
                        break;
                    case 22:
                        payload.setObject(new GreatNode(DataType.STRING, skin));
                        break;
                    case 23:
                        payload.setObject(new CompareNode(DataType.INT, skin));
                        break;
                    case 24:
                        payload.setObject(new CompareNode(DataType.FLOAT, skin));
                        break;
                    case 25:
                        payload.setObject(new CompareNode(DataType.STRING, skin));
                        break;
                    case 26:
                        payload.setObject(new IfNode(skin));
                        break;
                    case 27:
                        payload.setObject(new MakeArrayNode(DataType.BOOL, skin));
                        break;
                    case 28:
                        payload.setObject(new MakeArrayNode(DataType.INT, skin));
                        break;
                    case 29:
                        payload.setObject(new MakeArrayNode(DataType.FLOAT, skin));
                        break;
                    case 30:
                        payload.setObject(new MakeArrayNode(DataType.STRING, skin));
                        break;
                    case 31:
                        payload.setObject(new ArrayAddNode(DataType.BOOL, skin));
                        break;
                    case 32:
                        payload.setObject(new ArrayAddNode(DataType.INT, skin));
                        break;
                    case 33:
                        payload.setObject(new ArrayAddNode(DataType.FLOAT, skin));
                        break;
                    case 34:
                        payload.setObject(new ArrayAddNode(DataType.STRING, skin));
                        break;
                    case 35:
                        payload.setObject(new ArrayGetNode(DataType.BOOL, skin));
                        break;
                    case 36:
                        payload.setObject(new ArrayGetNode(DataType.INT, skin));
                        break;
                    case 37:
                        payload.setObject(new ArrayGetNode(DataType.FLOAT, skin));
                        break;
                    case 38:
                        payload.setObject(new ArrayGetNode(DataType.STRING, skin));
                        break;
                }
                payload.setDragActor(new Label(item, skin));

                return payload;
            }
        });
    }
}
