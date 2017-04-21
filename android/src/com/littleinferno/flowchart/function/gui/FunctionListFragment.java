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

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutFunctionListBinding;
import com.littleinferno.flowchart.function.AndroidFunctionManager;

public class FunctionListFragment extends DialogFragment {

    public static final String FUNCTION_MANAGER_TAG = "FUNCTION_MANAGER";

    LayoutFunctionListBinding layout;
    private AndroidFunctionManager functionManager;
    private FunctionListAdapter functionListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.DialogDetails);
        layout = LayoutFunctionListBinding.inflate(inflater.cloneInContext(themeWrapper), container, false);

        return layout.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null) {
            functionManager = bundle.getParcelable(FUNCTION_MANAGER_TAG);
        }

        if (functionManager == null)
            throw new RuntimeException("function manager cannot be null");

        layout.addFunction.setOnClickListener(this::createNewFunction);

        FunctionListAdapter adapter = new FunctionListAdapter(functionManager, getChildFragmentManager());
        layout.fun.items.setAdapter(adapter);
        layout.fun.items.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void createNewFunction(View view) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(NewFunctionDialog.FUNCTION_MANAGER_TAG, functionManager);

        NewFunctionDialog functionDialog = new NewFunctionDialog();
        functionDialog.setArguments(bundle);

        functionDialog.show(getFragmentManager(), "CREATE");

    }


}
