package com.littleinferno.flowchart.variable.gui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.littleinferno.flowchart.databinding.LayoutVariableListBinding;
import com.littleinferno.flowchart.variable.AndroidVariableManager;

public class VariableListFragment extends DialogFragment {

    LayoutVariableListBinding layout;
    private AndroidVariableManager variableManager;
    private VariableListAdapter variableListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutVariableListBinding.inflate(inflater, container, false);

        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL;

        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null) {
            variableManager = (AndroidVariableManager) bundle.get(AndroidVariableManager.TAG);
        }

        if (variableManager == null)
            throw new RuntimeException("variable manager cannot be null");

        layout.addVariable.setOnClickListener(this::createNewVariable);

        variableListAdapter = new VariableListAdapter(variableManager, getFragmentManager());

        layout.include.variableList.setAdapter(variableListAdapter);
        layout.include.variableList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void createNewVariable(View view) {
        VariableDetailsFragment variableDetails = new VariableDetailsFragment();
        variableDetails.setArguments(getArguments());
        variableDetails.show(getFragmentManager(), "create");
    }
}
