package com.littleinferno.flowchart.node;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.littleinferno.flowchart.pin.Connector;
import com.littleinferno.flowchart.plugin.AndroidNodePluginHandle;
import com.littleinferno.flowchart.scene.AndroidSceneLayout;
import com.littleinferno.flowchart.util.Destroyable;

@SuppressLint("ViewConstructor")
public class AndroidNode extends BaseNode implements Destroyable {

    private final AndroidNodePluginHandle.NodeHandle nodeHandle;

    public AndroidNode(final Context context, @NonNull AndroidNodePluginHandle.NodeHandle nodeHandle) {
        super(context);
        this.nodeHandle = nodeHandle;
        this.nodeHandle.getInit().call(this);
        this.nodeHandle.getAttribute("functionInit").ifPresent(o -> {

        });

        setOnClickListener(v -> {
            onDestroy();
            ((AndroidSceneLayout) getParent()).removeView(this);
        });
    }

    public AndroidNodePluginHandle.NodeHandle getNodeHandle() {
        return nodeHandle;
    }

    @Override
    public void onDestroy() {
        int childCount = layout.nodeLeft.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = layout.nodeLeft.getChildAt(i);

            if (child instanceof Connector) {
                Connector c = (Connector) child;
                c.disconnectAll();
                c.onDestroy();
            }
        }

        childCount = layout.nodeRight.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = layout.nodeRight.getChildAt(i);

            if (child instanceof Connector) {
                Connector c = (Connector) child;
                c.disconnectAll();
                c.onDestroy();
            }
        }
    }

    @Override
    public void addView(Align align, View view) {
        super.addView(align, view);
        //do not remove this
    }
}
