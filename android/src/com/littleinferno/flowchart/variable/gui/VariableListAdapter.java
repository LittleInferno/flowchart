package com.littleinferno.flowchart.variable.gui;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.util.ResUtil;
import com.littleinferno.flowchart.variable.AndroidVariable;
import com.littleinferno.flowchart.variable.AndroidVariableManager;

public class VariableListAdapter
        extends RecyclerView.Adapter<VariableListAdapter.ViewHolder> {

    private final AndroidVariableManager variableManager;
    private FragmentManager fragmentManager;


    public VariableListAdapter(AndroidVariableManager variableManager, FragmentManager fragmentManager) {
        this.variableManager = variableManager;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_variable, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AndroidVariable variable = variableManager.getVariables().get(position);
        holder.varName.setText(variable.getName());
        holder.varType.setText(variable.getDataType().toString().toLowerCase());
        holder.varType.setTextColor(ResUtil.getDataTypeColor(holder.varType.getContext(), variable.getDataType()));

        holder.varArray.setImageDrawable(ResUtil.getArrayDrawable(holder.varArray.getContext(), variable.isArray()));
    }

    @Override
    public int getItemCount() {
        return variableManager.getVariables().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView varName;
        private final TextView varType;
        private final ImageView varArray;

        public ViewHolder(View itemView) {
            super(itemView);
            varName = (TextView) itemView.findViewById(R.id.variable_name);
            varType = (TextView) itemView.findViewById(R.id.variable_type);
            varArray = (ImageView) itemView.findViewById(R.id.variable_is_array);

            RxView.clicks(itemView).subscribe(o -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(AndroidVariableManager.TAG, variableManager);
                bundle.putParcelable(AndroidVariable.TAG, variableManager.getVariables().get(getLayoutPosition()));

                VariableDetailsFragment variable = new VariableDetailsFragment();
                variable.setArguments(bundle);
                variable.show(fragmentManager, "DETAILS");
                notifyDataSetChanged();
            });
        }
    }
}
