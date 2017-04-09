package com.littleinferno.flowchart;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.databinding.ProjectLoadLayoutBinding;
import com.littleinferno.flowchart.plugin.BasePluginHandle;
import com.littleinferno.flowchart.plugin.NodePluginHandle;

import java.io.File;
import java.util.List;

public class ProjectLoadDialog extends DialogFragment {

    ProjectLoadLayoutBinding layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = DataBindingUtil.inflate(inflater, R.layout.project_load_layout, container, false);


        String string = Environment.getExternalStorageDirectory().toString();

        File file = new File(string + "/flowchart_projects/lib.zip");

        File[] files = file.listFiles();


        List<NodePluginHandle> pluginHandles = Stream.of(files).map(NodePluginHandle::new).toList();

        int filesCount = Stream.of(pluginHandles).mapToInt(BasePluginHandle::getFileCount).sum();
        layout.progress.setMax(filesCount);


        return layout.getRoot();
    }

}
