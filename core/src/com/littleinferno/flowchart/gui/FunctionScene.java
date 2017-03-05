package com.littleinferno.flowchart.gui;


import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.project.Project;

public class FunctionScene extends Scene {
    public FunctionScene(Function function) {
        super(new FunctionSceneHandle(new NodeManager(), function.getName(), function.getName()));
        function.addListener(newName -> {
            setName(newName);
            Scene.UiTab uiTab = getUiTab();
            TabbedPane pane = uiTab.getPane();
            int pos = pane.getUIOrderedTabs().indexOf(uiTab, true);
            pane.remove(uiTab);
            pane.insert(pos, uiTab);
        });
    }

    public FunctionScene(FunctionSceneHandle functionSceneHandle) {
        super(functionSceneHandle);

        Function function = Project.instance()
                .getFunctionManager()
                .getFunction(functionSceneHandle.functionName);

        function.setScene(this);
        function.addListener(newName -> {
            setName(newName);
            Scene.UiTab uiTab = getUiTab();
            TabbedPane pane = uiTab.getPane();
            int pos = pane.getUIOrderedTabs().indexOf(uiTab, true);
            pane.remove(uiTab);
            pane.insert(pos, uiTab);
        });
    }

    public static class FunctionSceneHandle extends SceneHandle {
        String functionName;

        public FunctionSceneHandle() {
        }

        public FunctionSceneHandle(NodeManager nodeManager, String name, String functionName) {
            super(nodeManager, name, true);
            this.functionName = functionName;
        }
    }
}
