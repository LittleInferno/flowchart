package com.littleinferno.flowchart.function.gui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.littleinferno.flowchart.databinding.LayoutCreateNewFunctionBinding;
import com.littleinferno.flowchart.function.AndroidFunctionManager;

public class NewFunctionDialog extends DialogFragment {

    LayoutCreateNewFunctionBinding layout;
    private AndroidFunctionManager functionManager;
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
            functionManager = bundle.getParcelable(AndroidFunctionManager.TAG);

        RxTextView
                .textChanges(layout.functionName)
                .map(String::valueOf)
                .map(this::checkName)
                .subscribe(b -> layout.create.setEnabled(b));


        RxView.clicks(layout.discard).subscribe(v -> dismiss());

        RxView.clicks(layout.create).subscribe(v -> {
            createFunction();
            dismiss();
        });
    }

    private void createFunction() {
        functionManager.createFunction(nameBuffer);
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
}
