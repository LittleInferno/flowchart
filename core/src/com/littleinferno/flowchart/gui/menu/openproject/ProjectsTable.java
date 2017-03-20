package com.littleinferno.flowchart.gui.menu.openproject;

import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.project.Project;

import java.util.ArrayList;

public class ProjectsTable extends VisTable {

    public ProjectsTable() {

        ArrayList<FileHandle> projects =null;// Files.getProjects();
        ProjectListAdapter adapter = new ProjectListAdapter(projects);
        ListView<FileHandle> listView = new ListView<>(adapter);

        listView.setItemClickListener(item -> Project.load(item.name(), item));

        add(listView.getMainTable()).grow().row();
    }
}
