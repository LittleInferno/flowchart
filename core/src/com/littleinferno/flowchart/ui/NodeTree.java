package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by danil on 27.12.2016.
 */

public class NodeTree extends Table {

    public NodeTree(Skin skin) {

        Tree tree = new Tree(skin);
        tree.add(new Tree.Node(new Label("qwe", skin)));
        tree.add(new Tree.Node(new Label("qwe", skin)));

        Tree.Node node = new Tree.Node(new Label("qwe", skin));
        node.add(new Tree.Node(new Label("qwe", skin)));
        node.add(new Tree.Node(new Label("qwe", skin)));


        Tree.Node node1 = new Tree.Node(new Label("qwe", skin));
        node1.add(new Tree.Node(new Label("qwe", skin)));
        node1.add(new Tree.Node(new Label("qwe", skin)));
        node.add(node1);

        tree.add(node);
        add(tree);
    }

}
