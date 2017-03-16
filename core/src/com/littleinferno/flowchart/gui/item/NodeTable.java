package com.littleinferno.flowchart.gui.item;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.plugin.NodePluginManager;

import java.util.ArrayList;
import java.util.List;

public class NodeTable extends VisTable {

    public NodeTable(List<NodePluginManager.PluginHandle> pluginHandles) {

        ArrayList<Item> items = Stream.of(pluginHandles)
                .map(Item::new)
                .collect(Collectors.toCollection(ArrayList<Item>::new));

        ItemsTable itemsTable = new ItemsTable(items);
        add(itemsTable).top().fillX();

        setSize(350,300);
    }
}
