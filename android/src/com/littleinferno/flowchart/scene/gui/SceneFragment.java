package com.littleinferno.flowchart.scene.gui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.databinding.LayoutSceneBinding;
import com.littleinferno.flowchart.function.AndroidFunction;

public class SceneFragment extends Fragment {

    LayoutSceneBinding layout;

    private AndroidFunction function;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutSceneBinding.inflate(inflater, container, false);

        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();

        if (bundle != null)
            function = bundle.getParcelable(AndroidFunction.TAG);

        if (function == null)
            throw new RuntimeException("function cannot be null");

        function.getProject().setCurrentScene(function);
        function.bindScene(layout.scene);

        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(function.getProject().getName() + " - " + function.getName());
    }


    public static void show(@NonNull AndroidFunction function, @NonNull FragmentManager fragmentManager, @IdRes int layout) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AndroidFunction.TAG, function);
        SceneFragment scene = new SceneFragment();
        scene.setArguments(bundle);
        fragmentManager.beginTransaction().replace(layout, scene).commit();
    }
}
