package com.littleinferno.flowchart.project.gui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.annimon.stream.Stream;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.littleinferno.flowchart.Files;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.NewProjectLayoutBinding;
import com.littleinferno.flowchart.plugin.AndroidBasePluginHandle;
import com.littleinferno.flowchart.plugin.PluginHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipFile;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CreateNewProjectDialog extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    NewProjectLayoutBinding layout;
    String projectName;
    private List<AndroidBasePluginHandle.PluginParams> pluginParams;
    private Disposable name;
    private Disposable items;
    private Disposable plugins;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = DataBindingUtil.setContentView(this, R.layout.new_project_layout);

        setSupportActionBar(layout.toolbar);
        getSupportActionBar().setTitle("create new project");
        layout.toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        layout.toolbar.setNavigationOnClickListener(v -> this.finish());


        layout.addPlugin.setOnClickListener(v -> {
            showFilePicker();
        });

        initPlugins();
        pluginParams = new ArrayList<>();

        items = RxAdapterView.itemSelections(layout.plugins)
                .skipInitialValue()
                .map(i -> pluginParams.get(i))
                .subscribe(params -> {
                    layout.pluginParams.name.setText(params.getPluginName());
                    layout.pluginParams.version.setText(params.getPluginVersion());
                    layout.pluginParams.description.setText(params.getPluginDescription());
                });

        name = RxTextView.textChanges(layout.projectName)
                .skipInitialValue()
                .map(String::valueOf)
                .map(n -> projectName = n)
                .map(s -> !s.isEmpty())
                .subscribe(b ->
                        layout.toolbar.findViewById(R.id.bt_create_project).setEnabled(b));

        layout.toolbar.post(() -> layout.toolbar.findViewById(R.id.bt_create_project).setEnabled(false));
    }

    private void showFilePicker() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"fcp"};

        FilePickerDialog dialog = new FilePickerDialog(this, properties);

        dialog.setDialogSelectionListener(files -> {

            File source = new File(files[0]);

            String destinationPath = Files.getPLuginsLocation() + source.getName();
            File destination = new File(destinationPath);
            Files.copyFile(source, destination);
            initPlugins();
        });

        dialog.show();
    }

    private void initPlugins() {
        layout.plugins.setVisibility(View.INVISIBLE);
        layout.progress.setEnabled(true);
        layout.progress.setVisibility(View.VISIBLE);

        Gson gson = new Gson();

        plugins = Observable.just(Files.getPLuginsLocation())
                .map(File::new)
                .map(File::listFiles)
                .flatMap(Observable::fromArray)
                .map(ZipFile::new)
                .map(file -> file.getInputStream(file.getEntry(PluginHelper.indexFile)))
                .map(Files::inputStreamToString)
                .map(json -> gson.fromJson(json, AndroidBasePluginHandle.PluginParams.class))
                .toList()
                .map(pluginParamses -> {
                    pluginParamses.add(PluginHelper.getStandartPluginParams(getAssets()));
                    return pluginParamses;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(params -> {

                    pluginParams = params;
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                                    Stream.of(params)
                                            .map(AndroidBasePluginHandle.PluginParams::getPluginName)
                                            .toList()
                            );

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    layout.plugins.setAdapter(adapter);

                    layout.progress.setVisibility(View.INVISIBLE);
                    layout.progress.setEnabled(false);
                    layout.plugins.setVisibility(View.VISIBLE);
                });
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_new_project, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_create_project:
//                FlowchartProject.createNew(layout.projectName.toString());
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        name.dispose();
        items.dispose();
        plugins.dispose();
    }
}
