package com.littleinferno.flowchart.nui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;

public class FunctionTable extends Table {


    private VisList<FunctionItem> functions;
    private int counter = 0;

    public FunctionTable() {
        functions = new VisList<FunctionItem>();
        add(functions).fill().expand().row();


        final VisTable details = new VisTable();
        VisScrollPane scrollDetails = new VisScrollPane(details);
        Table detailsContainer = new Table();
        detailsContainer.add(scrollDetails).expand().fillX().top();
        add(detailsContainer).fill();


        functions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                details.clearChildren();

                details.add(functions.getSelected().getDetails()).fill().expand();

            }
        });
    }


    public void addFunction() {
        functions.getItems().add(new FunctionItem("newFun" + counter++));
    }

}
