package com.littleinferno.flowchart.project.gui;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.littleinferno.flowchart.databinding.LayoutCodeViewBinding;
import com.littleinferno.flowchart.util.Files;
import com.littleinferno.flowchart.util.Generator;

import java.io.File;

import thereisnospon.codeview.CodeViewTheme;

public class CodeViewDialog extends DialogFragment {

    LayoutCodeViewBinding layout;
    private String code;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = LayoutCodeViewBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getDialog().getWindow();
        lp.copyFrom(window.getAttributes());

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        window.setAttributes(lp);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle arguments = getArguments();

        if (arguments != null) {
            code = arguments.getString(Generator.CODE);
            layout.codeview.setTheme(CodeViewTheme.GITHUB);
            layout.codeview.showCode(code);

        }

        layout.back.setOnClickListener(v -> dismiss());
        layout.save.setOnClickListener(v -> showFilePicker());
    }

    private void showFilePicker() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.DIR_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);

        FilePickerDialog dialog = new FilePickerDialog(getContext(), properties);

        dialog.setDialogSelectionListener(files -> {
            Files.writeToFile(getContext(), files[0], code);
        });

        dialog.show();
    }

    public static void show(String code, FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        bundle.putString(Generator.CODE, code);

        CodeViewDialog codeViewDialog = new CodeViewDialog();

        codeViewDialog.setArguments(bundle);
        codeViewDialog.show(fragmentManager, "CODE_VIEW");
    }
}
