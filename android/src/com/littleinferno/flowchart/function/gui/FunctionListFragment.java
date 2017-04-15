package com.littleinferno.flowchart.function.gui;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.LayoutFunctionListBinding;

public class FunctionListFragment extends DialogFragment {

    LayoutFunctionListBinding layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final Context themeWrapper = new ContextThemeWrapper(getActivity(), R.style.DialogList);
        layout = LayoutFunctionListBinding.inflate(inflater.cloneInContext(themeWrapper), container, false);





        return layout.getRoot();
    }
}
