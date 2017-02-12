package com.littleinferno.flowchart.function;

import com.annimon.stream.Stream;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.gui.FunctionItem;
import com.littleinferno.flowchart.gui.SceneUi;

import java.util.ArrayList;

public class FunctionManager {

    private ArrayListAdapter<Function, VisTable> functions;
    private final VisTable detailsTable;
    private final VisTable funTable;

    private int counter;

    private SceneUi sceneUi;

    public FunctionManager(SceneUi sceneUi) {
        this.sceneUi = sceneUi;
        functions = new FunctionManager.FunctionListAdapter(new ArrayList<>());
        detailsTable = new VisTable(true);
        funTable = new VisTable(true);
        counter = 0;

        ListView<Function> view = new ListView<>(functions);

        view.setItemClickListener(item -> {
            detailsTable.clearChildren();
            detailsTable.add(item.getTable()).grow();
            sceneUi.pinToTabbedPane(item.getScene().getUiTab());
        });

        funTable.add(view.getMainTable()).grow().row();
    }

    public Function createFunction() {
        Function function = new Function("newFun" + counter++, sceneUi);

        functions.add(function);

        sceneUi.pinToTabbedPane(function.getScene().getUiTab());

        return function;
    }

    void removeFunction(Function function) {
        function.destroy();

        sceneUi.unpinFromTabbedPane(function.getScene().getUiTab());
        detailsTable.clearChildren();
        functions.remove(function);
    }

    public VisTable getFunTable() {
        return funTable;
    }

    public VisTable getDetailsTable() {
        return detailsTable;
    }

    public String gen(CodeBuilder builder) {
        final StringBuilder stringBuilder = new StringBuilder();

        Stream.of(functions.iterable()).forEach(function -> stringBuilder.append(function.gen(builder)));

        return stringBuilder.toString();
    }

    private class FunctionListAdapter extends ArrayListAdapter<Function, VisTable> {
        private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

        FunctionListAdapter(ArrayList<Function> array) {
            super(array);
            setSelectionMode(SelectionMode.SINGLE);
        }

        @Override
        protected void selectView(VisTable view) {
            view.setBackground(selection);
        }

        @Override
        protected void deselectView(VisTable view) {
            view.setBackground(bg);
        }

        @Override
        protected VisTable createView(Function item) {

            final VisTable table = new VisTable();
            final VisLabel it = new VisLabel(item.getName());

            item.addListener(it::setText);

            sceneUi.addDragAndDropSource(new DragAndDrop.Source(table) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();

                    payload.setObject(new FunctionItem(item));
                    payload.setDragActor(new VisLabel(it.getText()));

                    deselectView(table);
                    return payload;
                }
            });

            table.add(it).grow();
            return table;
        }
    }

}
