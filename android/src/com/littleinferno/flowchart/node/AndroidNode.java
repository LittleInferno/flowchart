package com.littleinferno.flowchart.node;

import android.support.annotation.NonNull;
import android.view.View;

import com.littleinferno.flowchart.scene.AndroidSceneLayout;
import com.littleinferno.flowchart.plugin.AndroidNodePluginHandle;

public class AndroidNode extends BaseNode {

    public enum Align {
        LEFT, RIGHT, CENTER
    }

    private final AndroidNodePluginHandle.NodeHandle nodeHandle;

    public AndroidNode(@NonNull AndroidSceneLayout scene, @NonNull AndroidNodePluginHandle.NodeHandle nodeHandle) {
        super(scene);
        this.nodeHandle = nodeHandle;
        this.nodeHandle.getInit().call(this);

        layout.nodeTitle.setText(this.nodeHandle.getTitle());

        layout.close.setOnClickListener(v -> ((AndroidSceneLayout) getParent()).removeView(this));
    }

    public AndroidNodePluginHandle.NodeHandle getNodeHandle() {
        return nodeHandle;
    }

    public void addView(View view, Align align) {
        view.setLayoutParams(createLayoutParams());
        switch (align) {
            case LEFT:
                layout.nodeLeft.addView(view);
                break;
            case RIGHT:
                layout.nodeRight.addView(view);
                break;
            case CENTER:
                layout.container.addView(view);
                break;
        }
    }
}
