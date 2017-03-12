package com.littleinferno.flowchart.jsutil;

import com.littleinferno.flowchart.gui.ControlTable;

public class IO {

    public static void print(String value) {
        ControlTable.getConsole().append(value, true);
    }
//
//    public static void printArray(NativeArray value) {
//        ControlTable.getConsole().append(Arrays.toString(value.toArray()), true);
//    }

}
