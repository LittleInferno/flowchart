package com.littleinferno.flowchart.variable.gui;

import android.app.AlertDialog;
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
import com.littleinferno.flowchart.databinding.LayoutVariableListBinding;
import com.littleinferno.flowchart.util.Link;
import com.littleinferno.flowchart.util.ResUtil;
import com.littleinferno.flowchart.util.Swiper;
import com.littleinferno.flowchart.variable.Variable;
import com.littleinferno.flowchart.variable.VariableManager;

import net.idik.lib.slimadapter.SlimAdapter;

public class VariableListFragment extends DialogFragment {

    private LayoutVariableListBinding layout;
    private VariableManager variableManager;
    private SlimAdapter adapter;
    private Link add;
    private Link remove;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutVariableListBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.FILL_HORIZONTAL;
            params.windowAnimations = R.style.FragmentAnim;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null) {
            variableManager = (VariableManager) bundle.get(VariableManager.TAG);
        }

        if (variableManager == null)
            throw new RuntimeException("variable manager cannot be null");

        layout.addVariable.setOnClickListener(v ->
                VariableDetailsFragment.show(variableManager, getFragmentManager(), null));

        layout.variables.items.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = SlimAdapter.create()
                .registerDefault(R.layout.item_variable, (o, injector) -> {
                    Variable data = (Variable) o;
                    injector.text(R.id.variable_name, data.getName())
                            .text(R.id.variable_type, data.getDataType().toString().toLowerCase())
                            .textColor(R.id.variable_type, ResUtil.getDataTypeColor(getContext(), data.getDataType()))
                            .image(R.id.variable_is_array, ResUtil.getArrayDrawable(getContext(), data.isArray()))
                            .clicked(R.id.item_variable_root,
                                    v -> VariableDetailsFragment.show(
                                            variableManager, getFragmentManager(), data));
                })
                .attachTo(layout.variables.items)
                .updateData(variableManager.getVariables());

        Swiper.create(getContext())
                .swipeLeft(position -> {
                    if (variableManager.getVariable(position).isUse())
                        new AlertDialog.Builder(getContext())
                                .setMessage("variable using continue?")
                                .setPositiveButton("ok", (dialogInterface, i) -> removeVariable(position))
                                .setNegativeButton("cancel", (dialogInterface, i) -> adapter.notifyDataSetChanged())
                                .show();
                    else
                        removeVariable(position);
                })
                .icon(R.drawable.ic_delete)
                .iconColor(Color.WHITE)
                .backgroundColor(Color.RED)
                .attachTo(layout.variables.items);

        add = variableManager.onVariableAdd(adapter::notifyDataSetChanged);
        remove = variableManager.onVariableRemove(adapter::notifyDataSetChanged);
    }

    private void removeVariable(int position) {
        variableManager.removeVariable(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, variableManager.getVariables().size());
    }

    @Override
    public void onStop() {
        super.onStop();
        add.disconnect();
        remove.disconnect();
    }

    public static void show(@NonNull final VariableManager variableManager,
                            @NonNull final FragmentManager fragmentManager) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(VariableManager.TAG, variableManager);

        VariableListFragment variableFragment = new VariableListFragment();
        variableFragment.setArguments(bundle);
        variableFragment.show(fragmentManager, "VARIABLE_LIST");
    }


}
