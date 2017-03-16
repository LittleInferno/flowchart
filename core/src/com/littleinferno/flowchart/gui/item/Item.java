package com.littleinferno.flowchart.gui.item;

import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.littleinferno.flowchart.plugin.NodePluginManager;
import com.littleinferno.flowchart.util.EventWrapper;

public class Item extends VisTable {

    private final CollapsibleWidget collapsible;
    private VisTable container;
    private NodePluginManager.PluginHandle pluginHandle;

    Item(NodePluginManager.PluginHandle pluginHandle) {
        this.pluginHandle = pluginHandle;
        this.container = new VisTable();
        this.collapsible = new CollapsibleWidget(container);

        setDebug(true);

        VisImageButton button = new VisImageButton("toggle");

        button.addListener(new EventWrapper((event, actor) -> {
            collapsible.setCollapsed(button.isChecked());
        }));

        container.defaults().size(50);

        int k = 0;
        for (NodePluginManager.PluginNodeHandle i : pluginHandle.getHandles("any")) {

            container.add(new VisTextButton(i.name)).pad(2);
            k++;
            if (k == 4) {
                k = 0;
                container.row();
            }
        }

        add(button);
        add(new VisLabel(pluginHandle.getName())).fill().row();
        add(collapsible).colspan(2).fill();

    }


}

