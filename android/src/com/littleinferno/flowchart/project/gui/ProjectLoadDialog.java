package com.littleinferno.flowchart.project.gui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleinferno.flowchart.databinding.LayoutProjectLoadBinding;
import com.littleinferno.flowchart.project.FlowchartProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProjectLoadDialog extends DialogFragment {

    private LayoutProjectLoadBinding layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutProjectLoadBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        String string = Environment.getExternalStorageDirectory().toString();
        File file = new File(string + "/flowchart_projects/plugins/codegen.js");

        String s = null;
        try {
            s = readFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FlowchartProject load = FlowchartProject.load(null);
        load.getPluginManager().loadCodeGeneratorPlugin(s);

    }

    String readFile(File file) throws FileNotFoundException {
        int fileSize = (int) file.length();
        layout.progress.setMax(fileSize);

        Scanner scanner = new Scanner(new InputStreamReader(new FileInputStream(file)));

        StringBuilder text = new StringBuilder();

        Observable.just(scanner)
                .subscribeOn(Schedulers.io())
                .takeWhile(Scanner::hasNextLine)
                .map(Scanner::nextLine)
                .doOnEach(text::append)
                .map(String::length)
                .reduce((i1, i2) -> i1 += i2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(layout.progress::setProgress);


        return text.toString();
    }
}
