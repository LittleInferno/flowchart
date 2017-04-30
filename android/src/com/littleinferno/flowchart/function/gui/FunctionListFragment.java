package com.littleinferno.flowchart.function.gui;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutFunctionListBinding;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionManager;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.scene.gui.SceneFragment;

import net.idik.lib.slimadapter.SlimAdapter;

public class FunctionListFragment extends DialogFragment {

    LayoutFunctionListBinding layout;
    private AndroidFunctionManager functionManager;
    private FunctionListAdapter functionListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutFunctionListBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL;
        wmlp.windowAnimations = R.style.FragmentAnim;
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

        layout.functions.items.setLayoutManager(new LinearLayoutManager(getContext()));
//        FunctionListAdapter adapter = new FunctionListAdapter(functionManager, getChildFragmentManager());
//        layout.functions.items.setAdapter(adapter);

        final SlimAdapter slimAdapter = SlimAdapter.create()
                .registerDefault(R.layout.item_function_card, (o, injector) -> {
                    AndroidFunction data = (AndroidFunction) o;
                    injector.text(R.id.function_name_card, data.getName())
                            .clicked(R.id.card, v ->
                                    FunctionDetailsFragment.show(functionManager, getChildFragmentManager(),
                                            data, R.id.function_details_layout))
                            .longClicked(R.id.card, v -> {
                                        Bundle b = new Bundle();
                                        b.putParcelable(AndroidFunction.TAG, data);

                                        SceneFragment scene = new SceneFragment();
                                        scene.setArguments(b);
                                        FlowchartProject.getProject()
                                                .getFragmentManager()
                                                .beginTransaction().replace(R.id.scene_frame, scene).commit();

                                        dismiss();
                                        return true;
                                    }
                            );

                })
                .attachTo(layout.functions.items)
                .updateData(functionManager.getFunctions());

        FunctionDetailsFragment.show(functionManager, getChildFragmentManager(),
                functionManager.getFunctions().get(0), R.id.function_details_layout);
    }

    private void createNewFunction(View view) {
        NewFunctionDialog functionDialog = new NewFunctionDialog();

        functionDialog.setArguments(getArguments());
        functionDialog.show(getFragmentManager(), "CREATE");
    }

    public static void show(@NonNull AndroidFunctionManager functionManager, @NonNull FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidFunctionManager.TAG, functionManager);

        FunctionListFragment functionList = new FunctionListFragment();
        functionList.setArguments(bundle);
        functionList.show(fragmentManager, "FUNCTION_LIST");
    }
}
