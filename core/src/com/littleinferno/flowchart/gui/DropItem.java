package com.littleinferno.flowchart.gui;

import com.kotcrab.vis.ui.widget.PopupMenu;

public abstract class DropItem extends PopupMenu {
    public abstract void init(com.littleinferno.flowchart.scene.Scene scene);

    @Override
    public com.littleinferno.flowchart.scene.Scene getStage() {
        return (com.littleinferno.flowchart.scene.Scene) super.getStage();
    }
}
