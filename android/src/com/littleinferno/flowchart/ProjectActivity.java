package com.littleinferno.flowchart;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.databinding.ActivityProjectBinding;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.gui.FunctionListFragment;
import com.littleinferno.flowchart.node.gui.Section;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;
import com.littleinferno.flowchart.plugin.PluginHelper;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.scene.gui.SceneFragment;
import com.littleinferno.flowchart.variable.gui.VariableListFragment;

import java.util.List;

public class ProjectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityProjectBinding layout;
    private FlowchartProject flowchartProject;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flowchartProject.unloadPlugin();
        flowchartProject.clearContext();
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


        if (savedInstanceState != null && savedInstanceState.containsKey(FlowchartProject.TAG))
            flowchartProject = savedInstanceState.getParcelable(FlowchartProject.TAG);
        else
            flowchartProject = FlowchartProject.create(this, "t");

        assert flowchartProject != null;
        flowchartProject.setContext(this);

        AndroidPluginHandle androidPluginHandle = null;
        try {
            androidPluginHandle = new AndroidPluginHandle(PluginHelper.getStandartPluginContent(getAssets()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        flowchartProject.setPlugin(androidPluginHandle);

        List<AndroidPluginHandle.NodeHandle> handles = flowchartProject.getNodeHandles();

        layout.projectMain.projectLayout.nodes.init(flowchartProject, Stream.of(handles)
                .map(AndroidPluginHandle.NodeHandle::getCategory)
                .distinct()
                .filter(v -> !v.equals("system"))
                .map(s -> new Section(s, Stream.of(handles)
                        .filter(h -> h.getCategory().equals(s))
                        .map(AndroidPluginHandle.NodeHandle::getName)
                        .toList())).toList());

        flowchartProject.setFragmentManager(getFragmentManager());
        AndroidFunction main = flowchartProject.getFunctionManager().getFunction(flowchartProject.getRules().getEntryPoint());
        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidFunction.TAG, main);
        SceneFragment scene = new SceneFragment();
        scene.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.scene_frame, scene).commit();

        SceneFragment.show(main, getFragmentManager(), R.id.scene_frame);

    }


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + Files.projectLocation + "/f.png");
//
////            Bitmap bitmap = loadBitmapFromView(layout.projectMain.projectLayout.projectFrame);
//
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//            // PNG is a lossless format, the compression factor (100) is ignored
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_actions, menu);


        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FlowchartProject.TAG, flowchartProject);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        flowchartProject = savedInstanceState.getParcelable(FlowchartProject.TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.variable: {
                VariableListFragment.show(flowchartProject.getVariableManager(), getFragmentManager());
                break;
            }
            case R.id.function: {
                FunctionListFragment.show(flowchartProject.getFunctionManager(), getFragmentManager());
                break;
            }
            case R.id.start: {
                String generate = flowchartProject.getFunctionManager().generate();
                Log.d("GENERATE", generate);
                break;
            }

        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        flowchartProject.save();
        return false;
    }


}
