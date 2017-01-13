package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;


public class TabbedPane extends Table {

    private final Stack contentStack;
    private static HorizontalGroup openedWindows;
    private final Skin skin;
    private final ButtonGroup<Tab> buttonGroup;
    private final Array closedWindow;

    public static class Tab extends TextButton {

        Tab(String name, Skin skin) {
            super(name, skin);
            setSize(70, 15);

            contentTable = new Table();

            addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    hideTabs(Tab.this);
                }
            });
        }

        public Table getContentTable() {
            return contentTable;
        }

        private Table contentTable;
    }

    TabbedPane(Skin skin) {
        this.skin = skin;
        top();
        setFillParent(true);

        DragAndDrop dnd = new DragAndDrop();

        openedWindows = new HorizontalGroup();
        add(openedWindows).expandX().fillX().row();

        contentStack = new Stack();
        add(contentStack).fill().expand();
        buttonGroup = new ButtonGroup<Tab>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);

        closedWindow = new Array();

        dnd.setDragActorPosition(0, 0);
        dnd.addSource(new DragAndDrop.Source(openedWindows) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {

                DragAndDrop.Payload payload = new DragAndDrop.Payload();

                SnapshotArray<Actor> array = openedWindows.getChildren();

                Actor actor;
                for (int i = 0; i < array.size; ++i) {

                    actor = array.get(i);

                    if (x > actor.getX() && x < actor.getWidth() + actor.getX() && y > actor.getY() && y < actor.getHeight() + actor.getY()) {
                        payload.setObject(actor);
                        payload.setDragActor(actor);
                        openedWindows.removeActor(actor);
                        return payload;
                    }
                }
                return null;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {

                if (target == null) {
                    Tab tab = (Tab) payload.getObject();
                    openedWindows.addActor(tab);
                    tab.setChecked(true);
                }
            }
        });

        dnd.addTarget(new DragAndDrop.Target(openedWindows) {

            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                SnapshotArray<Actor> array = openedWindows.getChildren();

                Tab tab = (Tab) payload.getObject();

                Actor actor;
                for (int i = 0; i < array.size; ++i) {
                    actor = array.get(i);

                    if (x > actor.getX() && x < actor.getWidth() + actor.getX() && y > actor.getY() && y < actor.getHeight() + actor.getY()) {
                        openedWindows.addActorBefore(actor, tab);
                        tab.setChecked(true);
                        return;
                    }
                }
                openedWindows.addActor(tab);
                tab.setChecked(true);
            }
        });
    }

    private static void hideTabs(Tab opened) {
        SnapshotArray array = openedWindows.getChildren();

        Tab actor;
        for (int i = 0; i < array.size; ++i) {
            actor = (Tab) array.get(i);

            if (actor != opened)
                actor.getContentTable().setVisible(false);
            else
                actor.getContentTable().setVisible(true);
        }
    }

    Tab addTab(String name) {

        final Tab tab = new Tab(name, skin);
        contentStack.add(tab.getContentTable());
        Main.getDND().addTarget(new DragAndDrop.Target(tab.getContentTable()) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Actor node = (Actor) payload.getObject();
                node.setPosition(x, y);
                tab.getContentTable().addActor(node);
            }
        });

        buttonGroup.add(tab);
        openedWindows.addActor(tab);
        tab.setChecked(true);

        return tab;
    }

}
