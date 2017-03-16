package com.littleinferno.flowchart.gui.item;

import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;

public class ItemListAdaptor extends ArrayListAdapter<Item, VisTable> {

    public ItemListAdaptor(ArrayList<Item> array) {
        super(array);
    }

    void init(){
        view.getScrollPane().setFadeScrollBars(true);
    }

    @Override
    protected VisTable createView(Item item) {
        VisTable table = new VisTable();
        table.add(item).fill();
        return table;
    }
}
