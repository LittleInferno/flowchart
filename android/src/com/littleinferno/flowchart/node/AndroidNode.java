package com.littleinferno.flowchart.node;

import android.support.annotation.NonNull;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.littleinferno.flowchart.pin.Connector;
import com.littleinferno.flowchart.plugin.AndroidNodePluginHandle;
import com.littleinferno.flowchart.scene.AndroidSceneLayout;
import com.littleinferno.flowchart.util.Destroyable;

import io.reactivex.disposables.Disposable;

public class AndroidNode extends BaseNode implements Destroyable {

    private final AndroidNodePluginHandle.NodeHandle nodeHandle;
    private final Disposable close;

    public AndroidNode(@NonNull AndroidSceneLayout scene, @NonNull AndroidNodePluginHandle.NodeHandle nodeHandle) {
        super(scene);
        this.nodeHandle = nodeHandle;
        this.nodeHandle.getInit().call(this);
        layout.nodeTitle.setText(this.nodeHandle.getTitle());

        close = RxView.clicks(layout.close).subscribe(v -> {
            onDestroy();
            ((AndroidSceneLayout) getParent()).removeView(this);
        });
    }

    public AndroidNodePluginHandle.NodeHandle getNodeHandle() {
        return nodeHandle;
    }

    @Override
    public void onDestroy() {
        close.dispose();

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
}
