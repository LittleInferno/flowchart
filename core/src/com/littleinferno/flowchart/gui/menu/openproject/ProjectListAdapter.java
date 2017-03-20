package com.littleinferno.flowchart.gui.menu.openproject;

import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.util.BaseListAdaptor;

import java.util.ArrayList;

class ProjectListAdapter extends BaseListAdaptor<FileHandle, VisTable> {

    ProjectListAdapter(ArrayList<FileHandle> array) {
        super(array);
    }

    @Override
    protected VisTable createView(FileHandle item) {
        final VisTable table = new VisTable();
        final VisLabel label = new VisLabel(item.name());

        table.add(label).grow();
        return table;
    }
}
