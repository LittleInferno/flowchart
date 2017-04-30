package com.littleinferno.flowchart.function.gui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutFunctionDetailsBinding;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionManager;
import com.littleinferno.flowchart.function.AndroidFunctionParameter;
import com.littleinferno.flowchart.util.ResUtil;
import com.littleinferno.flowchart.util.UpdaterHandle;

import net.idik.lib.slimadapter.SlimAdapter;

public class FunctionDetailsFragment extends Fragment {

    private LayoutFunctionDetailsBinding layout;
    private AndroidFunctionManager functionManager;
    private AndroidFunction function;

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
            functionManager = bundle.getParcelable(AndroidFunctionManager.TAG);
            function = bundle.getParcelable(AndroidFunction.TAG);
        }

        if (functionManager == null)
            throw new RuntimeException("function manager cannot be null");

        if (function == null) {
            throw new RuntimeException("function cannot be null");
        }

        layout.functionName.setText(function.getName());

        final SlimAdapter slimAdapter = SlimAdapter.create();
        final UpdaterHandle updaterHandle = new UpdaterHandle(
                () -> slimAdapter.updateData(function.getParameters()));

        layout.addParameter.setOnClickListener(v -> FunctionParameterDetailsFragment.show(
                function, getFragmentManager(), updaterHandle, null));

        layout.parameters.items.setLayoutManager(new LinearLayoutManager(getContext()));

        slimAdapter.registerDefault(R.layout.item_parameter, (o, injector) -> {
            AndroidFunctionParameter data = (AndroidFunctionParameter) o;
            injector.text(R.id.parameter_name, data.getName())
                    .text(R.id.parameter_type, data.getDataType().toString().toLowerCase())
                    .textColor(R.id.parameter_type, ResUtil.getDataTypeColor(getContext(), data.getDataType()))
                    .image(R.id.parameter_is_array, ResUtil.getArrayDrawable(getContext(), data.isArray()))
                    .clicked(R.id.item_parameter_root,
                            v -> FunctionParameterDetailsFragment.show(
                                    function, getFragmentManager(), updaterHandle, data));
        })
                .attachTo(layout.parameters.items)
                .updateData(function.getParameters());
    }

    public static void show(@NonNull AndroidFunctionManager functionManager, @NonNull FragmentManager fragmentManager,
                            @NonNull AndroidFunction data, @IdRes int layout) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidFunctionManager.TAG, functionManager);
        bundle.putParcelable(AndroidFunction.TAG, data);

        FunctionDetailsFragment functionDetails = new FunctionDetailsFragment();
        functionDetails.setArguments(bundle);
        fragmentManager.beginTransaction().replace(layout, functionDetails).commit();
    }

    private void createNewParameter(View view) {
        FunctionParameterDetailsFragment parameterDetails = new FunctionParameterDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidFunction.TAG, function);
        parameterDetails.setArguments(bundle);
        parameterDetails.show(getFragmentManager(), "create");
    }
}
