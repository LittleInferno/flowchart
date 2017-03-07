package com.littleinferno.flowchart.util;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;

import java.util.ArrayList;


public abstract class BaseListAdaptor<ItemT, ViewT extends Table> extends ArrayListAdapter<ItemT,ViewT> {
    private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
    private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

    public BaseListAdaptor(ArrayList<ItemT> array) {
        super(array);
        setSelectionMode(SelectionMode.SINGLE);
    }

    @Override
    protected void selectView(ViewT view) {
        view.setBackground(selection);
    }

    @Override
    protected void deselectView(ViewT view) {
        view.setBackground(bg);
    }
}
