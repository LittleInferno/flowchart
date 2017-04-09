package com.littleinferno.flowchart;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.littleinferno.flowchart.databinding.ActivityProjectBinding;
import com.littleinferno.flowchart.node.BaseNode;
import com.littleinferno.flowchart.nodes.NodeFragmet;
import com.littleinferno.flowchart.pin.Connector;

import java.io.FileOutputStream;
import java.io.IOException;

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

        FlowchartProject flowchartProject = FlowchartProject.load();
        flowchartProject.setCurrentScene(layout.projectMain.projectLayout.projectFrame);
        flowchartProject.setLayout(layout.projectMain.projectLayout.projectLayout);

        BaseNode tv = new BaseNode(layout.projectMain.projectLayout.projectFrame);
        tv.setLayoutParams(lparams);
        tv.setX(10);
        tv.setY(400);
        tv.addDataInputPin("cc", false, DataType.BOOL);
        tv.addDataOutputPin("gg", true, DataType.BOOL, DataType.INT);
        tv.addDataOutputPin("dfg", true, DataType.INT);
        Connector ff1 = tv.addDataOutputPin("bvc", true, DataType.FLOAT);
        tv.addDataOutputPin("ff", true, DataType.STRING);
        layout.projectMain.projectLayout.projectFrame.addView(tv);

        BaseNode node = new BaseNode(layout.projectMain.projectLayout.projectFrame);
        node.setLayoutParams(lparams);
        node.setX(500);
        node.setY(400);
        node.addDataInputPin("aa", false, DataType.BOOL);
        node.addDataInputPin("ff", true, DataType.BOOL, DataType.INT);
        node.addDataInputPin("ff", true, DataType.INT);
        Connector ff = node.addDataInputPin("ff", true, DataType.FLOAT);
        node.addDataInputPin("ff", true, DataType.STRING);
        layout.projectMain.projectLayout.projectFrame.addView(node);
        ff.connect(ff1);

    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + Files.projectLocation + "/f.png");

            Bitmap bitmap = loadBitmapFromView(layout.projectMain.projectLayout.projectFrame);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
