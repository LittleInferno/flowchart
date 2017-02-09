package com.littleinferno.flowchart.gui;

import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;

public class ConsoleTable extends VisTable {

    private VisTextArea textArea;

    ConsoleTable() {

        textArea = new VisTextArea("");
        textArea.setReadOnly(true);
        add(textArea).grow();
    }


    public void append(String string) {
        append(string, false);
    }

    public void append(String string, boolean newLine) {
        textArea.appendText(string);
        if (newLine)
            textArea.appendText("\n");
    }
}
