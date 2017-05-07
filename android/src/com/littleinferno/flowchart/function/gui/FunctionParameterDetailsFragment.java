package com.littleinferno.flowchart.function.gui;

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
import android.widget.ArrayAdapter;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutFunctionParameterDetailsBinding;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionParameter;
import com.littleinferno.flowchart.util.Objects;

import io.reactivex.disposables.Disposable;

public class FunctionParameterDetailsFragment extends DialogFragment {

    LayoutFunctionParameterDetailsBinding layout;

    private boolean isArrayBuffer;
    private DataType dataTypeBuffer;
    private String nameBuffer;

    private AndroidFunction function;

    private AndroidFunctionParameter parameter;
    private Connection connectionBuffer;
    private Disposable name;
    private Disposable type;
    private Disposable connection;
    private Disposable discard;
    private Disposable ok;
    private Disposable isArray;

    public FunctionParameterDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.DialogDetails);
        layout = LayoutFunctionParameterDetailsBinding.inflate(inflater.cloneInContext(themeWrapper), container, false);

        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null) {
            function = bundle.getParcelable(AndroidFunction.TAG);
            parameter = bundle.getParcelable(AndroidFunctionParameter.TAG);
        }

        if (function == null)
            throw new RuntimeException("function cannot be null");

        init();
    }

    @Override
    public void onStop() {
        super.onStop();

        name.dispose();
        type.dispose();
        isArray.dispose();
        discard.dispose();
        connection.dispose();
        ok.dispose();
    }


    private void init() {
        name = RxTextView
                .textChanges(layout.variableName)
                .map(String::valueOf)
                .map(this::checkName)
                .subscribe(b -> layout.create.setEnabled(b));

        ArrayAdapter<DataType> dataTypeAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                        new DataType[]{DataType.BOOL, DataType.FLOAT, DataType.INT, DataType.STRING});
        dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        layout.dataType.setAdapter(dataTypeAdapter);

        ArrayAdapter<Connection> connectionAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Connection.values());
        connectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        layout.connection.setAdapter(connectionAdapter);

        type = RxAdapterView
                .itemSelections(layout.dataType)
                .map(i -> layout.dataType.getSelectedItem())
                .map(Object::toString)
                .map(DataType::valueOf)
                .subscribe(dataType -> dataTypeBuffer = dataType);

        connection = RxAdapterView
                .itemSelections(layout.connection)
                .map(i -> layout.connection.getSelectedItem())
                .map(Object::toString)
                .map(Connection::valueOf)
                .subscribe(connection -> connectionBuffer = connection);

        isArray = RxCompoundButton
                .checkedChanges(layout.isArray)
                .subscribe(o -> isArrayBuffer = o);

        discard = RxView.clicks(layout.discard).subscribe(v -> dismiss());

        ok = RxView.clicks(layout.create).subscribe(v -> {
            if (Objects.nonNull(parameter))
                changeParameter(parameter);
            else
                addParameter();

            dismiss();
        });

        if (parameter != null) {
            layout.variableName.setText(parameter.getName());
            layout.isArray.setChecked(parameter.isArray());

            //noinspection unchecked
            layout.dataType.setSelection(((ArrayAdapter<DataType>)
                    layout.dataType.getAdapter()).getPosition(parameter.getDataType()));

            //noinspection unchecked
            layout.dataType.setSelection(((ArrayAdapter<Connection>)
                    layout.dataType.getAdapter()).getPosition(parameter.getConnection()));
        }
    }

    private boolean checkName(final String sequence) {
        String result = function.checkParameterName(sequence);

        if (result == null || (parameter != null && parameter.getName().equals(sequence))) {
            nameBuffer = sequence;
            layout.variableNameLayout.setErrorEnabled(false);
            return true;
        } else {
            layout.variableNameLayout.setError(result);
            layout.variableNameLayout.setErrorEnabled(true);
            return false;
        }
    }

    private void changeParameter(AndroidFunctionParameter var) {
        var.setDataType(dataTypeBuffer);
        var.setArray(isArrayBuffer);
        var.setName(nameBuffer);
        var.setConnection(connectionBuffer);
        function.updateData();
    }

    private void addParameter() {
        this.parameter = function.createParameter(connectionBuffer, nameBuffer, dataTypeBuffer, isArrayBuffer);
    }

    public static void show(@NonNull AndroidFunction function,
                            @NonNull FragmentManager fragmentManager,
                            @Nullable AndroidFunctionParameter data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidFunction.TAG, function);
        bundle.putParcelable(AndroidFunctionParameter.TAG, data);

        FunctionParameterDetailsFragment parameterDetails = new FunctionParameterDetailsFragment();
        parameterDetails.setArguments(bundle);
        parameterDetails.show(fragmentManager, "PARAMETER_DETAILS");
    }
}
