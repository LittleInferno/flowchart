package com.littleinferno.flowchart.function;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.R;


public class ParameterAdaptors extends RecyclerView.Adapter<ParameterViewHolder> {

    private AndroidFunction function;

    public ParameterAdaptors(AndroidFunction function) {
        this.function = function;
    }

    @Override
    public ParameterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.function_parameter_layout, parent, false);

        return new ParameterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ParameterViewHolder holder, int position) {
        FunctionParameter parameter = function.getParameters().get(position);
        holder.parameterName.setText(parameter.getName());
        holder.parameterType.setText("data type: " + parameter.getName());
        holder.isArray.setText("is array: " + parameter.isArray());
    }

    @Override
    public int getItemCount() {
        return function.getParameters().size();
    }

}
