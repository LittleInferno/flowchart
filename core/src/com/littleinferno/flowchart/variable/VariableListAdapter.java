package com.littleinferno.flowchart.variable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.gui.VariableItem;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.util.BaseListAdaptor;

import java.util.ArrayList;

public class VariableListAdapter extends BaseListAdaptor<Variable, VisTable> {
    public VariableListAdapter(ArrayList<Variable> array) {
        super(array);
    }

    @Override
    protected VisTable createView(Variable item) {

        final VisTable table = new VisTable();
        final VisLabel it = new VisLabel(item.getName());

        item.addListener(it::setText);

        Project.instance().getUiScene().addDragAndDropSource(new DragAndDrop.Source(table) {
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
