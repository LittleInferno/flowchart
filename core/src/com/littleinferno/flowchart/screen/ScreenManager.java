package com.littleinferno.flowchart.screen;

import com.annimon.stream.Stream;
import com.badlogic.gdx.Screen;
import com.littleinferno.flowchart.Application;
import com.littleinferno.flowchart.util.managers.AbstactManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScreenManager extends AbstactManager {

    private HashMap<String, AbstractScreen> screens;

    public ScreenManager(Application app) {
        super(app);
        initScreens();
        setScreen("menu");
    }

    private void initScreens() {
        this.screens = new HashMap<>();
        this.screens.put("menu", new MenuScreen(this));
    }

    @Override
    public void dispose() {
        Stream.of(screens)
                .map(Map.Entry::getValue)
                .filter(Objects::nonNull)
                .forEach(Screen::dispose);

        screens.clear();
    }

    public void setScreen(String name) {
        getApp().setScreen(screens.get(name));
    }

    public void addScreen(String name, AbstractScreen screen) {
        screens.put(name, screen);
    }

}
