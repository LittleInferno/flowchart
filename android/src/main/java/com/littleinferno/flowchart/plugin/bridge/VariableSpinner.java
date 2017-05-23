package com.littleinferno.flowchart.plugin.bridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.node.AndroidNode;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Link;
import com.littleinferno.flowchart.variable.Variable;
import com.littleinferno.flowchart.variable.VariableManager;

@SuppressWarnings("unused")
@SuppressLint("ViewConstructor")
public class VariableSpinner extends android.support.v7.widget.AppCompatSpinner implements ViewName {

    public static VariableSpinner make(String name, AndroidNode node, OnSelected selected) {
        return new VariableSpinner(name, node.getFunction().getProject().getVariableManager(),
                node.getContext(), selected);
    }

    public static String getSelected(AndroidNode node, String name) {
        VariableSpinner view = (VariableSpinner) node.getView(name);
        return view != null ? view.variableManager.getVariable(view.getSelectedItemPosition()).getName() : "";
    }

    public static void setSelected(AndroidNode node, String name, String text) {
        VariableSpinner view = (VariableSpinner) node.getView(name);
        if (view != null) {
            int index = Stream.of(view.variableManager.getVariables())
                    .filter(variable -> variable.getName().equals(name))
                    .map(variable -> view.variableManager.getVariables().indexOf(variable))
                    .findSingle()
                    .orElse(0);

            view.setSelection(index);
        }
    }

    private final Link add;
    private final Link remove;
    private final String name;
    private final VariableManager variableManager;

    private VariableSpinner(String name, VariableManager variableManager, Context context, OnSelected onSelected) {
        super(context);
        this.name = name;
        this.variableManager = variableManager;

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                        Stream.of(variableManager.getVariables())
                                .map(Variable::getName)
                                .toList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setAdapter(adapter);

        Fun fun = () -> {
            adapter.clear();
            adapter.addAll(Stream.of(variableManager.getVariables())
                    .map(Variable::getName)
                    .toList());
        };
        add = variableManager.onVariableAdd(fun);
        remove = variableManager.onVariableRemove(fun);

        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onSelected.select(variableManager.getVariable(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                onSelected.select(null);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        add.disconnect();
        remove.disconnect();
    }

    @Override
    public String getName() {
        return name;
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnSelected {
        void select(Variable object);
    }
}
