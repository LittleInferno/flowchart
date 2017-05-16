package com.littleinferno.flowchart.node.gui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.project.FlowchartProject;

import java.util.List;

class SectionAdapter extends ExpandableRecyclerAdapter<Section, String, Section.SectionViewHolder, Section.NodeViewHolder> {

    private LayoutInflater inflater;
    private FlowchartProject project;

    SectionAdapter(FlowchartProject project, Context context, @NonNull List<Section> sections) {
        super(sections);
        this.project = project;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Section.SectionViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View section = inflater.inflate(R.layout.item_node_section_layout, parentViewGroup, false);
        return new Section.SectionViewHolder(section);
    }

    @NonNull
    @Override
    public Section.NodeViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView = inflater.inflate(R.layout.item_node_layout, childViewGroup, false);
        return new Section.NodeViewHolder(project.getCurrentScene().getNodeManager(), ingredientView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull Section.SectionViewHolder parentViewHolder, int parentPosition, @NonNull Section parent) {
        parentViewHolder.bind(parent);

    }

    @Override
    public void onBindChildViewHolder(@NonNull Section.NodeViewHolder ingredientViewHolder, int parentPosition, int childPosition, @NonNull String node) {
        ingredientViewHolder.bind(node);
    }
}
