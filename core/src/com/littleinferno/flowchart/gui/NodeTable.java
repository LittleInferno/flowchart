package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;
import java.util.Collections;

class NodeTable extends VisTable {

    private UIScene sceneUi;

    NodeTable(UIScene sceneUi) {
        super(true);
        this.sceneUi = sceneUi;
        String[] nodeList = sceneUi.getProject().getNodePluginManager().getNodeList();

//        String[] items = new String[]{
//                MakeArrayNode.class.getName(),
//                ArrayAddNode.class.getName(),
//                ArrayGetNode.class.getName(),
//                ArraySetNode.class.getName(),
//                PrintArrayNode.class.getName(),
//        };

        ArrayList<String> array = new ArrayList<>();
        Collections.addAll(array, nodeList);

        NodeAdapter adapter = new NodeAdapter(array);
        ListView<String> view = new ListView<>(adapter);

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

            final VisLabel it = new VisLabel(item);

            sceneUi.addDragAndDropSource(new DragAndDrop.Source(table) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();

                    payload.setObject(item);

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
