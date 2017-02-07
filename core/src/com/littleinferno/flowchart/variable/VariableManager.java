package com.littleinferno.flowchart.variable;

import com.annimon.stream.Stream;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.VariableChangedAdaptor;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.gui.VariableItem;
import com.littleinferno.flowchart.ui.Main;

import java.util.ArrayList;

public class VariableManager {

    public static final VariableManager instance = new VariableManager();

    private ArrayListAdapter<Variable, VisTable> variables;
    private final VisTable detailsTable;
    private final VisTable varTable;

    private int counter;

    private VariableManager() {
        variables = new VariableListAdapter(new ArrayList<>());
        detailsTable = new VisTable(true);
        varTable = new VisTable(true);
        counter = 0;

        ListView<Variable> view = new ListView<>(variables);

        view.setItemClickListener(item -> {
            detailsTable.clearChildren();
            detailsTable.add(item.getTable()).grow();
        });

        varTable.add(view.getMainTable()).grow().row();

    }

    public String createVariable() {
        Variable variable = new Variable("newVar" + counter++, DataType.BOOL, false);

        variables.add(variable);

        return variable.getName();
    }

    void removeVariable(Variable variable) {
        variables.remove(variable);
    }

    public String gen(CodeBuilder builder) {
        final StringBuilder stringBuilder = new StringBuilder();

        Stream.of(variables.iterable()).forEach(variable -> stringBuilder.append(variable.gen()));

        return stringBuilder.toString();
    }

    public VisTable getVarTable() {
        return varTable;
    }

    public VisTable getDetailsTable() {
        return detailsTable;
    }


    private class VariableListAdapter extends ArrayListAdapter<Variable, VisTable> {
        private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

        VariableListAdapter(ArrayList<Variable> array) {
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
        protected VisTable createView(Variable item) {

            final VisTable table = new VisTable();
            final VisLabel it = new VisLabel(item.getName());

            item.addListener(new VariableChangedAdaptor() {
                @Override
                public void nameChanged(String newName) {
                    it.setText(newName);
                }
            });

            Main.addSourceF(new DragAndDrop.Source(table) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();

                    payload.setObject(new VariableItem(item));
                    payload.setDragActor(new VisLabel(it.getText()));

                    deselectView(table);
                    return payload;
                }
            });

            table.add(it).grow();
            return table;
        }
    }

}
