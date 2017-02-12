package com.littleinferno.flowchart.gui;


import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.littleinferno.flowchart.function.Function;

public class FunctionScene extends Scene {
    public FunctionScene(Function function, SceneUi sceneUi) {
        super(function.getName(), true, sceneUi);

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
