package com.littleinferno.flowchart;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.databinding.ActivityProjectBinding;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.function.gui.FunctionListFragment;
import com.littleinferno.flowchart.node.gui.Section;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;
import com.littleinferno.flowchart.plugin.PluginHelper;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.project.gui.CreateNewProjectDialog;
import com.littleinferno.flowchart.project.gui.ViewCodeFragment;
import com.littleinferno.flowchart.scene.gui.SceneFragment;
import com.littleinferno.flowchart.util.Files;
import com.littleinferno.flowchart.variable.gui.VariableListFragment;

import java.util.List;

public class ProjectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityProjectBinding layout;
    private Project project;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        project.unloadPlugin();
        project.clearContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = DataBindingUtil.setContentView(this, R.layout.activity_project);

        setSupportActionBar(layout.projectMain.projectToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, layout.drawerLayout, layout.projectMain.projectToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        layout.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String name;

        if (savedInstanceState != null && savedInstanceState.containsKey(Project.TAG)) {
            project = savedInstanceState.getParcelable(Project.TAG);
            project.setContext(this);
        } else if ((name = getIntent().getStringExtra(Project.TAG)) != null) {
            project = Project.create(name);
            project.setContext(this);
            Files.loadProject(project);
        } else {
            project = Project.create(getIntent().getStringExtra(Project.PROJECT_NAME));
            project.setContext(this);
            try {
                String plugin = getIntent().getStringExtra("PLUGIN");
                if (plugin.equals("standart plugin")) {
                    project.setPlugin
                            (new AndroidPluginHandle(PluginHelper.getStandartPluginParams(getAssets()),
                                    PluginHelper.getStandartPluginContent(getAssets())));
                } else
                    project.setPlugin(Files.loadPlugin(plugin));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<AndroidPluginHandle.NodeHandle> handles = project.getNodeHandles();

        layout.projectMain.projectLayout.nodes.init(project, Stream.of(handles)
                .map(AndroidPluginHandle.NodeHandle::getCategory)
                .distinct()
                .filter(v -> !v.equals("system"))
                .map(s -> new Section(s, Stream.of(handles)
                        .filter(h -> h.getCategory().equals(s))
                        .map(AndroidPluginHandle.NodeHandle::getName)
                        .toList())).toList());

        project.setFragmentManager(getFragmentManager());
        Function main = project.getFunctionManager().getFunction(project.getRules().getEntryPoint());
        Bundle bundle = new Bundle();
        bundle.putParcelable(Function.TAG, main);
        SceneFragment scene = new SceneFragment();
        scene.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.scene_frame, scene).commit();

        SceneFragment.show(main, getFragmentManager(), R.id.scene_frame);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_actions, menu);


        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Project.TAG, project);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        project = savedInstanceState.getParcelable(Project.TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.variable: {
                VariableListFragment.show(project.getVariableManager(), getFragmentManager());
                break;
            }
            case R.id.function: {
                FunctionListFragment.show(project.getFunctionManager(), getFragmentManager());
                break;
            }
            case R.id.start: {
                String generate = project.getFunctionManager().generate();
                project.setGen(generate);
                Files.saveGen(project, generate);
                break;
            }
            case R.id.code: {
                String generate = project.getFunctionManager().generate();
                project.setGen(generate);
                Files.saveGen(project, generate);
                ViewCodeFragment.show(generate, getFragmentManager());
                break;
            }

        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.nav_save:
                project.save();
                break;
            case R.id.nav_close:
                finish();
                break;
            case R.id.nav_create_new:
                Intent intent = new Intent(this, CreateNewProjectDialog.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
        }
        project.save();
        return false;
    }


}
