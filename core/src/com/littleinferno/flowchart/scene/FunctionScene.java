package com.littleinferno.flowchart.scene;


import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.project.Project;

public class FunctionScene extends Scene {

    @SuppressWarnings("unused")
    public FunctionScene(Function function, Project project) {
        super(new FunctionSceneHandle(new NodeManager(), function.getName(), function.getName()), project);
        function.addListener(newName -> {
            setName(newName);
            UiTab uiTab = getUiTab();
            TabbedPane pane = uiTab.getPane();
            int pos = pane.getUIOrderedTabs().indexOf(uiTab, true);
            pane.remove(uiTab);
            pane.insert(pos, uiTab);
        });
    }

    @SuppressWarnings("unused")
    public FunctionScene(FunctionSceneHandle functionSceneHandle, Project project) {
        super(functionSceneHandle, project);

        Function function = Project.instance()
                .getFunctionManager()
                .getFunction(functionSceneHandle.functionName);

        function.setScene(this);
        function.addListener(newName -> {
            setName(newName);
            UiTab uiTab = getUiTab();
            TabbedPane pane = uiTab.getPane();
            int pos = pane.getUIOrderedTabs().indexOf(uiTab, true);
            pane.remove(uiTab);
            pane.insert(pos, uiTab);
        });
    }


    @SuppressWarnings("WeakerAccess")
    public static class FunctionSceneHandle extends SceneHandle {
        String functionName;

        @SuppressWarnings("unused")
        public FunctionSceneHandle() {
        }

        public FunctionSceneHandle(NodeManager nodeManager, String name, String functionName) {
            super(nodeManager, name, true);
            this.functionName = functionName;
        }
    }
}
