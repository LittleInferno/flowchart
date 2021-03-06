package com.littleinferno.flowchart.variable.gui;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutVariableDetailsBinding;
import com.littleinferno.flowchart.util.DataType;
import com.littleinferno.flowchart.variable.Variable;
import com.littleinferno.flowchart.variable.VariableManager;

import io.reactivex.disposables.Disposable;

public class VariableDetailsFragment extends DialogFragment {

    LayoutVariableDetailsBinding layout;

    private boolean isArrayBuffer;
    private DataType dataTypeBuffer;
    private String nameBuffer;

    private Variable variable;

    private VariableManager variableManager;
    private Disposable name;
    private Disposable type;
    private Disposable isArray;
    private Disposable discard;
    private Disposable ok;

    public VariableDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.DialogDetails);
        layout = LayoutVariableDetailsBinding.inflate(inflater.cloneInContext(themeWrapper), container, false);

        return layout.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmlp = window.getAttributes();
            wmlp.windowAnimations = R.style.FragmentAnim;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null) {
            variableManager = bundle.getParcelable(VariableManager.TAG);
            variable = bundle.getParcelable(Variable.TAG);
        }

        if (variableManager == null)
            throw new RuntimeException("variable manager cannot be null");

        init();
    }

    @Override
    public void onStop() {
        super.onStop();

        name.dispose();
        type.dispose();
        isArray.dispose();
        ok.dispose();
        discard.dispose();
    }

    private void init() {
        name = RxTextView
                .textChanges(layout.variableName)
                .map(String::valueOf)
                .map(this::checkName)
                .subscribe(b -> layout.btOk.setEnabled(b));

        ArrayAdapter<DataType> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                        new DataType[]{DataType.BOOL, DataType.FLOAT, DataType.INT, DataType.STRING});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        layout.spnDataType.setAdapter(adapter);

        type = RxAdapterView
                .itemSelections(layout.spnDataType)
                .map(i -> layout.spnDataType.getSelectedItem())
                .map(Object::toString)
                .map(DataType::valueOf)
                .subscribe(dataType -> dataTypeBuffer = dataType);

        isArray = RxCompoundButton
                .checkedChanges(layout.chkIsArray)
                .subscribe(o -> isArrayBuffer = o);

        discard = RxView.clicks(layout.btDiscard).subscribe(v -> dismiss());

        ok = RxView.clicks(layout.btOk).subscribe(v -> {
            if (variable != null)
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
            return true;
        } else {
            layout.variableNameLayout.setError(result);
            layout.variableNameLayout.setErrorEnabled(true);
            return false;
        }
    }

    private void changeVariable(Variable var) {
        var.setDataType(dataTypeBuffer);
        var.setArray(isArrayBuffer);
        var.setName(nameBuffer);
        variableManager.updateData();
    }

    private void addVariable() {
        this.variable = variableManager.createVariable(nameBuffer, dataTypeBuffer, isArrayBuffer);
    }

    public static void show(@NonNull final VariableManager variableManager,
                            @NonNull final FragmentManager fragmentManager,
                            @Nullable final Variable variable) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(VariableManager.TAG, variableManager);
        bundle.putParcelable(Variable.TAG, variable);

        VariableDetailsFragment variableDetailsFragment = new VariableDetailsFragment();
        variableDetailsFragment.setArguments(bundle);
        variableDetailsFragment.show(fragmentManager, "VARIABLE_DETAILS");
    }
}
