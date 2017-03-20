package com.littleinferno.flowchart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class ProjectActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

            ProjectFragment fragment = new ProjectFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.project_fragment, fragment, "project")
                    .commit();
    }

    @Override
    public void exit() {

    }
}
