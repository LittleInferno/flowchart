package com.littleinferno.flowchart.variable.gui;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutVariableListBinding;
import com.littleinferno.flowchart.util.ResUtil;
import com.littleinferno.flowchart.util.Swiper;
import com.littleinferno.flowchart.util.UpdaterHandle;
import com.littleinferno.flowchart.variable.AndroidVariable;
import com.littleinferno.flowchart.variable.AndroidVariableManager;

import net.idik.lib.slimadapter.SlimAdapter;

public class VariableListFragment extends DialogFragment {

    private LayoutVariableListBinding layout;
    private AndroidVariableManager variableManager;
    private SlimAdapter adapter;

    private final UpdaterHandle updaterHandle = new UpdaterHandle(
            () -> adapter.updateData(variableManager.getVariables()));


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutVariableListBinding.inflate(inflater, container, false);
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
            variableManager = (AndroidVariableManager) bundle.get(AndroidVariableManager.TAG);
        }

        if (variableManager == null)
            throw new RuntimeException("variable manager cannot be null");

        layout.addVariable
                .setOnClickListener(v -> VariableDetailsFragment.show(
                        variableManager, getFragmentManager(), updaterHandle, null));

        layout.variables.items.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = SlimAdapter.create().registerDefault(R.layout.item_variable, (o, injector) -> {
            AndroidVariable data = (AndroidVariable) o;
            injector.text(R.id.variable_name, data.getName())
                    .text(R.id.variable_type, data.getDataType().toString().toLowerCase())
                    .textColor(R.id.variable_type, ResUtil.getDataTypeColor(getContext(), data.getDataType()))
                    .image(R.id.variable_is_array, ResUtil.getArrayDrawable(getContext(), data.isArray()))
                    .clicked(R.id.item_variable_root,
                            v -> VariableDetailsFragment.show(
                                    variableManager, getFragmentManager(), updaterHandle, data));
        })
                .attachTo(layout.variables.items)
                .updateData(variableManager.getVariables());

        Swiper.create(getContext(), ItemTouchHelper.LEFT).register((direction, position) -> {
            if (direction == ItemTouchHelper.LEFT) {
                variableManager.getVariables().remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, variableManager.getVariables().size());
            }
        })
                .attachTo(layout.variables.items);
    }

    public static void show(@NonNull final AndroidVariableManager variableManager,
                            @NonNull final FragmentManager fragmentManager) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidVariableManager.TAG, variableManager);

        VariableListFragment variableFragment = new VariableListFragment();
        variableFragment.setArguments(bundle);
        variableFragment.show(fragmentManager, "VARIABLE_LIST");
    }


}
