package com.littleinferno.flowchart.util;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class EventWrapper extends ChangeListener {
    private Event event;

    public EventWrapper(Event event) {
        this.event = event;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        this.event.changed(event, actor);
    }

    public interface Event {
        void changed(ChangeEvent event, Actor actor);
    }
}
