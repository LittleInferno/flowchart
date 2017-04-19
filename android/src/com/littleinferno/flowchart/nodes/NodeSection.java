package com.littleinferno.flowchart.nodes;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.littleinferno.flowchart.FlowchartProject;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.scene.AndroidScene;
import com.littleinferno.flowchart.node.AndroidNode;
import com.littleinferno.flowchart.plugin.AndroidNodePluginHandle;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class NodeSection extends StatelessSection {

    private SectionedRecyclerViewAdapter adapter;
    private String name;
    private List<AndroidNodePluginHandle.NodeHandle> nodes;
    private boolean expanded;

    public NodeSection(SectionedRecyclerViewAdapter adapter, String name, List<AndroidNodePluginHandle.NodeHandle> nodes) {
        super(R.layout.section_node_header, R.layout.item_node_layout);
        this.adapter = adapter;
        this.name = name;
        this.nodes = nodes;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? nodes.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        itemHolder.nodeName.setText(nodes.get(position).getName());
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.title.setText(name);

        headerHolder.view.setOnClickListener(v -> {
            expanded = !expanded;
            headerHolder.image.setImageResource(
                    expanded ? R.drawable.ic_keyboard_arrow_up_black_24dp : R.drawable.ic_keyboard_arrow_down_black_24dp
            );
            adapter.notifyDataSetChanged();
        });
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final View view;
        private final ImageView image;

        public HeaderViewHolder(View view) {
            super(view);

            this.view = view;
            title = (TextView) view.findViewById(R.id.tv_section_name);
            image = (ImageView) view.findViewById(R.id.iv_arrow);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView nodeName;
        private final View root;

        public ItemViewHolder(View view) {
            super(view);

            root = view;
            nodeName = (TextView) view.findViewById(R.id.node_name);
            root.setOnTouchListener((v, e) -> {

                if (e.getAction() == MotionEvent.ACTION_DOWN) {

                    AndroidScene currentScene = FlowchartProject.getProject().getCurrentScene();

                    Optional<AndroidNode> node = currentScene.getNodeManager().createNode(nodeName.getText().toString());

                    node.ifPresent(androidNode -> {

                        Integer i = androidNode
                                .getNodeHandle()
                                .getAttribute("limit")
                                .map(Integer.class::cast)
                                .orElse(0);

                        if (i > 0)
                            root.setEnabled(false);

                        androidNode.setVisibility(View.INVISIBLE);
                        currentScene.addView(androidNode);

                        androidNode.post(() -> {
                            androidNode.setPoint(androidNode.getWidth() / 2, androidNode.getHeight() / 2);
                            androidNode.drag();
                        });
                    });
                }
                return true;
            });
        }
    }
}
