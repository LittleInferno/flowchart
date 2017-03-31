package com.littleinferno.flowchart.function;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.littleinferno.flowchart.R;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


public class ParameterSection extends StatelessSection {

    private final Function function;
    private final boolean isInput;

    public ParameterSection(Function function, boolean isInput) {
        super(0/*R.id.rv_parameters*/, R.layout.function_parameter_layout);
        this.function = function;
        this.isInput = isInput;
    }

    @Override
    public int getContentItemsTotal() {
        return isInput ? function.getInputParameters().size()
                : function.getOutputParameters().size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ParameterViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        FunctionParameter parameter = function.getParameters().get(position);

        ParameterViewHolder parameterHolder = (ParameterViewHolder) holder;

        parameterHolder.parameterName.setText(parameter.getName());
        parameterHolder.parameterType.setText("data type: " + parameter.getName());
        parameterHolder.isArray.setText("is array: " + parameter.isArray());
    }
}
