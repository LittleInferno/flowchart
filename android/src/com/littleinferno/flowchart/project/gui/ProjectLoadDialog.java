package com.littleinferno.flowchart.project.gui;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.littleinferno.flowchart.util.Files;
import com.littleinferno.flowchart.ProjectActivity;
import com.littleinferno.flowchart.databinding.LayoutProjectLoadBinding;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;
import com.littleinferno.flowchart.project.Project;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class ProjectLoadDialog extends DialogFragment {

    private LayoutProjectLoadBinding layout;
    private Disposable plugin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutProjectLoadBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        String projectName = "test";
        String pluginPath = Environment.getExternalStorageDirectory().toString() + "/flowchart_projects/plugins/new.js";

        String saveName = Files.projectNameToSaveName(projectName);

        layout.close.setOnClickListener(v -> dismiss());

        Project project = Project.create(projectName);

        Observable<Project> projectObservable = Observable.just(pluginPath)
//                .subscribeOn(Schedulers.io())
                .map(File::new)
                .map(Files::readToString)
//                .observeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> new AndroidPluginHandle(null, s))
                .map(AndroidPluginHandle.class::cast)
                .map(ph -> {
                    project.setPlugin(ph);
                    return project;
                })
                .map(p -> {
                    Observable.zip(
                            Observable.just(Files.getSavesLocation() + saveName)
                                    .map(File::new)
                                    .map(Files::readToString),
                            Observable.just(new Gson()), this::readSave)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
                            .flatMap(Observable::fromIterable)
                            .forEach(p.getFunctionManager()::createFunction);
                    return p;
                });

        Observable<List<Function.SimpleObject>> nodesObservable = Observable.zip(
                Observable.just(Files.getSavesLocation() + saveName)
                        .map(File::new)
                        .map(Files::readToString),
                Observable.just(new Gson()), this::readSave);
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());


//        pluginFile = Observable.zip(projectObservable, nodesObservable,
//                (p, objects) ->
//                {
//                    Observable.fromIterable(objects)
//                            .subscribeOn(AndroidSchedulers.mainThread())
//                            .forEach(p.getFunctionManager()::createFunction);
//                    return p;
//                })
        projectObservable.subscribe(p -> getContext().startActivity(new Intent(getContext(), ProjectActivity.class)),
                throwable -> {
                    throwable.printStackTrace();
                    layout.information.setText("ERROR load pluginFile:" + throwable.getMessage());
                    layout.close.setText("close");
                });
    }

    List<Function.SimpleObject> readSave(String save, Gson gson) {
        return gson.fromJson(save, Project.getSavingType());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        plugin.dispose();
    }
}
