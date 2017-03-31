package com.littleinferno.flowchart.nodes;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.plugin.NodePluginHandle;
import com.littleinferno.flowchart.project.Project;

import java.io.File;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class NodeFragmet extends Fragment {

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ui_layout, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();


        String string = Environment.getExternalStorageDirectory().toString();

        File file = new File(string + "/flowchart_projects/lib.zip");

        List<NodePluginHandle> loadedNodePlugins = Project.pluginManager(file).getLoadedNodePlugins();

        List<ItemNode> nodes = Stream.of(loadedNodePlugins)
                .flatMap(nph -> Stream.of(nph.getNodes()))
                .map(NodePluginHandle.PluginNodeHandle::getName)
                .map(ItemNode::new)
                .toList();

        sectionAdapter.addSection(new NodeSection(sectionAdapter, "f", nodes));
        sectionAdapter.addSection(new NodeSection(sectionAdapter, "A", nodes));


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_nodes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }
}
