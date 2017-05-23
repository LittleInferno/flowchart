package com.littleinferno.flowchart.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.content.ContextCompat;

import com.littleinferno.flowchart.R;

public final class ResUtil {

    private ResUtil() {
    }

    public static Drawable getArrayDrawable(Context context, boolean isArray) {
        if (isArray)
            return ContextCompat.getDrawable(context, R.drawable.ic_array_true);
        else
            return ContextCompat.getDrawable(context, R.drawable.ic_array_false);
    }

    public static int getDataTypeColor(Context context, DataType dataType) {
        switch (dataType) {
            case BOOL:
                return ContextCompat.getColor(context, R.color.Bool);
            case INT:
                return ContextCompat.getColor(context, R.color.Int);
            case FLOAT:
                return ContextCompat.getColor(context, R.color.Float);
            case STRING:
                return ContextCompat.getColor(context, R.color.String);
            case UNIVERSAL:
                return ContextCompat.getColor(context, R.color.Universal);
            case EXECUTION:
                return ContextCompat.getColor(context, R.color.Execution);
        }
        return 0;
    }

    public static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
}
