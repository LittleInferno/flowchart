package com.littleinferno.flowchart.plugin.bridge;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionManager;
import com.littleinferno.flowchart.node.AndroidNode;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Link;

@SuppressWarnings("unused")
@SuppressLint("ViewConstructor")
public class FunctionSpinner extends android.support.v7.widget.AppCompatSpinner {

    private final Link add;
    private final Link remove;

    private FunctionSpinner(AndroidFunctionManager functionManager, OnSelected onSelected) {
        super(functionManager.getProject().getContext());

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                        Stream.of(functionManager.getFunctions())
                                .map(AndroidFunction::getName)
                                .toList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setAdapter(adapter);

        Fun fun = () -> {
            adapter.clear();
            adapter.addAll(Stream.of(functionManager.getFunctions())
                    .map(AndroidFunction::getName)
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
        void select(AndroidFunction object);
    }

    public static FunctionSpinner make(AndroidNode node, OnSelected selected) {
        return new FunctionSpinner(node.getFunction().getFunctionManager(), selected);
    }
}
