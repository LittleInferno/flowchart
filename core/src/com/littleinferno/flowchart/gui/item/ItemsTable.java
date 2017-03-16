package com.littleinferno.flowchart.gui.item;

import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;

public class ItemsTable extends VisTable {

    public ItemsTable(ArrayList<Item> items) {
        ItemListAdaptor adapter = new ItemListAdaptor(items);
        ListView<Item> listView = new ListView<>(adapter);
        adapter.init();
        setDebug(true);
        add(listView.getMainTable()).fill();
    }
}
