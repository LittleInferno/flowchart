package com.littleinferno.flowchart.codegen;

import com.littleinferno.flowchart.gui.ControlTable;

public class IO {

    public static void print(String value) {
        ControlTable.getConsole().append(value, true);
    }
}
