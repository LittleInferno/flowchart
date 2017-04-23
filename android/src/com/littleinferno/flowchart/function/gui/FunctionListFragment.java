package com.littleinferno.flowchart.function.gui;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutFunctionListBinding;
import com.littleinferno.flowchart.function.AndroidFunctionManager;

public class FunctionListFragment extends DialogFragment {

    LayoutFunctionListBinding layout;
    private AndroidFunctionManager functionManager;
    private FunctionListAdapter functionListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutFunctionListBinding.inflate(inflater, container, false);

        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL;

        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null) {
            functionManager = bundle.getParcelable(AndroidFunctionManager.TAG);
        }

        if (functionManager == null)
            throw new RuntimeException("function manager cannot be null");

        layout.addFunction.setOnClickListener(this::createNewFunction);

        FunctionListAdapter adapter = new FunctionListAdapter(functionManager, getChildFragmentManager());
        layout.fun.items.setAdapter(adapter);
        layout.fun.items.setLayoutManager(new LinearLayoutManager(getContext()));

        FunctionDetailsFragment functionDetails = new FunctionDetailsFragment();
        Bundle detailsBundle = new Bundle();
        detailsBundle.putParcelable(FunctionDetailsFragment.FUNCTION_MANAGER_TAG, functionManager);
        detailsBundle.putParcelable(FunctionDetailsFragment.FUNCTION_TAG, functionManager.getFunctions().get(0));

        functionDetails.setArguments(detailsBundle);

        getChildFragmentManager().beginTransaction().replace(R.id.function_details_layout, functionDetails).commit();
    }

    private void createNewFunction(View view) {
        NewFunctionDialog functionDialog = new NewFunctionDialog();

        functionDialog.setArguments(getArguments());
        functionDialog.show(getFragmentManager(), "CREATE");
    }
}
