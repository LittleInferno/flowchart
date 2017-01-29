package com.littleinferno.flowchart.nui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;


public class ControlTable extends VisTable {

    public ControlTable() {

        TabbedPane.TabbedPaneStyle tabbedPaneStyle = VisUI.getSkin().get(TabbedPane.TabbedPaneStyle.class);
        tabbedPaneStyle.draggable = false;

        TabbedPane tabbedPane = new TabbedPane(tabbedPaneStyle);

        final Table tabTable = new Table();

        tabbedPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab(Tab tab) {
                Table content = tab.getContentTable();

                tabTable.clearChildren();
                tabTable.add(content).expand().fill();
            }
        });


        add(tabbedPane.getTable()).fillX().expandX().row();
        add(tabTable).fill().expand();


        tabbedPane.add(new com.littleinferno.flowchart.nui.variable.VariableTable());
        tabbedPane.add(new com.littleinferno.flowchart.nui.function.FunctionTable());

    }
}
