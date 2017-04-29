package com.littleinferno.flowchart.plugin.bridge;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.widget.EditText;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.node.BaseNode;


public class TextField extends TextInputLayout {

    private EditText editText;

    public static TextField make(BaseNode node, DataType dataType) {
        TextField textField = new TextField(node.getContext(), dataType);


        return textField;
    }


    private TextField(Context context, DataType dataType) {
        super(context);
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
}
