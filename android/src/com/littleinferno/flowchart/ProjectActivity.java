package com.littleinferno.flowchart;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.littleinferno.flowchart.databinding.ActivityProjectBinding;
import com.littleinferno.flowchart.node.BaseNode;
import com.littleinferno.flowchart.nodes.NodeFragmet;

public class ProjectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityProjectBinding layout;

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

        BaseNode tv = new BaseNode(this);
        tv.setLayoutParams(lparams);
        tv.setX(10);
        tv.setY(400);
        tv.addDataInputPin("aa", false, DataType.BOOL);
        tv.addDataOutputPin("ff", true, DataType.BOOL, DataType.INT);
        tv.addDataOutputPin("ff", true, DataType.INT);
        tv.addDataOutputPin("ff", true, DataType.FLOAT);
        tv.addDataOutputPin("ff", true, DataType.STRING);
        layout.projectMain.projectLayout.projectFrame.addView(tv);

        BaseNode node = new BaseNode(this);
        node.setLayoutParams(lparams);
        node.setX(500);
        node.setY(400);
        node.addDataInputPin("aa", false, DataType.BOOL);
        node.addDataInputPin("ff", true, DataType.BOOL, DataType.INT);
        node.addDataInputPin("ff", true, DataType.INT);
        node.addDataInputPin("ff", true, DataType.FLOAT);
        node.addDataInputPin("ff", true, DataType.STRING);
        layout.projectMain.projectLayout.projectFrame.addView(node);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
