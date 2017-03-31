package com.littleinferno.flowchart.pin;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.node.BaseNode;

import java.util.List;
import java.util.Set;

public class Connector extends LinearLayout {

    TextView text;
    ImageView image;

    private final Connection connection;
    private DataType type;
    private boolean isConnect;
    private boolean isArray;

    private final BaseNode node;

    private Optional<Connector> connectedPin;
    private Optional<List<Connector>> connectedPins;

    private final Optional<Set<DataType>> possibleConvert;

    public Connector(BaseNode node, LayoutParams params, Connection connection, String name, boolean isArray, Optional<Set<DataType>> possibleConvert, DataType type) {
        super(node.getContext());

        this.connection = connection;
        this.node = node;

        inflate(node.getContext(),
                connection == Connection.INPUT ?
                        R.layout.pin_input_layout :
                        R.layout.pin_output_layout, this);

        text = (TextView) findViewById(connection == Connection.INPUT ? R.id.tv_pin_in : R.id.tv_pin_out);
        image = (ImageView) findViewById(connection == Connection.INPUT ? R.id.iv_pin_in : R.id.iv_pin_out);

        if (this.connection == Connection.INPUT)
            this.node.getLayout().nodeLeft.addView(this);
        else
            this.node.getLayout().nodeRight.addView(this);

        this.node.invalidate();
        this.possibleConvert = possibleConvert;

        // = image.mutate();
        //mWrappedDrawable = DrawableCompat.wrap(mWrappedDrawable);
        //DrawableCompat.setTint(mWrappedDrawable, mColor);
        //DrawableCompat.setTintMode(mWrappedDrawable, PorterDuff.Mode.SRC_IN);

        connectedPin = Optional.empty();
        connectedPins = Optional.empty();


        setLayoutParams(params);
        setName(name);
        setArray(isArray);
        setType(type);
    }

    private void init(Context context) {
        inflate(context, R.layout.pin_output_layout, this);
    }

    public void setName(String name) {
        text.setText(name);
    }

    public String getName() {
        return String.valueOf(text.getText());
    }

    public void setArray(boolean isArray) {
        if (this.isArray == isArray)
            return;

        this.isArray = isArray;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setType(DataType type) {

        if (this.type == type)
            return;

        possibleConvert.ifPresentOrElse(dataTypes -> {
            if (dataTypes.contains(type) || type == DataType.UNIVERSAL) {
                this.type = type;
                setColor(type);
                connectedPin.ifPresent(connector -> connector.setType(type));

                connectedPins.ifPresent(connectors -> Stream
                        .of(connectors)
                        .forEach(connector -> connector.setType(type)));

//                Stream.of(parent.getPins())
//                        .filter(Pin::isUniversal)
//                        .forEach(pin -> pin.setType(newType));
            }
        }, () -> {
            this.type = type;
            setColor(type);
        });
    }

    private void setColor(DataType type) {

        switch (type) {
            case EXECUTION:
                break;
            case BOOL:
                image.setColorFilter(getContext().getResources().getColor(R.color.Bool));
                break;
            case INT:
                image.setColorFilter(getContext().getResources().getColor(R.color.Int));
                break;
            case FLOAT:
                image.setColorFilter(getContext().getResources().getColor(R.color.Float));
                break;
            case STRING:
                image.setColorFilter(getContext().getResources().getColor(R.color.String));
                break;
            case UNIVERSAL:
                image.setColorFilter(getContext().getResources().getColor(R.color.Universal));
                break;
        }

    }
}