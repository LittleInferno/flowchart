package com.littleinferno.flowchart.nui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.littleinferno.flowchart.Function;

public class FunctionItem extends VisTable {

    private final Function function;
    private final FunctionDetails details;

    FunctionItem(String funName) {
        function = new Function(funName);

        Table title = new Table();

        final VisLabel name = new VisLabel(funName);
        name.setEllipsis(true);
        title.add(name).fill();

        final VisTextButton edit = new VisTextButton("ed");
        title.add(edit).size(30).right();

        final VisTextButton del = new VisTextButton("del");
        title.add(del).size(30).right();

        final VisTextButton ret = new VisTextButton("ret");
        title.add(ret).size(30).right();

        details = new FunctionDetails(function);
    }


    public FunctionDetails getDetails() {
        return details;
    }
}
