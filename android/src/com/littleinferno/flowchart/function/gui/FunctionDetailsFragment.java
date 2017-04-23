package com.littleinferno.flowchart.function.gui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.databinding.LayoutFunctionDetailsBinding;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionManager;

public class FunctionDetailsFragment extends Fragment {

    public static final String FUNCTION_MANAGER_TAG = "FUNCTION_MANAGER";
    public static final String FUNCTION_TAG = "FUNCTION";

    private LayoutFunctionDetailsBinding layout;
    private AndroidFunctionManager functionManager;
    private AndroidFunction function;
    private FunctionParameterListAdapter parameterListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutFunctionDetailsBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null) {
            functionManager = bundle.getParcelable(FUNCTION_MANAGER_TAG);
            function = bundle.getParcelable(FUNCTION_TAG);
        }

        if (functionManager == null)
            throw new RuntimeException("function manager cannot be null");

        if (function == null) {
            throw new RuntimeException("function cannot be null");
        }

        layout.functionName.setText(function.getName());
        layout.addParameter.setOnClickListener(this::createNewParameter);

        parameterListAdapter = new FunctionParameterListAdapter(function, getFragmentManager());
        layout.parameters.items.setAdapter(parameterListAdapter);
        layout.parameters.items.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void createNewParameter(View view) {
        FunctionParameterDetailsFragment parameterDetails = new FunctionParameterDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidFunction.TAG, function);
        parameterDetails.setArguments(bundle);
        parameterDetails.show(getFragmentManager(), "create");
    }
}
