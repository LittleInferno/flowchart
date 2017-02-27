package com.littleinferno.flowchart;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;
import com.littleinferno.flowchart.codegen.JSCodeExecution;
import com.littleinferno.flowchart.codegen.JSCodeGenerator;
import com.littleinferno.flowchart.node.IntegerNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.node.SceneNodeManager;
import com.littleinferno.flowchart.project.Project;


public class Flowchart extends Game {

    private Screen scene;
    private static boolean change;

    @Override
    public void create() {

        Project project = Project.createProject("test", "test/", new JSCodeGenerator(), new JSCodeExecution());

        NodeManager nodeManager = new NodeManager();

        nodeManager.addSerializer(IntegerNode.class, new Node.DefaultNodeSerializer<>());


        SceneNodeManager sceneNodeManager = new SceneNodeManager();
        sceneNodeManager.createNode(IntegerNode.class);
        sceneNodeManager.createNode(IntegerNode.class);
        sceneNodeManager.createNode(IntegerNode.class);

        String save = nodeManager.save(sceneNodeManager);

        System.out.println(save);

        SceneNodeManager load = nodeManager.load(save);

        save = nodeManager.save(load);

        System.out.println(save);

        scene = project.getProjectScreen();
        setScreen(scene);
    }

    @Override
    public void dispose() {

        scene.dispose();

        VisUI.dispose();
    }
}
