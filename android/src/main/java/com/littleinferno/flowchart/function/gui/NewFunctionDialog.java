package com.littleinferno.flowchart.function.gui;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.littleinferno.flowchart.databinding.LayoutCreateNewFunctionBinding;
import com.littleinferno.flowchart.function.FunctionManager;

public class NewFunctionDialog extends DialogFragment {

    LayoutCreateNewFunctionBinding layout;
    private FunctionManager functionManager;
    private String nameBuffer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutCreateNewFunctionBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null)
            functionManager = bundle.getParcelable(FunctionManager.TAG);

        RxTextView
                .textChanges(layout.functionName)
                .map(String::valueOf)
                .map(this::checkName)
                .subscribe(b -> layout.create.setEnabled(b));


        layout.discard.setOnClickListener(v -> dismiss());

        layout.create.setOnClickListener(v -> {
            functionManager.createFunction(nameBuffer);
            dismiss();
        });
    }

    private boolean checkName(String s) {
        String result = functionManager.checkFunctionName(s);

        if (result == null) {
            nameBuffer = s;
            layout.functionNameLayout.setErrorEnabled(false);
            return true;
        } else {
            layout.functionNameLayout.setError(result);
            layout.functionNameLayout.setErrorEnabled(true);
            return false;
        }
    }

    public static void show(@NonNull final FunctionManager functionManager,
                            @NonNull final FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(FunctionManager.TAG, functionManager);

        NewFunctionDialog functionDialog = new NewFunctionDialog();
        functionDialog.setArguments(bundle);
        functionDialog.show(fragmentManager, "NEW_FUNCTION");
    }
}
