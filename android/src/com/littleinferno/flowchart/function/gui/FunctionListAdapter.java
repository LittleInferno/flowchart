package com.littleinferno.flowchart.function.gui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.function.AndroidFunction;

import java.util.List;

public class FunctionListAdapter extends RecyclerView.Adapter<FunctionListAdapter.ViewHolder> {

    private final List<AndroidFunction> functions;

    FunctionListAdapter(List<AndroidFunction> functions) {
        this.functions = functions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_function_card, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.functionName.setText(functions.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return functions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView functionName;

        public ViewHolder(View view) {
            super(view);

            functionName = (TextView) view.findViewById(R.id.function_name_card);
        }
    }
}
