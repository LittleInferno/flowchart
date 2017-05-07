package com.littleinferno.flowchart.function.gui;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutFunctionListBinding;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionManager;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.scene.gui.SceneFragment;
import com.littleinferno.flowchart.util.Link;
import com.littleinferno.flowchart.util.Swiper;

import net.idik.lib.slimadapter.SlimAdapter;

public class FunctionListFragment extends DialogFragment {

    LayoutFunctionListBinding layout;
    private AndroidFunctionManager functionManager;
    private Link add;
    private Link remove;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutFunctionListBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.FILL_HORIZONTAL;
            layoutParams.windowAnimations = R.style.FragmentAnim;
        }
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

        layout.addFunction.setOnClickListener(v -> NewFunctionDialog.show(functionManager, getFragmentManager()));

        layout.functions.items.setLayoutManager(new LinearLayoutManager(getContext()));

        SlimAdapter adapter = SlimAdapter.create()
                .registerDefault(R.layout.item_function_card, (o, injector) -> {
                    AndroidFunction data = (AndroidFunction) o;
                    injector.text(R.id.function_name_card, data.getName())
                            .clicked(R.id.card, v ->
                                    FunctionDetailsFragment.show(functionManager, getChildFragmentManager(),
                                            data, R.id.function_details_layout))
                            .longClicked(R.id.card, v -> {
                                        SceneFragment.show(data, FlowchartProject.getProject()
                                                .getFragmentManager(), R.id.scene_frame);

                                        dismiss();
                                        return true;
                                    }
                            );

                })
                .attachTo(layout.functions.items)
                .updateData(functionManager.getFunctions());

        Swiper.create(getContext())
                .swipeLeft(position -> {
                    functionManager.getFunctions().remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, functionManager.getFunctions().size());

                    if (position > functionManager.getFunctions().size())
                        position--;

                    FunctionDetailsFragment.show(functionManager, getChildFragmentManager(),
                            functionManager.getFunctions().get(position), R.id.function_details_layout);

                    layout.addFunction.show();
                })
                .icon(R.drawable.ic_delete)
                .iconColor(Color.WHITE)
                .backgroundColor(Color.RED)
                .swipeRule(vh -> functionManager
                        .getFunctions()
                        .get(vh.getLayoutPosition())
                        .getName()
                        .equals("MAIN"))
                .attachTo(layout.functions.items);

        FunctionDetailsFragment.show(functionManager, getChildFragmentManager(),
                functionManager.getProject().getCurrentScene(), R.id.function_details_layout);

        add = functionManager.onFunctionAdd(adapter::notifyDataSetChanged);
        remove = functionManager.onFunctionRemove(adapter::notifyDataSetChanged);
    }

    @Override
    public void onStop() {
        super.onStop();
        add.disconnect();
        remove.disconnect();
    }

    public static void show(@NonNull AndroidFunctionManager functionManager,
                            @NonNull FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidFunctionManager.TAG, functionManager);

        FunctionListFragment functionList = new FunctionListFragment();
        functionList.setArguments(bundle);
        functionList.show(fragmentManager, "FUNCTION_LIST");
    }
}
