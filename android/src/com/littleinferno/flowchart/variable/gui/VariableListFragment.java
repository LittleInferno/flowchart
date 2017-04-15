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

    LayoutVariableListBinding layout;

    private AndroidVariableManager variableManager;
    private VariableListAdapter variableListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.DialogList);
        layout = LayoutVariableListBinding.inflate(inflater.cloneInContext(themeWrapper), container, false);

        layout = LayoutVariableListBinding.inflate(inflater, container, false);
        layout.addVariable.setOnClickListener(this::createNewVariable);

        variableListAdapter = new VariableListAdapter(variableManager.getVariables());
        variableListAdapter.setEventListener(new EventListener());
        layout.include.variableList.setAdapter(variableListAdapter);
        layout.include.variableList.setLayoutManager(new LinearLayoutManager(getContext()));

        return layout.getRoot();
    }

    private void createNewVariable(View view) {
        VariableDetailsFragment variableDetails = new VariableDetailsFragment();
        variableDetails.setVariableManager(variableManager);
        variableDetails.setVariableListAdapter(variableListAdapter);
        variableDetails.show(getFragmentManager(), "create");
    }

    public AndroidVariableManager getVariableManager() {
        return variableManager;
    }

    public void setVariableManager(AndroidVariableManager variableManager) {
        this.variableManager = variableManager;
    }

    private class EventListener implements VariableListAdapter.EventListener {
        @Override
        public void onItemRemoved(int position) {
        }

        @Override
        public void onItemPinned(int position) {
        }

        @Override
        public void onItemViewClicked(View v, boolean pinned) {

        }
    }
}
