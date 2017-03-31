package com.littleinferno.flowchart.function;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.littleinferno.flowchart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParameterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_parameter_name)
    TextView parameterName;

    @BindView(R.id.tv_data_type)
    TextView parameterType;

    @BindView(R.id.tv_is_array)
    TextView isArray;

    public ParameterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
