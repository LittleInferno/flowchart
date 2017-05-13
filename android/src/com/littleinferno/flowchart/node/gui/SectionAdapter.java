package com.littleinferno.flowchart.node.gui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.littleinferno.flowchart.R;

import java.util.List;

public class SectionAdapter extends ExpandableRecyclerAdapter<Section, String, Section.SectionViewHolder, Section.NodeViewHolder> {

    private LayoutInflater inflater;

    public SectionAdapter(Context context, @NonNull List<Section> sections) {
        super(sections);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Section.SectionViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView = inflater.inflate(R.layout.item_node_section_layout, parentViewGroup, false);
        return new Section.SectionViewHolder(recipeView);
    }

    @NonNull
    @Override
    public Section.NodeViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView = inflater.inflate(R.layout.item_node_layout, childViewGroup, false);
        return new Section.NodeViewHolder(ingredientView);
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
