package com.littleinferno.flowchart.node.gui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;
import com.littleinferno.flowchart.project.FlowchartProject;

import java.util.List;

public class NodeFragmet extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ui_layout, container, false);

        NodePicker recycler = (NodePicker) view.findViewById(R.id.rv_nodes);

        List<AndroidPluginHandle.NodeHandle> handles = FlowchartProject.getProject()
                .getPluginManager()
                .getPlugin()
                .getNodes();

        recycler.init(Stream.of(handles)
                .map(AndroidPluginHandle.NodeHandle::getCategory)
                .distinct()
                .filter(v -> !v.equals("system"))
                .map(s -> new Section(s, Stream.of(handles)
                        .filter(h -> h.getCategory().equals(s))
                        .map(AndroidPluginHandle.NodeHandle::getName)
                        .toList())).toList());

        return view;
    }
}
