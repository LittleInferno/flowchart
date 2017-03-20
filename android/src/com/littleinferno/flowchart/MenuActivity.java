package com.littleinferno.flowchart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.annimon.stream.Stream;

public class MenuActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 200);

//
//        findViewById(R.id.start).setOnClickListener(v -> {
//
//            ProjectFragment fragment = new ProjectFragment();
//            getSupportFragmentManager()
//                    .beginTransaction()
////                    .add(R.id.project, fragment, "project")
//                    .commit();
//        });
//
        recyclerView = (RecyclerView) findViewById(R.id.project_list);

        ProjectsAdaptor adapter = new ProjectsAdaptor(Stream.of(Files.getProjects()).map(ProjectItem::new).toList());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        ProjectsAdaptor.ItemDecoration dividerItemDecoration = new ProjectsAdaptor.ItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
