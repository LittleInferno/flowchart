package com.littleinferno.flowchart.function.gui;

import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionParameter;

class FunctionParameterListAdapter extends RecyclerView.Adapter<FunctionParameterListAdapter.ViewHolder> {
    private final AndroidFunction function;
    private final FragmentManager fragmentManager;

    public FunctionParameterListAdapter(AndroidFunction function, FragmentManager fragmentManager) {
        this.function = function;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_parameter, parent, false));
    }

    @Override
    public void onBindViewHolder(FunctionParameterListAdapter.ViewHolder holder, int position) {
        AndroidFunctionParameter afp = function.getParameters().get(position);
        holder.name.setText(afp.getName());
        holder.isArray.setText(String.valueOf(afp.isArray()));
        holder.connection.setText(afp.getConnection().toString());
        holder.type.setText(afp.getDataType().toString());
    }

    @Override
    public int getItemCount() {
        return function.getParameters().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView isArray;
        private final TextView type;
        private final TextView connection;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.parameter_name);
            isArray = (TextView) itemView.findViewById(R.id.parameter_array);
            type = (TextView) itemView.findViewById(R.id.parameter_type);
            connection = (TextView) itemView.findViewById(R.id.parameter_connection);
        }
    }
}
