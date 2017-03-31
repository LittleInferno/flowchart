package com.littleinferno.flowchart.function;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.annimon.stream.Optional;
import com.littleinferno.flowchart.R;

import butterknife.BindView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class FunctionDetailsFragment extends DialogFragment {

    @BindView(R.id.et_function_name)
    EditText functionName;

//    @BindView(R.id.fab_add_parameter)
//    FloatingActionButton add_parameter;

//    @BindView(R.id.rv_parameters)
//    RecyclerView parameters;

    private Optional<Function> function;
    private SectionedRecyclerViewAdapter sectionAdapter;

    public FunctionDetailsFragment() {
        function = Optional.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_details_layout, container, false);
//        ButterKnife.bind(this, view);


//        add_parameter.setOnClickListener(v -> {
//            function.ifPresent(fun ->
//            {
//                fun.addParameter("TestName", DataType.BOOL, Connection.INPUT, false);
//                sectionAdapter.notifyItemInserted(fun.getInputParameters().size() - 1);
//            });
//        });
//
//
//        sectionAdapter = new SectionedRecyclerViewAdapter();
//
//        sectionAdapter.addSection(new ParameterSection(function.get(), true));
//        sectionAdapter.addSection(new ParameterSection(function.get(), false));
//
//        parameters.setLayoutManager(new LinearLayoutManager(getContext()));
//        parameters.setAdapter(sectionAdapter);


        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    public void setFunction(Function function) {
        this.function = Optional.of(function);
    }
}
