package com.littleinferno.flowchart.nui.function;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.parameter.Parameter;
import com.littleinferno.flowchart.ui.Main;


class FunctionDetails extends VisTable {

    Function function;
    FunctionTable table;
    private int counter = 0;
    private final VerticalGroup input = new VerticalGroup();
    private final VerticalGroup output = new VerticalGroup();


    FunctionDetails(FunctionTable table, Function function) {
        super(true);
        this.table = table;
        this.function = function;
        init();
    }

    private void init() {

        top();

        final VisValidatableTextField functionName = new VisValidatableTextField(function.getName());
        functionName.addValidator(new InputValidator() {
            @Override
            public boolean validateInput(String input) {
                return input.matches("([_A-Za-z][_A-Za-z0-9]*)");
            }
        });

        functionName.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (functionName.isInputValid()) {
                    function.setName(functionName.getText());
                }
            }
        });

        final VisImageButton deleteFunction = new VisImageButton("close");

        deleteFunction.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("deleted", toString());
                function.delete();
                table.deleteFunction(FunctionDetails.this);
            }
        });

        VisTable header = new VisTable();
        header.add(new VisLabel("name: "));
        header.add(functionName).growX();
        header.add(deleteFunction).row();
        add(header).growX().row();
        addSeparator();


        VisTable container = new VisTable();
        initCollapsible(Connection.INPUT, container, input);
        container.addSeparator();
        initCollapsible(Connection.OUTPUT, container, output);


        Array<VisTable> array = new Array<VisTable>();
        array.add(container);

        ArrayAdapter<VisTable, VisTable> adapter = new ArrayAdapter<VisTable, VisTable>(array) {
            @Override
            protected VisTable createView(VisTable item) {
                return item;
            }
        };

        ListView<VisTable> view = new ListView<VisTable>(adapter);
        add(view.getMainTable()).grow();
    }

    private void initCollapsible(final Connection connection,
                                 final VisTable container,
                                 final VerticalGroup widget) {

        final String title = connection == Connection.INPUT ? "inputs" : "outputs";
        VisTextButton label = new VisTextButton(title, "toggle");

        final VisTextButton addParameter = new VisTextButton("add");
        label.toggle();

        VisTable header = new VisTable();

        header.add(label).growX();
        header.addSeparator(true);
        header.add(addParameter).row();


        final VisTable widgetContainer = new VisTable();
        widgetContainer.add(widget).grow();
        final CollapsibleWidget collapsible = new CollapsibleWidget(widgetContainer);

        container.add(header).growX().row();
        container.addSeparator();
        container.add(collapsible).growX().row();

        addParameter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                widget.addActor(new ParameterTable(connection));
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

    private class ParameterTable extends VisTable {

        ParameterTable(Connection connection) {
            final Parameter parameter = new Parameter("newOutParam" + counter++, DataType.BOOL, connection, false);
            function.addParameter(parameter);

            final VisValidatableTextField parameterName = new VisValidatableTextField(parameter.getName());
            parameterName.addValidator(new InputValidator() {
                Array<Parameter> parameters = function.getParameters();

                @Override
                public boolean validateInput(String input) {

                    for (Parameter par : parameters)
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


            final VisSelectBox<DataType> parameterType = new VisSelectBox<DataType>();

            parameterType.setItems(DataType.BOOL,
                    DataType.FLOAT,
                    DataType.INT,
                    DataType.STRING);

            parameterType.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    parameter.setDataType(parameterType.getSelected());
                }
            });


            VisImageButton.VisImageButtonStyle style =
                    VisUI.getSkin().get("toggle", VisImageButton.VisImageButtonStyle.class);
            style.imageChecked = Main.skin.getDrawable("array");
            style.imageUp = Main.skin.getDrawable("array");


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

            add(parameterName).growX().expandY();
            add(parameterType).growX().expandY();
            add(isArray);
            add(delete);
        }
    }
}
