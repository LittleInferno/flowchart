package com.littleinferno.flowchart.plugin.bridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.function.FunctionManager;
import com.littleinferno.flowchart.node.AndroidNode;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Link;

@SuppressWarnings("unused")
@SuppressLint("ViewConstructor")
public class FunctionSpinner extends android.support.v7.widget.AppCompatSpinner implements ViewName {

    public static FunctionSpinner make(String name, AndroidNode node, OnSelected selected) {
        return new FunctionSpinner(name, node.getFunction().getFunctionManager(),
                node.getContext(), selected);
    }

    public static String getSelected(AndroidNode node, String name) {
        FunctionSpinner view = (FunctionSpinner) node.getView(name);
        return view != null ? view.functionManager.getFunction(view.getSelectedItemPosition()).getName() : "";
    }

    public static void setSelected(AndroidNode node, String name, String text) {
        FunctionSpinner view = (FunctionSpinner) node.getView(name);
        if (view != null) {
            int index = Stream.of(view.functionManager.getFunctions())
                    .filter(variable -> variable.getName().equals(name))
                    .map(variable -> view.functionManager.getFunctions().indexOf(variable))
                    .findSingle()
                    .orElse(0);

            view.setSelection(index);
        }
    }

    private final Link add;
    private final Link remove;
    private final String name;
    private final FunctionManager functionManager;

    private FunctionSpinner(String name, FunctionManager functionManager, Context context, OnSelected onSelected) {
        super(context);
        this.name = name;
        this.functionManager = functionManager;

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                        Stream.of(functionManager.getFunctions())
                                .map(Function::getName)
                                .toList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setAdapter(adapter);

        Fun fun = () -> {
            adapter.clear();
            adapter.addAll(Stream.of(functionManager.getFunctions())
                    .map(Function::getName)
                    .toList());
        };
        add = functionManager.onFunctionAdd(fun);
        remove = functionManager.onFunctionRemove(fun);

        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onSelected.select(functionManager.getFunctions().get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        add.disconnect();
        remove.disconnect();
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnSelected {
        void select(Function object);
    }

    @Override
    public String getName() {
        return name;
    }
}
