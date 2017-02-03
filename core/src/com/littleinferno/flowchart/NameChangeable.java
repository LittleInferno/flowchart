package com.littleinferno.flowchart;


public interface NameChangeable {

    void addListener(NameChange listener);

    void notifyListenersNameChanged(String newName);

    interface NameChange {
        void changed(String newName);
    }
}
