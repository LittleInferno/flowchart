package com.littleinferno.flowchart;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 200);

        setSupportActionBar(layout.mainMenuToolbar);

//        findViewById(R.id.start).setOnClickListener(v -> {
////
////            ProjectFragment fragment = new ProjectFragment();
////            getSupportFragmentManager()
////                    .beginTransaction()
////                    .add(R.id.project, fragment, "project")
////                    .commit();
//        });

        ProjectsAdaptor adapter = new ProjectsAdaptor(Stream.of(Files.getProjects()).map(ProjectItem::new).toList());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        layout.projectLayout.projectList.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        layout.projectLayout.projectList.setLayoutManager(layoutManager);
        layout.projectLayout.projectList.setItemAnimator(new DefaultItemAnimator());
        layout.projectLayout.projectList.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
}
