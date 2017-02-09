package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

import java.util.ArrayList;

public class SceneUi extends Stage {

    private ArrayList<Scene> scenes;
    private InputMultiplexer inputMultiplexer;
    private Scene show;


    static private DragAndDrop dragAndDrop;


    public SceneUi() {

        scenes = new ArrayList<>();

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);

        show = null;

        dragAndDrop = new DragAndDrop();

        VisTable container = new VisTable();
        container.setFillParent(true);

        ControlTable controlTable = new ControlTable();

        VisTable activityContainer = new VisTable();

        Table activity = new Table();
        TabbedPane tabbedPane = new TabbedPane();

        tabbedPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab(Tab tab) {
                Table content = tab.getContentTable();

                activity.clearChildren();
                activity.add(content).grow();

                if (show != null) {
                    inputMultiplexer.removeProcessor(show);
                    inputMultiplexer.removeProcessor(show.getGesture());
                }

                show = ((Scene.UiTab) tab).getScene();

                inputMultiplexer.addProcessor(show);
                inputMultiplexer.addProcessor(show.getGesture());
            }
        });

        scenes.add(new Scene(this));

        tabbedPane.add(scenes.get(0).getUiTab());

        container.add(controlTable).width(310).height(getHeight());

        activityContainer.add(tabbedPane.getTable()).expandX().fillX().row();
        activityContainer.addSeparator();
        activityContainer.add(activity).grow();
        container.addSeparator(true);
        container.add(activityContainer).grow();

        addActor(container);
    }

    static public void addDragAndDropTarget(final DragAndDrop.Target target) {
        dragAndDrop.addTarget(target);
    }

    static public void addDragAndDropSource(final DragAndDrop.Source source) {
        dragAndDrop.addSource(source);
    }


    @Override
    public void draw() {
        show.draw();
        super.draw();
    }

    @Override
    public void act(float delta) {
        show.act(delta);
        super.act(delta);
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }
}
