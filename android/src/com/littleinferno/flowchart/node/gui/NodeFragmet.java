package com.littleinferno.flowchart.node.gui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.databinding.LayoutNodeUiBinding;

public class NodeFragmet extends Fragment {

    LayoutNodeUiBinding layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutNodeUiBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();


        Bundle bundle = getArguments();



    }
}
