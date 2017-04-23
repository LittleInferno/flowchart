package com.littleinferno.flowchart.variable.gui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutVariableDetailsBinding;
import com.littleinferno.flowchart.util.Objects;
import com.littleinferno.flowchart.variable.AndroidVariable;
import com.littleinferno.flowchart.variable.AndroidVariableManager;

public class VariableDetailsFragment extends DialogFragment {

    LayoutVariableDetailsBinding layout;

    private boolean isArrayBuffer;
    private DataType dataTypeBuffer;
    private String nameBuffer;

    private AndroidVariable variable;

    private AndroidVariableManager variableManager;

    public VariableDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.DialogDetails);
        layout = LayoutVariableDetailsBinding.inflate(inflater.cloneInContext(themeWrapper), container, false);

        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null) {
            variableManager = bundle.getParcelable(AndroidVariableManager.TAG);
            variable = bundle.getParcelable(AndroidVariable.TAG);
        }

        if (variableManager == null)
            throw new RuntimeException("variable manager cannot be null");

        init();
    }

    private void init() {
        RxTextView
                .textChanges(layout.variableName)
                .map(String::valueOf)
                .map(this::checkName)
                .subscribe(b -> layout.btOk.setEnabled(b));

        ArrayAdapter<DataType> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                        new DataType[]{DataType.BOOL, DataType.FLOAT, DataType.INT, DataType.STRING});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        layout.spnDataType.setAdapter(adapter);

        RxAdapterView
                .itemSelections(layout.spnDataType)
                .map(i -> layout.spnDataType.getSelectedItem())
                .map(Object::toString)
                .map(DataType::valueOf)
                .subscribe(dataType -> dataTypeBuffer = dataType);

        RxCompoundButton
                .checkedChanges(layout.chkIsArray)
                .subscribe(o -> isArrayBuffer = o);

        RxView.clicks(layout.btDiscard).subscribe(v -> dismiss());

        RxView.clicks(layout.btOk).subscribe(v -> {
            if (Objects.nonNull(variable))
                changeVariable(variable);
            else
                addVariable();

            dismiss();
        });

        if (variable != null) {
            layout.variableName.setText(variable.getName());
            layout.chkIsArray.setChecked(variable.isArray());

            //noinspection unchecked
            layout.spnDataType.setSelection(((ArrayAdapter<DataType>)
                    layout.spnDataType.getAdapter()).getPosition(variable.getDataType()));
        }
    }

    private boolean checkName(String sequence) {
        String result = variableManager.checkVariableName(sequence);

        if (result == null || (variable != null && variable.getName().equals(sequence))) {
            nameBuffer = sequence;
            layout.variableNameLayout.setErrorEnabled(false);
        } else {
            layout.variableNameLayout.setError(result);
            layout.variableNameLayout.setErrorEnabled(true);
        }

        return result == null;
    }

    private void changeVariable(AndroidVariable var) {
        var.setDataType(dataTypeBuffer);
        var.setArray(isArrayBuffer);
        var.setName(nameBuffer);
    }

    private void addVariable() {
        this.variable = variableManager.createVariable(nameBuffer, dataTypeBuffer, isArrayBuffer);
    }
}
