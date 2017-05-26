package com.littleinferno.flowchart.plugin.gui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.annimon.stream.Stream;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.gson.Gson;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.ActivityPluginBinding;
import com.littleinferno.flowchart.plugin.BasePluginHandle;
import com.littleinferno.flowchart.plugin.PluginHelper;
import com.littleinferno.flowchart.util.Files;
import com.littleinferno.flowchart.util.Swiper;

import net.idik.lib.slimadapter.SlimAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PluginActivity extends AppCompatActivity {

    ActivityPluginBinding layout;
    private Disposable plugins;
    private List<BasePluginHandle.PluginParams> pluginParams;
    private SlimAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = DataBindingUtil.setContentView(this, R.layout.activity_plugin);

        setSupportActionBar(layout.toolbar);
        layout.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("plugins");
        layout.toolbar.setNavigationOnClickListener(v -> this.finish());

        layout.plugins.items.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = SlimAdapter
                .create()
                .registerDefault(R.layout.layout_plugin_params, (o, injector) -> {
                    BasePluginHandle.PluginParams data = (BasePluginHandle.PluginParams) o;
                    injector.text(R.id.name, data.getPluginName())
                            .text(R.id.version, data.getPluginVersion())
                            .text(R.id.description, data.getPluginDescription());
                })
                .attachTo(layout.plugins.items);

        Swiper.create(this)
                .swipeLeft(PluginActivity.this::removePlugin)
                .swipeRule(vh -> pluginParams
                        .get(vh.getLayoutPosition())
                        .getPluginName()
                        .equals("standart plugin"))
                .icon(R.drawable.ic_delete)
                .iconColor(Color.WHITE)
                .backgroundColor(Color.RED)
                .attachTo(layout.plugins.items);

        initPlugins();

        layout.addPlugin.setOnClickListener(v -> {
            showFilePicker();
        });
    }

    private void removePlugin(int position) {
        Files.delete(this, pluginParams.remove(position).getPluginName());
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, pluginParams.size());
    }

    private void showFilePicker() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"fcp"};

        FilePickerDialog dialog = new FilePickerDialog(this, properties);

        dialog.setDialogSelectionListener(files -> {

            Stream.of(files).forEach(s -> {
                File source = new File(s);
                String destinationPath = Files.getPLuginsLocation() + "/" + source.getName();
                File destination = new File(destinationPath);
                Files.copyFile(this, source, destination);
            });

            initPlugins();
        });

        dialog.show();
    }

    private void initPlugins() {
        layout.plugins.items.setVisibility(View.INVISIBLE);
        layout.progress.setEnabled(true);
        layout.progress.setVisibility(View.VISIBLE);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            pluginParams = new ArrayList<>();
            try {
                pluginParams.add(PluginHelper.getStandartPluginParams(getAssets()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            adapter.updateData(pluginParams);
            layout.progress.setVisibility(View.INVISIBLE);
            layout.progress.setEnabled(false);
            layout.plugins.items.setVisibility(View.VISIBLE);
            layout.addPlugin.setEnabled(false);
            return;
        }
        Gson gson = new Gson();

        plugins = Observable.just(Files.getPLuginsLocation())
                .map(File::new)
                .map(File::listFiles)
                .flatMap(Observable::fromArray)
                .map(ZipFile::new)
                .map(file -> file.getInputStream(file.getEntry(PluginHelper.INDEX_FILE)))
                .map(Files::inputStreamToString)
                .map(json -> gson.fromJson(json, BasePluginHandle.PluginParams.class))
                .toList()
                .map(pluginParamses -> {
                    pluginParamses.add(PluginHelper.getStandartPluginParams(getAssets()));
                    return pluginParamses;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(params -> {
                    pluginParams = params;
                    adapter.updateData(pluginParams);
                    layout.progress.setVisibility(View.INVISIBLE);
                    layout.progress.setEnabled(false);
                    layout.plugins.items.setVisibility(View.VISIBLE);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (plugins != null)
            plugins.dispose();

    }
}
