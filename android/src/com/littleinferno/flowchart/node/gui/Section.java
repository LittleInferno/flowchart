package com.littleinferno.flowchart.node.gui;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.node.AndroidNode;
import com.littleinferno.flowchart.project.FlowchartProject;

import java.util.List;

public class Section implements Parent<String> {

    private String title;
    private List<String> nodes;

    public Section(String title, List<String> nodes) {
        this.title = title;
        this.nodes = nodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public List<String> getChildList() {
        return nodes;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }

    static class SectionViewHolder extends ParentViewHolder {

        private final ImageView arrow;
        private final TextView sectionName;

        SectionViewHolder(View itemView) {
            super(itemView);
            sectionName = (TextView) itemView.findViewById(R.id.header_name);
            arrow = (ImageView) itemView.findViewById(R.id.arrow);
        }

        public void bind(Section section) {
            sectionName.setText(section.getTitle());
        }


        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);

            arrow.setImageDrawable(ContextCompat.getDrawable(arrow.getContext(),
                    expanded ? R.drawable.ic_keyboard_arrow_down_black_24dp :
                            R.drawable.ic_keyboard_arrow_up_black_24dp));
        }
    }

    static class NodeViewHolder extends ChildViewHolder {

        private TextView nodeName;

        NodeViewHolder(View itemView) {
            super(itemView);
            nodeName = (TextView) itemView.findViewById(R.id.node_name);

            itemView.setOnLongClickListener(v -> {
                AndroidNode node = FlowchartProject.getProject()
                        .getCurrentScene()
                        .getNodeManager()
                        .createNode(nodeName.getText().toString());

                node.setVisibility(View.INVISIBLE);

                node.post(() -> {
                    node.setPoint(node.getWidth() / 2, node.getHeight() / 2);
                    node.drag();
                });
                return false;
            });
        }

        public void bind(String name) {
            nodeName.setText(name);
        }
    }
}
