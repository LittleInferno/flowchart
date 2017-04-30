package com.littleinferno.flowchart.menu;

import android.content.Context;

import com.annimon.stream.Stream;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.List;

public class PieMenu extends ArcLayout {

    public PieMenu(Context context, List<PieMenuItem> items) {
        super(context);

        Stream.of(items).forEach(this::addView);
    }


}
