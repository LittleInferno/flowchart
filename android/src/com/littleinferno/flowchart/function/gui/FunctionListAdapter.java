package com.littleinferno.flowchart.function.gui;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.function.AndroidFunctionManager;

class FunctionListAdapter extends RecyclerView.Adapter<FunctionListAdapter.ViewHolder> {

    private final AndroidFunctionManager functionManager;
    private final FragmentManager fragmentManager;


    public FunctionListAdapter(AndroidFunctionManager functionManager, FragmentManager fragmentManager) {
        this.functionManager = functionManager;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_function_card, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.functionName.setText(functionManager.getFunctions().get(position).getName());
    }

    @Override
    public int getItemCount() {
        return functionManager.getFunctions().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView functionName;

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this::showDetails);
            functionName = (TextView) view.findViewById(R.id.function_name_card);
        }

        private void showDetails(View view) {
            FunctionDetailsFragment functionDetails = new FunctionDetailsFragment();
            Bundle detailsBundle = new Bundle();
            detailsBundle.putParcelable(FunctionDetailsFragment.FUNCTION_MANAGER_TAG, functionManager);
            detailsBundle.putParcelable(FunctionDetailsFragment.FUNCTION_TAG, functionManager.getFunctions().get(getLayoutPosition()));

            functionDetails.setArguments(detailsBundle);

            fragmentManager.beginTransaction().replace(R.id.function_details_layout, functionDetails).commit();
        }
    }
}
