package com.littleinferno.flowchart.gui.function;

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
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.NameChangeable;
import com.littleinferno.flowchart.ui.Main;

import java.util.ArrayList;

public class FunctionTable extends VisTable {

    private final ArrayListAdapter<String, VisTable> adapter;
    private final VisTable details;
    private ArrayList<FunctionDetails> functionDetailses;

    private int counter = 0;

    public FunctionTable() {
        super(true);

        functionDetailses = new ArrayList<FunctionDetails>();
        ArrayList<String> array = new ArrayList<String>();

        adapter = new FunctionAdapter(array);
        ListView<String> view = new ListView<String>(adapter);
        view.setItemClickListener(new ListView.ItemClickListener<String>() {
            @Override
            public void clicked(String item) {

                for (FunctionDetails function : functionDetailses) {
                    if (function.getName().equals(item)) {
                        details.clearChildren();
                        details.add(function).grow();
                    }
                }
            }
        });

        VisTextButton create = new VisTextButton("create");
        create.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addFunction();
            }
        });

        details = new VisTable(true);

        add(create).growX().row();
        add(view.getMainTable()).grow().row();
        addSeparator();
        add(details).growX().height(250);

    }

    private void addFunction() {
        FunctionDetails functionDetails = new FunctionDetails(this, new Function("newFun" + counter++));
        functionDetailses.add(functionDetails);
        final String name = functionDetails.getName();
        adapter.add(name);

        functionDetails.function.addListener(new NameChangeable.NameChange() {
            String oldName = name;

            @Override
            public void changed(String newName) {
                adapter.set(adapter.indexOf(oldName), newName);
                oldName = newName;
            }
        });
    }

    void deleteFunction(FunctionDetails function) {
        functionDetailses.remove(function);
        adapter.remove(function.getName());
        details.clearChildren();
    }

    private class FunctionAdapter extends ArrayListAdapter<String, VisTable> {
        private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

        FunctionAdapter(ArrayList<String> array) {
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

                    for (FunctionDetails function : functionDetailses) {
                        if (function.getName().equals(it.getText().toString())) {
                            payload.setObject(new FunctionItem(function.function));
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
