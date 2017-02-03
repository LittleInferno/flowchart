package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.node.BoolNode;
import com.littleinferno.flowchart.node.FloatNode;
import com.littleinferno.flowchart.node.IfNode;
import com.littleinferno.flowchart.node.IntegerNode;
import com.littleinferno.flowchart.node.PrintNode;
import com.littleinferno.flowchart.node.StringNode;
import com.littleinferno.flowchart.node.array.ArrayAddNode;
import com.littleinferno.flowchart.node.array.ArrayGetNode;
import com.littleinferno.flowchart.node.array.MakeArrayNode;
import com.littleinferno.flowchart.node.math.AddNode;
import com.littleinferno.flowchart.node.math.DivNode;
import com.littleinferno.flowchart.node.math.EqualNode;
import com.littleinferno.flowchart.node.math.GreatNode;
import com.littleinferno.flowchart.node.math.LessNode;
import com.littleinferno.flowchart.node.math.MulNode;
import com.littleinferno.flowchart.node.math.SubNode;
import com.littleinferno.flowchart.ui.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class NodeTable extends VisTable {

    NodeTable() {
        super(true);
        String[] items = new String[]{
                BoolNode.class.getName(),
                IntegerNode.class.getName(),
                FloatNode.class.getName(),
                StringNode.class.getName(),

                AddNode.class.getName(),
                SubNode.class.getName(),
                MulNode.class.getName(),
                DivNode.class.getName(),

                EqualNode.class.getName(),
                LessNode.class.getName(),
                GreatNode.class.getName(),

                IfNode.class.getName(),

                MakeArrayNode.class.getName(),
                ArrayAddNode.class.getName(),
                ArrayGetNode.class.getName(),
                PrintNode.class.getName(),
        };

        List<String> tmp = Arrays.asList(items);
        ArrayList<String> array = new ArrayList<String>();
        array.addAll(tmp);

        NodeAdapter adapter = new NodeAdapter(array);
        ListView<String> view = new ListView<String>(adapter);

        add(view.getMainTable()).grow().row();
    }

    private class NodeAdapter extends ArrayListAdapter<String, VisTable> {
        private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

        NodeAdapter(ArrayList<String> array) {
            super(array);
            setSelectionMode(SelectionMode.SINGLE);
        }

        @Override
        protected void selectView(VisTable view) {
            view.setBackground(selection);
        }

        @Override
        protected void deselectView(VisTable view) {
            view.setBackground(bg);
        }

        @Override
        protected VisTable createView(final String item) {
            final VisTable table = new VisTable();
            table.left();

            String[] strings = item.split("[.]");

            final VisLabel it = new VisLabel(strings[strings.length - 1]);

            Main.addSourceF(new DragAndDrop.Source(table) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();

                    Object object = null;
                    try {
                        Class<?> node = Class.forName(item);
                        object = node.newInstance();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    payload.setObject(object);
                    payload.setDragActor(new VisLabel(it.getText()));

                    deselectView(table);
                    return payload;
                }
            });

            table.add(it);
            return table;
        }
    }
}
