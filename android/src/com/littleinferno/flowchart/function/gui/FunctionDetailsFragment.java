package com.littleinferno.flowchart.function.gui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.databinding.LayoutFunctionDetailsBinding;

public class FunctionDetailsFragment extends Fragment {

    public static final String FUNCTION_MANAGER_TAG = "FUNCTION_MANAGER";
    public static final String FUNCTION_TAG = "FUNCTION";

    private LayoutFunctionDetailsBinding layout;

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
            bundle.get(FUNCTION_TAG);
        }

    }


}
