package com.littleinferno.flowchart;

import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.littleinferno.flowchart.databinding.ActivityProjectBinding;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionManager;
import com.littleinferno.flowchart.function.MainFunction;
import com.littleinferno.flowchart.function.gui.FunctionListFragment;
import com.littleinferno.flowchart.nodes.NodeFragmet;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.scene.gui.SceneFragment;
import com.littleinferno.flowchart.variable.AndroidVariableManager;
import com.littleinferno.flowchart.variable.gui.VariableListFragment;

public class ProjectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityProjectBinding layout;
    private AndroidVariableManager variableManager;
    private AndroidFunctionManager functionManager;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FlowchartProject.getProject().getPluginManager().unloadNodePlugins();

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

        NodeFragmet fr = new NodeFragmet();
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.project_ui_frame, fr, "ui")
                .commit();


        RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        FlowchartProject flowchartProject = FlowchartProject.load(this);

        variableManager = new AndroidVariableManager(FlowchartProject.getProject());

        functionManager = new AndroidFunctionManager(FlowchartProject.getProject());
        functionManager.createFunction("func1");
        functionManager.createFunction("func2");
        functionManager.createFunction("fun1");
        functionManager.createFunction("fun9");

        flowchartProject.setLayout(layout.projectMain.projectLayout.sceneFrame);
        flowchartProject.setFragmentManager(getFragmentManager());
        MainFunction main = functionManager.createMain();
        flowchartProject.setCurrentScene(main);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidFunction.TAG, main);
        SceneFragment scene = new SceneFragment();
        scene.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.scene_frame, scene).commit();


        String string = Environment.getExternalStorageDirectory().toString() + "/flowchart_projects/plugins/codegen.js";


//        String str = Files.readToString(new File(string));

//        Codeview.with(getApplicationContext())
//                .withCode(str)
//                .setStyle(Settings.WithStyle.DARKULA)
//                .setLang(Settings.Lang.JAVASCRIPT)
//                .into(layout.projectMain.projectLayout.codeView);

    }

//    public static Bitmap loadBitmapFromView(View v) {
//        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//        v.draw(c);
//        return b;
//    }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.variable: {

                VariableListFragment variableListFragment = new VariableListFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AndroidVariableManager.TAG, variableManager);
                variableListFragment.setArguments(bundle);
                variableListFragment.show(getFragmentManager(), "variable");
                break;
            }
            case R.id.function: {

                FunctionListFragment functionListFragment = new FunctionListFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AndroidFunctionManager.TAG, functionManager);
                functionListFragment.setArguments(bundle);

                functionListFragment.show(getFragmentManager(), "function");
                break;
            }

        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


}
