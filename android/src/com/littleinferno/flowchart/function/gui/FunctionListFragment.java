package com.littleinferno.flowchart.function.gui;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.FlowchartProject;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutFunctionListBinding;
import com.littleinferno.flowchart.function.AndroidFunctionManager;

public class FunctionListFragment extends DialogFragment {

    LayoutFunctionListBinding layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.DialogList);
        layout = LayoutFunctionListBinding.inflate(inflater.cloneInContext(themeWrapper), container, false);

        AndroidFunctionManager androidFunctionManager = new AndroidFunctionManager(FlowchartProject.getProject());

        androidFunctionManager.createFunction("func1");
        androidFunctionManager.createFunction("func2");
        androidFunctionManager.createFunction("fun1");
        androidFunctionManager.createFunction("fun9");

        FunctionListAdapter adapter = new FunctionListAdapter(androidFunctionManager.getFunctions());
        layout.functions.setAdapter(adapter);
        layout.functions.setLayoutManager(new LinearLayoutManager(getContext()));

        return layout.getRoot();

    }


}
