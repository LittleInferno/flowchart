package com.littleinferno.flowchart.variable.gui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.annimon.stream.Optional;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutVariableDetailsBinding;
import com.littleinferno.flowchart.variable.AndroidVariable;
import com.littleinferno.flowchart.variable.AndroidVariableManager;

public class VariableDetailsFragment extends DialogFragment {

    LayoutVariableDetailsBinding layout;

    private boolean isArrayBuffer;
    private DataType dataTypeBuffer;
    private String nameBuffer;

    private Optional<AndroidVariable> variable;

    private AndroidVariableManager variableManager;
    private Optional<VariableListAdapter> variableListAdapter;

    public VariableDetailsFragment() {
        this.variable = Optional.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.DialogDetails);
        layout = LayoutVariableDetailsBinding.inflate(inflater.cloneInContext(themeWrapper), container, false);

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
            variable.ifPresentOrElse(this::changeVariable, this::addVariable);
            dismiss();
        });

        variable.ifPresent(var ->
        {
            layout.variableName.setText(var.getName());
            layout.chkIsArray.setChecked(var.isArray());

            //noinspection unchecked
            layout.spnDataType.setSelection(((ArrayAdapter<DataType>)
                    layout.spnDataType.getAdapter()).getPosition(var.getDataType()));
        });

        return layout.getRoot();
    }

    public void setVariable(AndroidVariable variable) {
        this.variable = Optional.of(variable);
    }

    public void setVariableManager(AndroidVariableManager variableManager) {
        this.variableManager = variableManager;
    }

    public AndroidVariableManager getVariableManager() {
        return variableManager;
    }

    private boolean checkName(String sequence) {
        boolean result = variableManager.checkVariableName(sequence);

        if (result) {
            nameBuffer = sequence;
            layout.variableNameLayout.setErrorEnabled(false);
        } else {
            layout.variableNameLayout.setError(variableManager.getError());
            layout.variableNameLayout.setErrorEnabled(true);
        }

        return result;
    }

    private void changeVariable(AndroidVariable var) {
        var.setDataType(dataTypeBuffer);
        var.setArray(isArrayBuffer);
        var.setName(nameBuffer);
    }

    public void setVariableListAdapter(VariableListAdapter variableListAdapter) {
        this.variableListAdapter = Optional.of(variableListAdapter);
    }

    private void addVariable() {
        AndroidVariable var = variableManager.createVariable(dataTypeBuffer, nameBuffer, isArrayBuffer);
        this.variable = Optional.of(var);

        VariableListAdapter adapter = this.variableListAdapter
                .orElseThrow(() -> new RuntimeException("adapter not set"));

        adapter.notifyItemInserted(adapter.getItemCount());
    }
}
