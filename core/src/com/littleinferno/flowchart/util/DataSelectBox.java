package com.littleinferno.flowchart.util;

import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.littleinferno.flowchart.DataType;


public class DataSelectBox extends VisSelectBox<DataType> {

    public DataSelectBox() {

        setItems(DataType.BOOL,
                DataType.FLOAT,
                DataType.INT,
                DataType.STRING);
    }
}
