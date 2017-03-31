package com.littleinferno.flowchart.node;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.databinding.NodeLayoutBinding;
import com.littleinferno.flowchart.pin.Connector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class BaseNode extends CardView {
    PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
    PointF StartPT = new PointF();

    NodeLayoutBinding layout;
    private float oldX;
    private float oldY;
    private int mActivePointerId;

    public BaseNode(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        layout = NodeLayoutBinding.inflate((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE), this, true);
    }

    public Connector addDataInputPin(final String name, final boolean isArray, final DataType... possibleConvert) {
        return buildPin(Connection.INPUT, name, isArray, possibleConvert);
    }

    public Connector addDataOutputPin(final String name, final boolean isArray, final DataType... possibleConvert) {
        return buildPin(Connection.OUTPUT, name, isArray, possibleConvert);
    }

    public Connector addExecutionInputPin(final String name) {
        return buildPin(Connection.INPUT, name, false, DataType.EXECUTION);
    }

    public Connector addExecutionOutputPin(final String name) {
        return buildPin(Connection.OUTPUT, name, false, DataType.EXECUTION);
    }

    Connector buildPin(final Connection connection, final String name, final boolean isArray, final DataType type) {
        return new Connector(this, createLayoutParams(),
                connection, name, isArray, Optional.empty(), type);
    }

    Connector buildPin(final Connection connection, final String name, final boolean isArray, final DataType... possibleConverts) {
        if (possibleConverts.length == 1)
            return buildPin(connection, name, isArray, possibleConverts[0]);

        return new Connector(this, createLayoutParams(),
                connection, name, isArray,
                Optional.of(new HashSet<DataType>(Arrays.asList(possibleConverts))),
                DataType.UNIVERSAL);
    }

    public void removePin(final Connector pin) {
        layout.nodeLeft.removeView(pin);
        layout.nodeRight.removeView(pin);

    }

    public List<Connector> getPins() {

        List<Connector> pins = Stream.range(0, layout.nodeLeft.getChildCount())
                .map(layout.nodeLeft::getChildAt)
                .filter(value -> value instanceof Connector)
                .map(Connector.class::cast)
                .toList();

        pins.addAll(Stream.range(0, layout.nodeRight.getChildCount())
                .map(layout.nodeRight::getChildAt)
                .filter(value -> value instanceof Connector)
                .map(Connector.class::cast)
                .toList());

        return pins;
    }


    LinearLayout.LayoutParams createLayoutParams() {
        return new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public NodeLayoutBinding getLayout() {
        return layout;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eid = event.getAction();
        switch (eid)
        {
            case MotionEvent.ACTION_MOVE :
                PointF mv = new PointF( event.getX() - DownPT.x, event.getY() - DownPT.y);
                setX((int)(StartPT.x+mv.x));
                setY((int)(StartPT.y+mv.y));
                StartPT = new PointF( getX(), getY() );
                break;
            case MotionEvent.ACTION_DOWN :
                DownPT.x = event.getX();
                DownPT.y = event.getY();
                StartPT = new PointF( getX(), getY() );
                break;
            case MotionEvent.ACTION_UP :
                // Nothing have to do
                break;
            default :
                break;
        }
        return true;
    }
}
