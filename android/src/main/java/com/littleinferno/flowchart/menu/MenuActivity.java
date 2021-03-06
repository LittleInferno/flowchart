package com.littleinferno.flowchart.menu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.databinding.ActivityMenuBinding;
import com.littleinferno.flowchart.plugin.gui.PluginActivity;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.project.gui.CreateNewProjectDialog;
import com.littleinferno.flowchart.project.gui.ProjectActivity;
import com.littleinferno.flowchart.util.Files;

import net.idik.lib.slimadapter.SlimAdapter;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MenuActivity extends AppCompatActivity implements ProjectUpdate {

    private ActivityMenuBinding layout;
    private SlimAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this, R.layout.activity_menu);

        setSupportActionBar(layout.mainMenuToolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        layout.projects.items.setLayoutManager(layoutManager);

        adapter = SlimAdapter.create().registerDefault(R.layout.item_project,
                (o, injector) -> {
                    injector.text(R.id.project_name, (String) o);

                    injector.clicked(R.id.project_root, v -> {
                        Intent intent = new Intent(v.getContext(), ProjectActivity.class);
                        intent.putExtra(Project.TAG, (String) o);
                        v.getContext().startActivity(intent);
                    });

                    injector.longClicked(R.id.project_root, v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(Project.PROJECT_NAME, (String) o);
                        ProjectDetailsDialog dialog = new ProjectDetailsDialog();
                        dialog.setDelListener(this);
                        dialog.setArguments(bundle);
                        dialog.show(getFragmentManager(), "DIALOG");

                        return true;
                    });

                })
                .attachTo(layout.projects.items);

        layout.addNewProject
                .setOnClickListener(v -> {
                    Intent intent = new Intent(MenuActivity.this, CreateNewProjectDialog.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                });

    }

    @Override
    protected void onResume() {
        super.onResume();

        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            Files.initFolder(this);
            initProjects();
        } else {
            requestPermissions(new String[]{permission}, 200);
        }

    }

    private void initProjects() {
        Observable.just(Files.getSavesLocation())
                .map(File::new)
                .map(File::list)
                .flatMap(Observable::fromArray)
                .map(Files::saveNameToProjectName)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> adapter.updateData(strings));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200 && grantResults.length > 0) {
            boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (readExternalFile) {
                Files.initFolder(this);
                initProjects();
                return;
            }
        }

        Toast.makeText(this, "need permissions", Toast.LENGTH_LONG).show();
        layout.addNewProject.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_plugins:
                startActivity(new Intent(this, PluginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update() {
        initProjects();
    }
}
