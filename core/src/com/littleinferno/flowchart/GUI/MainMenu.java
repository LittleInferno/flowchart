package com.littleinferno.flowchart.gui;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

public class MainMenu extends Stage {

    private final VisTable main;

    public MainMenu() {
        VisUI.load();

        main = new VisTable();
        main.setFillParent(true);


        VisTextButton createNew = new VisTextButton("create new");
    //    main.add(createNew).width(300).row();

        //main.add(new FunctionDetails()).top();

        main.addSeparator(true);

        VisTextButton newB = new VisTextButton("B new");

        //  main.add(newB).width(300);


        VisWindow visWindow = new VisWindow("create new", false);

        main.add(new CreateNewProject()).fill().expand();


        this.addActor(main);



         //  setDebugAll(true);
    }


    @Override
    public void dispose() {
        VisUI.dispose();
        super.dispose();
    }
}
