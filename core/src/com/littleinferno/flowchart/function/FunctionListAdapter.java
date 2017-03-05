package com.littleinferno.flowchart.function;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.gui.FunctionItem;
import com.littleinferno.flowchart.project.Project;

import java.util.ArrayList;

class FunctionListAdapter extends ArrayListAdapter<Function, VisTable> {
    private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
    private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

    FunctionListAdapter(ArrayList<Function> array) {
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
    protected VisTable createView(Function item) {

        final VisTable table = new VisTable();
        final VisLabel it = new VisLabel(item.getName());

        item.addListener(it::setText);

        Project.instance().getUiScene().addDragAndDropSource(new DragAndDrop.Source(table) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();

                payload.setObject(new FunctionItem(item));
                payload.setDragActor(new VisLabel(it.getText()));

                deselectView(table);
                return payload;
            }
        });

        table.add(it).grow();
        return table;
    }
}
