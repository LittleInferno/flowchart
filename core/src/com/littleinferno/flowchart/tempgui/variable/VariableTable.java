package com.littleinferno.flowchart.tempgui.variable;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.VariableChangedAdaptor;
import com.littleinferno.flowchart.ui.Main;

import java.util.ArrayList;

public class VariableTable extends VisTable {

    private final ArrayListAdapter<String, VisTable> adapter;
    private final VisTable details;
    private ArrayList<VariableDetails> variableDetailses;

    private int counter = 0;

    public VariableTable() {
        super(true);

        variableDetailses = new ArrayList<VariableDetails>();
        ArrayList<String> array = new ArrayList<String>();

        adapter = new VariableAdapter(array);
        ListView<String> view = new ListView<String>(adapter);
        view.setItemClickListener(new ListView.ItemClickListener<String>() {
            @Override
            public void clicked(String item) {
                for (VariableDetails variable : variableDetailses) {
                    if (variable.getName().equals(item)) {
                        details.clearChildren();
                        details.add(variable).grow();
                    }
                }
            }
        });

        VisTextButton create = new VisTextButton("create");
        create.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addVariable();
            }
        });

        details = new VisTable(true);

        add(create).growX().row();
        add(view.getMainTable()).grow().row();
        addSeparator();
        add(details).growX().row();
        addSeparator();
    }

    private void addVariable() {
        VariableDetails variableDetails =
                new VariableDetails(this, new Variable("newVar" + counter++, DataType.BOOL, false));

        variableDetailses.add(variableDetails);
        final String name = variableDetails.getName();
        adapter.add(name);

        variableDetails.variable.addListener(new VariableChangedAdaptor() {
            String oldName = name;

            @Override
            public void nameChanged(String newName) {
                adapter.set(adapter.indexOf(oldName), newName);
                oldName = newName;
            }
        });
    }

    private class VariableAdapter extends ArrayListAdapter<String, VisTable> {
        private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

        VariableAdapter(ArrayList<String> array) {
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
        protected VisTable createView(String item) {
            final VisTable table = new VisTable();
            table.left();
            final VisLabel it = new VisLabel(item);

            Main.addSourceF(new DragAndDrop.Source(table) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();

                    for (VariableDetails variable : variableDetailses) {
                        if (variable.getName().equals(it.getText().toString())) {
                            payload.setObject(new VariableItem(variable.variable));
                            payload.setDragActor(new VisLabel(it.getText()));
                        }
                    }

                    deselectView(table);
                    return payload;
                }
            });

            table.add(it);
            return table;
        }
    }
}
