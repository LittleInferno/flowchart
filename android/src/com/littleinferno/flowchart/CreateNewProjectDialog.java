package com.littleinferno.flowchart;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.littleinferno.flowchart.databinding.NewProjectLayoutBinding;
import com.littleinferno.flowchart.project.FlowchartProject;


public class CreateNewProjectDialog extends AppCompatActivity {

    NewProjectLayoutBinding layout;
    String projectName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = DataBindingUtil.setContentView(this, R.layout.new_project_layout);

        setSupportActionBar(layout.toolbar);
        getSupportActionBar().setTitle("create new project");
        layout.toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        layout.toolbar.setNavigationOnClickListener(v -> this.finish());

    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        layout = DataBindingUtil.inflate(inflater, , container, false);
//
//
//        Observable<String> filter = RxTextView.textChanges(layout.projectName)
//                .map(chars -> chars.toString().trim())
//                .filter(s -> !s.isEmpty())
//                .filter(s -> !Files.getProjects().contains(s))
//                .map(s -> projectName = s);
//
////                .subscribe(b -> layout.create.setEnabled(b))
//
//
//        return layout.getRoot();
//    }

    void setProjectName(final String name) {
        projectName = name;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_new_project, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_create_project:
                FlowchartProject.createNew(layout.projectName.toString());
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
