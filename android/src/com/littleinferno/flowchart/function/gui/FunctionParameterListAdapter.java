package com.littleinferno.flowchart.function.gui;

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
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionParameter;
import com.littleinferno.flowchart.util.ResUtil;

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
        holder.type.setText(afp.getDataType().toString().toLowerCase());
        holder.type.setTextColor(ResUtil.getDataTypeColor(holder.type.getContext(), afp.getDataType()));

        holder.isArray.setImageDrawable(ResUtil.getArrayDrawable(holder.isArray.getContext(), afp.isArray()));
    }

    @Override
    public int getItemCount() {
        return function.getParameters().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView type;
        private final ImageView isArray;

        ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.parameter_name);
            type = (TextView) itemView.findViewById(R.id.parameter_type);
            isArray = (ImageView) itemView.findViewById(R.id.parameter_is_array);

            RxView.clicks(itemView).subscribe(o -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(AndroidFunction.TAG, function);

                bundle.putParcelable(AndroidFunctionParameter.TAG, function.getParameters().get(getLayoutPosition()));

                FunctionParameterDetailsFragment function = new FunctionParameterDetailsFragment();
                function.setArguments(bundle);
                function.show(fragmentManager, "DETAILS");
                notifyDataSetChanged();
            });

        }
    }
}
