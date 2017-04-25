package com.littleinferno.flowchart.pin;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.jakewharton.rxbinding2.view.RxView;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.node.BaseNode;
import com.littleinferno.flowchart.util.Destroyable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.Disposable;

public class Connector extends android.support.v7.widget.AppCompatTextView implements Destroyable {

    private final Connection connection;
    private final Disposable click;
    private DataType type;
    private boolean isArray;

    private final BaseNode node;

    private Optional<Connector> connectedPin;
    private Optional<List<Connector>> connectedPins;

    private final Optional<Set<DataType>> possibleConvert;

    private static Optional<Connector> connector = Optional.empty();

    public Connector(BaseNode node, LinearLayout.LayoutParams params, Connection connection, String name, boolean isArray, Optional<Set<DataType>> possibleConvert, DataType type) {
        super(node.getContext());

        this.connection = connection;
        this.node = node;

        if (this.connection == Connection.INPUT)
            this.node.addView(this, BaseNode.Align.LEFT);
        else
            this.node.addView(this, BaseNode.Align.RIGHT);

        this.node.invalidate();
        this.possibleConvert = possibleConvert;

        click = RxView.clicks(this).subscribe(o -> connector.ifPresentOrElse(c -> c.connect(this),
                () -> connector = Optional.of(this)));

        connectedPin = Optional.empty();
        connectedPins = Optional.empty();

        setClickable(true);
        setBack();
        setLayoutParams(params);
        setName(name);
        setArray(isArray);
        setImage();
        setType(type);
    }

    private void setBack() {
        TypedValue value = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
        setBackgroundResource(value.resourceId);
    }

    public void setName(String name) {
        setText(name);
    }

    public String getName() {
        return getText().toString();
    }

    public void setArray(boolean isArray) {
        if (this.isArray == isArray)
            return;

        setImage();

        this.isArray = isArray;
    }

    private void setImage() {
        int i;

        if (isArray)
            i = isConnect() ? R.drawable.ic_pin_array_connect_true : R.drawable.ic_pin_array_connect_false;
        else
            i = isConnect() ? R.drawable.ic_pin_connect_true : R.drawable.ic_pin_connect_false;

        if (getConnection() == Connection.INPUT)
            setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), i), null, null, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), i), null);
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

                Stream.of(node.getPins())
                        .filter(pin -> pin.getType() == DataType.UNIVERSAL)
                        .forEach(pin -> pin.setType(type));
            }
        }, () -> {
            this.type = type;
            setColor(type);
        });
    }

    public DataType getType() {
        return type;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnect() {
        if (isSingle())
            return connectedPin.isPresent();

        return connectedPins.isPresent() && connectedPins.get().size() > 0;
    }

    public boolean connect(final Connector pin) {
        connector = Optional.empty();

        if (!possibleConnect(pin))
            return false;

        DataType pinType = pin.getType();
        DataType thisType = getType();

        if (pinType != thisType) {
            boolean success = false;

            if (pinType == DataType.UNIVERSAL) {
                pin.setType(thisType);
                success = pin.getType() == thisType;

            } else if (thisType == DataType.UNIVERSAL) {
                setType(pinType);
                success = getType() == pinType;
            }

            if (!success) return false;
        }

        if (isSingle())
            connectPin(pin);
        else
            connectPins(pin);

        return true;
    }

    private void connectPin(final Connector pin) {

        connectedPin.ifPresent(c -> c.disconnect(this));

        pin.connectedPins.executeIfAbsent(() -> pin.connectedPins = Optional.of(new ArrayList<>()));
        pin.connectedPins.ifPresent(connectors -> connectors.add(this));

        connectedPin = Optional.of(pin);
        setImage();
        pin.setImage();

        node.getScene().addWire(this, pin);
    }

    private void connectPins(final Connector pin) {
        pin.connectPin(this);
    }

    public void disconnectAll() {
        if (isSingle() && isConnect()) {
            disconnectPin();
        } else {
            connectedPins
                    .ifPresent(connectors -> Stream.of(connectors)
                            .forEach(c -> c.disconnect(this)));
        }
    }

    public void disconnect(final Connector pin) {
        if (isSingle()) {
            disconnectPin();
        } else {
            disconnectPins(pin);
        }
    }

    private void disconnectPin() {

        connectedPin.ifPresent(c -> {
            c.connectedPins.ifPresent(l -> l.remove(this));
            c.setImage();
        });

        connectedPin = Optional.empty();
        setImage();
    }

    private void disconnectPins(final Connector pin) {
        pin.disconnectPin();
    }

    private boolean possibleConnect(Connector pin) {
        return !(pin.getConnection() == getConnection()
                || pin.getParent() == getParent()
                || pin.isArray() != isArray());
    }

    private boolean isSingle() {
        return (getType() == DataType.EXECUTION && getConnection() == Connection.OUTPUT) ||
                (getType() != DataType.EXECUTION && getConnection() == Connection.INPUT);
    }

    private void setColor(DataType type) {
        switch (type) {
            case EXECUTION:
                set(R.color.Execution);
                break;
            case BOOL:
                set(R.color.Bool);
                break;
            case INT:
                set(R.color.Int);
                break;
            case FLOAT:
                set(R.color.Float);
                break;
            case STRING:
                set(R.color.String);
                break;
            case UNIVERSAL:
                set(R.color.Universal);
                break;
        }

    }

    private void set(int id) {
        Drawable drawable = getImage();
        if (drawable == null)
            setImage();

        getImage().setColorFilter(ContextCompat.getColor(getContext(), id), PorterDuff.Mode.DST);
    }

    public Drawable getImage() {
        if (getConnection() == Connection.INPUT)
            return getCompoundDrawables()[0];
        else
            return getCompoundDrawables()[2];
    }

    @Override
    public void onDestroy() {
        click.dispose();
    }
}