package com.littleinferno.flowchart.node.gui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.littleinferno.flowchart.project.FlowchartProject;

import java.util.List;

public class NodePicker extends RecyclerView {

    public NodePicker(Context context) {
        super(context);
    }

    public NodePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NodePicker(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(FlowchartProject flowchartProject, List<Section> sections) {
        SectionAdapter adapter = new SectionAdapter(flowchartProject, getContext(), sections);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(adapter);
    }
}
