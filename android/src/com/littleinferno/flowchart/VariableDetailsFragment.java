package com.littleinferno.flowchart;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.variable.Variable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VariableDetailsFragment extends DialogFragment {

    @BindView(R.id.et_variable_name)
    EditText name;

    @BindView(R.id.chk_is_array)
    CheckBox isArray;

    @BindView(R.id.spn_data_type)
    Spinner dataType;

    @BindView(R.id.bt_ok)
    Button ok;

    @BindView(R.id.bt_discard)
    Button discard;


    boolean isArrayBuffer;
    DataType dataTypeBuffer;

    private Optional<Variable> variable;

    public VariableDetailsFragment() {
        this.variable = Optional.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.variable_details_layout, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<DataType> adapter = new ArrayAdapter<DataType>(getContext(), android.R.layout.simple_spinner_item, DataType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataType.setAdapter(adapter);
        dataType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataTypeBuffer = DataType.valueOf(dataType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        isArray.setOnClickListener(v -> isArrayBuffer = isArray.isChecked());

        variable.ifPresent(var ->
        {
            name.setText(var.getName());
            isArray.setChecked(var.isArray());

            Stream.range(0, dataType.getCount())
                    .forEach(i -> {
                        if (dataType.getItemAtPosition(i).toString().equals(var.getDataType().toString()))
                            dataType.setSelection(i);
                    });
        });

        discard.setOnClickListener(v -> dismiss());

        ok.setOnClickListener(v -> {
            variable.ifPresent(var -> {
                var.setDataType(dataTypeBuffer);
                var.setArray(isArrayBuffer);
                dismiss();
            });
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setVariable(Variable variable) {
        this.variable = Optional.of(variable);
    }
}
