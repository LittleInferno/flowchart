package com.littleinferno.flowchart.plugin.bridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.widget.EditText;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.node.AndroidNode;


@SuppressWarnings("unused")
@SuppressLint("ViewConstructor")
public class TextField extends TextInputLayout implements ViewName {

    private EditText editText;
    private String name;

    public static TextField make(String name, AndroidNode node, DataType dataType) {
        return new TextField(name, node.getContext(), dataType);
    }

    public static String getText(AndroidNode node, String name) {
        TextField view = (TextField) node.getView(name);
        return view != null ? view.getText() : "";
    }

    public static void setText(AndroidNode node, String name, String text) {
        TextField view = (TextField) node.getView(name);
        if (view != null)
            view.setText(text);
    }

    private void setText(String text) {
        editText.setText(text);
    }

    private TextField(String name, Context context, DataType dataType) {
        super(context);
        this.name = name;

        inflate(context, R.layout.text_field, this);
        editText = (EditText) findViewById(R.id.edit_text);

        switch (dataType) {
            case BOOL:
                break;
            case INT:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                break;
            case FLOAT:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case STRING:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    String getText() {
        return editText.getText().toString();
    }

    @Override
    public String getName() {
        return name;
    }
}
