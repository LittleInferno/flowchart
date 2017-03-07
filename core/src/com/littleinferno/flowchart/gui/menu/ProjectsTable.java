package com.littleinferno.flowchart.gui.menu;

import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.util.Files;

import java.util.ArrayList;

class ProjectsTable extends VisTable {

    ProjectsTable() {

        ArrayList<FileHandle> projects = Files.getProjects();
        ProjectListAdapter adapter = new ProjectListAdapter(projects);
        ListView<FileHandle> listView = new ListView<>(adapter);

        listView.setItemClickListener(item -> Project.load(item.name(), item));

        add(listView.getMainTable()).grow().row();
    }
}
