package com.littleinferno.flowchart.variable.gui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutVariableListBinding;
import com.littleinferno.flowchart.variable.AndroidVariableManager;

public class VariableListFragment extends DialogFragment {

    public static final String VARIABLE_MANAGER_TAG = "VARIABLE_MANAGER";
    LayoutVariableListBinding layout;

    private AndroidVariableManager variableManager;
    private VariableListAdapter variableListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.DialogDetails);
        layout = LayoutVariableListBinding.inflate(inflater.cloneInContext(themeWrapper), container, false);

        return layout.getRoot();
    }

    private void createNewVariable(View view) {
        VariableDetailsFragment variableDetails = new VariableDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(VariableDetailsFragment.VARIABLE_MANAGER_TAG, variableManager);
        variableDetails.setArguments(bundle);
        variableDetails.show(getFragmentManager(), "create");
        variableListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null) {
            variableManager = (AndroidVariableManager) bundle.get(VARIABLE_MANAGER_TAG);
        }

        if (variableManager == null)
            throw new RuntimeException("variable manager cannot be null");

        layout.addVariable.setOnClickListener(this::createNewVariable);

        variableListAdapter = new VariableListAdapter(variableManager, getFragmentManager());
        layout.include.variableList.setAdapter(variableListAdapter);
        layout.include.variableList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
