package com.littleinferno.flowchart.gui;

import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

public class CreateNewProject extends VisTable {

    CreateNewProject() {

        VisTextField peojectName = new VisTextField();

        add(new VisLabel("Project title: "));
        add(peojectName).fillX().expandX();
    }

//    VisTable buttonTable = new VisTable(true);
//    buttonTable.add(errorLabel).expand().fill();
//    buttonTable.add(cancelButton);
//    buttonTable.add(acceptButton);
//
//    add(new VisLabel("first title: "));
//    add(firstNameField).expand().fill();


}
