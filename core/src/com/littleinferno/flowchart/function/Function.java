package com.littleinferno.flowchart.function;

import com.annimon.stream.Stream;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.building.OneColumnTableBuilder;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.gui.FunctionScene;
import com.littleinferno.flowchart.node.FunctionBeginNode;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.util.DataSelectBox;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.InputForm;
import com.littleinferno.flowchart.util.NameChangedListener;

import java.util.ArrayList;
import java.util.List;


public class Function implements Json.Serializable {

    private String name;

    private List<FunctionReturnNode> returnNodes;

    private List<FunctionParameter> parameters;

    private List<NameChangedListener> nameChangedListeners;
    private List<DestroyListener> destroyListeners;

    private List<FunctionParameter.Added> parameterAddedListeners;
    private List<FunctionParameter.Removed> parameterRemovedListeners;
    private GenerateListener generateListener;

    private FunctionDetailsTable functionDetailsTable;

    private FunctionScene scene;

    Function() {
    }

    public Function(String name) {
        this.name = name;

        parameters = new ArrayList<>();

        nameChangedListeners = new ArrayList<>();
        destroyListeners = new ArrayList<>();

        parameterAddedListeners = new ArrayList<>();
        parameterRemovedListeners = new ArrayList<>();

        functionDetailsTable = new FunctionDetailsTable(this);

        returnNodes = new ArrayList<>();

        scene = new FunctionScene(this);

        FunctionBeginNode beginNode = new FunctionBeginNode(this);
        beginNode.setPosition(350, 250);

        scene.addActor(beginNode);

        FunctionReturnNode returnNode = new FunctionReturnNode(this);
        returnNode.setPosition(600, 250);
        returnNode.removeCloseButton();

        scene.addActor(returnNode);

    }

    public List<FunctionParameter> getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        notifyListenersNameChanged(name);
    }

    public void removeReturnNode(FunctionReturnNode node) {
        returnNodes.remove(node);
        if (returnNodes.size() == 1)
            returnNodes.get(0).removeCloseButton();
    }

    public FunctionParameter addParameter(String name, DataType type, Connection connection, boolean isArray) {
        FunctionParameter parameter = new FunctionParameter(name, type, connection, isArray);

        notifyListenersParameterAdded(parameter);
        parameters.add(parameter);

        return parameter;
    }

    public void removeParameter(FunctionParameter parameter) {
        notifyListenersParameterRemoved(parameter);
        parameters.remove(parameter);
    }

    public void addListener(NameChangedListener listener) {
        nameChangedListeners.add(listener);
    }

    public void addListener(DestroyListener listener) {
        destroyListeners.add(listener);
    }

    public void addPareameterListener(FunctionParameter.Added added, FunctionParameter.Removed removed) {
        parameterAddedListeners.add(added);
        parameterRemovedListeners.add(removed);
    }

    public void setGenerateListener(GenerateListener listener) {
        generateListener = listener;
    }

    private void notifyListenersNameChanged(String newName) {
        Stream.of(nameChangedListeners).forEach(var -> var.changed(newName));
    }

    private void notifyListenersDestroed() {
        Stream.of(destroyListeners).forEach(DestroyListener::destroyed);
    }

    private void notifyListenersParameterAdded(FunctionParameter parameter) {
        Stream.of(parameterAddedListeners).forEach(var -> var.add(parameter));
    }

    private void notifyListenersParameterRemoved(FunctionParameter parameter) {
        Stream.of(parameterRemovedListeners).forEach(var -> var.remove(parameter));
    }

    public void addReturnNode(FunctionReturnNode returnNode) {
        returnNodes.add(returnNode);
        returnNode.addCloseButton();
    }

    public void applyParameters() {
        if (!parameterAddedListeners.isEmpty()) {
            FunctionParameter.Added listener = parameterAddedListeners.get(parameterAddedListeners.size() - 1);
            Stream.of(parameters).forEach(listener::add);
        }
    }

    public String gen(BaseCodeGenerator builder) {
        return generateListener.gen(builder);
    }

    public FunctionDetailsTable getTable() {
        return functionDetailsTable;
    }

    void destroy() {
        notifyListenersDestroed();
    }

    public FunctionScene getScene() {
        return scene;
    }

    @Override
    public void write(Json json) {

        json.writeObjectStart();
        json.writeValue("name", name);
        json.writeArrayStart("parameters");

        for (FunctionParameter fp : parameters) {
            json.writeObjectStart();
            json.writeField(fp, "name", "name");
            json.writeValue("type", fp.getDataType());
            json.writeField(fp, "isArray", "isArray");
            json.writeValue("connection", fp.getConnection());
            json.writeObjectEnd();
        }

        json.writeArrayEnd();
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

        name = jsonData.get("name").asString();

        JsonValue vars = jsonData.get("parameters");
        for (JsonValue element : vars) {
            parameters.add(json.readValue(FunctionParameter.class, element));
        }

    }

    private class FunctionDetailsTable extends VisTable {

        Function function;
        private int counter;

        private final VerticalGroup input = new VerticalGroup();
        private final VerticalGroup output = new VerticalGroup();


        FunctionDetailsTable(Function function) {
            super(true);
            this.function = function;
            this.counter = 0;
            initTable();
        }

        private void initTable() {
            top();
            VisTextButton functionName = new VisTextButton(function.getName());
            functionName.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    float w = getStage().getWidth() / 2;
                    float h = getStage().getHeight() / 2;
                    InputForm inputForm =
                            new InputForm("function name", function::getName, function::setName);
                    inputForm.setPosition(w - inputForm.getWidth() / 2, h);
                    getStage().addActor(inputForm);
                    inputForm.focus();
                }
            });

            function.addListener(functionName::setText);

            final VisImageButton deleteFunction = new VisImageButton("close");

            deleteFunction.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Project.instance().getFunctionManager().removeFunction(function);
                }
            });

            VisTable header = new VisTable();
            header.add(new VisLabel("name: ")).growY();
            header.add(functionName).grow();
            header.add(deleteFunction).row();


            add(header).growX().row();
            addSeparator();


            VisTable container = new VisTable();
            initCollapsible(Connection.INPUT, container, input);
            container.addSeparator();
            initCollapsible(Connection.OUTPUT, container, output);


            Array<VisTable> array = new Array<>();
            array.add(container);

            ArrayAdapter<VisTable, VisTable> adapter = new ArrayAdapter<VisTable, VisTable>(array) {
                @Override
                protected VisTable createView(VisTable item) {
                    return item;
                }
            };

            ListView<VisTable> view = new ListView<>(adapter);
            add(view.getMainTable()).grow();
        }

        private void initCollapsible(final Connection connection,
                                     final VisTable container,
                                     final VerticalGroup widget) {

            final String title = connection == Connection.INPUT ? "inputs" : "outputs";
            VisTextButton label = new VisTextButton(title, "toggle");

            final VisTextButton addParameter = new VisTextButton("add");
            label.toggle();

            final VisTable widgetContainer = new VisTable();
            widgetContainer.add(widget).grow();

            OneColumnTableBuilder builder = new OneColumnTableBuilder();

            final CollapsibleWidget collapsible = new CollapsibleWidget(widgetContainer);

            builder
                    .append(
                            CellWidget.of(label).expandX().fillX().wrap(),
                            CellWidget.of(addParameter).wrap())
                    .row()
                    .append(
                            CellWidget.of(collapsible).expandX().fillX().wrap());

            container.add(builder.build()).growX().row();

            addParameter.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    widget.addActor(new ParameterTable("newParam" + counter++, connection, function));
                }
            });

            label.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    collapsible.setCollapsed(!collapsible.isCollapsed());
                }
            });
        }

        @Override
        public String getName() {
            return function.getName();
        }
    }

    private static class ParameterTable extends VisTable {

        private Function function;
        private FunctionParameter parameter;

        ParameterTable(String name, Connection connection, Function function) {
            this.function = function;
            this.parameter = function.addParameter(name, DataType.BOOL, connection, false);

            initTable();
        }

        private void initTable() {
            final VisValidatableTextField parameterName = new VisValidatableTextField(parameter.getName());
            parameterName.addValidator(new InputValidator() {
                List<FunctionParameter> parameters = function.getParameters();

                @Override
                public boolean validateInput(String input) {

                    for (FunctionParameter par : parameters)
                        if (par.getName().equals(input) && par != parameter)
                            return false;

                    return input.matches("([_A-Za-z][_A-Za-z0-9]*)");
                }
            });

            parameterName.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (parameterName.isInputValid()) {
                        parameter.setName(parameterName.getText());
                    }
                }
            });


            final DataSelectBox parameterType = new DataSelectBox();

            parameterType.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    parameter.setDataType(parameterType.getSelected());
                }
            });


            VisImageButton.VisImageButtonStyle style =
                    VisUI.getSkin().get("toggle", VisImageButton.VisImageButtonStyle.class);

            Drawable drawable = VisUI.getSkin().getDrawable("array");

            style.imageChecked = drawable;
            style.imageUp = drawable;

            final VisImageButton isArray = new VisImageButton(style);
            isArray.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    parameter.setArray(isArray.isChecked());
                }
            });

            VisImageButton delete = new VisImageButton("close");
            delete.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    function.removeParameter(parameter);
                    getParent().removeActor(ParameterTable.this, true);
                }
            });

            add(parameterName).grow();
            add(parameterType).grow();
            add(isArray);
            add(delete);
        }
    }

    public interface GenerateListener {
        String gen(BaseCodeGenerator builder);
    }

}
