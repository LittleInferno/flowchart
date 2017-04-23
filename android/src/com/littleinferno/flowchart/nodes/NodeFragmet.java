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

import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.R;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class NodeFragmet extends Fragment {

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ui_layout, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();


        String string = Environment.getExternalStorageDirectory().toString();

//        List<NodePluginHandle> loadedNodePlugins = Project.pluginManager(file).getLoadedNodePlugins();

//        List<ItemNode> nodes = Stream.of(loadedNodePlugins)
//                .flatMap(nph -> Stream.of(nph.getNodes()))
//                .map(NodePluginHandle.NodeHandle::getName)
//                .map(ItemNode::new)
//                .toList();
//
        sectionAdapter.addSection(new NodeSection(sectionAdapter, "Nodes", FlowchartProject.getProject().getPluginManager().getLoadedNodeHandles()));
//        sectionAdapter.addSection(new NodeSection(sectionAdapter, "A", nodes));


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_nodes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }
}
