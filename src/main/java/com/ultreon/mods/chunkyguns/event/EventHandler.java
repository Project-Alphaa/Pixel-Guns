package com.ultreon.mods.chunkyguns.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventHandler<T> {

    private final List<T> listeners = new ArrayList<>();

    public void registerListener(T listener) {
        listeners.add(listener);
    }

    public void invokeEvent(Consumer<T> consumer) {
        for (T listener : listeners) {
            consumer.accept(listener);
        }
    }
}
