package com.littleinferno.flowchart.menu;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.littleinferno.flowchart.databinding.LayoutProjectDetailsBinding;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.util.Files;

import java.io.File;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectDetailsDialog extends DialogFragment {

    LayoutProjectDetailsBinding layout;
    private Disposable name;
    private Set<String> projectNames;
    private String projectName;
    private String newProjectName;
    private ProjectUpdate delListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutProjectDetailsBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getDialog().getWindow();
        lp.copyFrom(window.getAttributes());

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    @Override
    public void onStart() {
        super.onStart();

        projectName = getArguments().getString(Project.PROJECT_NAME);

        projectNames = Stream.of(new File(Files.getSavesLocation()).list())
                .map(Files::saveNameToProjectName).collect(Collectors.toSet());

        name = RxTextView.textChanges(layout.projectName)
                .skipInitialValue()
                .map(String::valueOf)
                .map(this::checkName)
                .subscribe(b ->
                        layout.save.setEnabled(b));

        layout.projectName.setText(projectName);
        layout.save.setOnClickListener(v -> {

            Files.copyFile(getContext(), new File(Files.getSaveLocation(projectName)),
                    new File(Files.getSaveLocation(newProjectName)));
            Files.delete(getContext(), Files.getSaveLocation(projectName));
            delListener.update();
            dismiss();
        });

        layout.back.setOnClickListener(v -> dismiss());
        layout.delete.setOnClickListener(v -> {
            Files.delete(getContext(), Files.getSaveLocation(projectName));
            delListener.update();
            dismiss();
        });

        loadProject();
    }

    private void loadProject() {
        layout.plugin.root.setVisibility(View.INVISIBLE);

        Observable.just(projectName)
                .map(Files::loadProjectSave)
                .map(Project.SimpleObject::getPlugin)
                .map(p -> Files.loadPluginParams(getContext(), p))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pluginParams -> {
                    layout.plugin.name.setText(pluginParams.getPluginName());
                    layout.plugin.version.setText(pluginParams.getPluginVersion());
                    layout.plugin.description.setText(pluginParams.getPluginDescription());

                    layout.progress.setVisibility(View.INVISIBLE);
                    layout.progress.setEnabled(false);
                    layout.plugin.root.setVisibility(View.VISIBLE);
                });
    }

    private boolean checkName(String s) {
        boolean b = true;

        if (projectNames.contains(s) && !s.equals(projectName)) {
            b = false;
            setError("This name is already taken");
        } else if (s.isEmpty()) {
            b = false;
            setError("Name cannot be empty");
        } else if (s.matches("^\\s*$")) {
            b = false;
            setError("Unacceptable symbols");
        }

        if (b) {
            newProjectName = s;
            layout.projectNameLayout.setErrorEnabled(false);
        }
        return b;
    }

    private void setError(String error) {
        layout.projectNameLayout.setErrorEnabled(true);
        layout.projectNameLayout.setError(error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        name.dispose();
    }

    public void setDelListener(ProjectUpdate delListener) {
        this.delListener = delListener;
    }
}
