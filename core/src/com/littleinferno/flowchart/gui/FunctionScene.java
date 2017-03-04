package com.littleinferno.flowchart.gui;


import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.node.NodeManager;

public class FunctionScene extends Scene {
    public FunctionScene(Function function) {
        super(function.getName(), new NodeManager(), true);

        function.addListener(newName -> {
            setName(newName);
            UiTab uiTab = getUiTab();
            TabbedPane pane = uiTab.getPane();
            int pos = pane.getUIOrderedTabs().indexOf(uiTab, true);
            pane.remove(uiTab);
            pane.insert(pos, uiTab);
        });
    }

    void setFunction(Function function) {
        setName(function.getName());
        function.addListener(newName -> {
            setName(newName);
            UiTab uiTab = getUiTab();
            TabbedPane pane = uiTab.getPane();
            int pos = pane.getUIOrderedTabs().indexOf(uiTab, true);
            pane.remove(uiTab);
            pane.insert(pos, uiTab);
        });
    }
}
